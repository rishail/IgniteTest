package com.example.ignitetest

import android.annotation.SuppressLint
import android.app.DownloadManager.*
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    private var db:SQLiteDatabase?=null

    private val TABLE_NAME = "TODO_TABLE"
    private val COL_1 = "ID"
    private val COL_2 = "TASK"
    private val COL_3 = "STATUS"



    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")")


    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL(DROP_USER_TABLE)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        onCreate(db)
    }

    fun insertTask(model: Todo) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model.task)
        values.put(COL_3, 0)
        db?.insert(TABLE_NAME, null, values)
    }

    fun updateTask(id: Int, task: String?) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, task)
        db?.update(TABLE_NAME,values,"ID=?",arrayOf(id.toString()))
    }

    fun updateStatus(id: Int, status: Int) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_3, status)
        db?.update(TABLE_NAME, values,"ID=?",arrayOf(id.toString()))
    }

    fun deleteTask(id: Int) {
        db = this.writableDatabase
        db?.delete(TABLE_NAME,"ID=?",arrayOf<String>(id.toString()))
    }

    @SuppressLint("Range")
    fun getAllTasks(): List<Todo> {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: MutableList<Todo> = java.util.ArrayList<Todo>()
        db?.beginTransaction()
        try {
            cursor = db?.query(TABLE_NAME,null,null,null,null,null,null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val task = Todo()
                        task.id=(cursor.getInt(cursor.getColumnIndex(COL_1)))
                        task.task=(cursor.getString(cursor.getColumnIndex(COL_2)))
                        task.status=(cursor.getInt(cursor.getColumnIndex(COL_3)))
                        modelList.add(task)
                    } while (cursor.moveToNext())
                }
            }
        }
        finally {
            db?.endTransaction()
            cursor!!.close()
        }
        cursor.close()
        return modelList
    }


    @SuppressLint("Range")
    fun getAllUser(): List<User> {

        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)

        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = ArrayList<User>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USER,
            columns,
            null,
            null,
            null,
            null,
            sortOrder)
        if (cursor.moveToFirst()) {
            do {
                val user = User(id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)))
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }


    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)
     
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun updateUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)

        db.update(TABLE_USER, values, "$COLUMN_USER_ID = ?",
            arrayOf(user.id.toString()))
        db.close()
    }


    fun deleteUser(user: User) {
        val db = this.writableDatabase

        db.delete(TABLE_USER, "$COLUMN_USER_ID = ?",
            arrayOf(user.id.toString()))
        db.close()
    }


    fun checkUser(email: String): Boolean {

        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase

        val selection = "$COLUMN_USER_EMAIL = ?"

        val selectionArgs = arrayOf(email)


        val cursor = db.query(TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)
        val cursorCount = cursor.count
        db.close()
        cursor.close()
        if (cursorCount > 0) {
            cursor.close()
            return true

        }
        cursor.close()
        return false
    }



    fun checkUser(email: String, password: String): Boolean {

        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase

        val selection = "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?"

        val selectionArgs = arrayOf(email, password)


        val cursor = db.query(TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)
        val cursorCount = cursor.count
        db.close()
        cursor.close()
        if (cursorCount > 0)
            return true
        return false
    }



    companion object {

        private const val DATABASE_VERSION = 1

        private const val DATABASE_NAME = "IgniteTest.db"

        private const val TABLE_USER = "user"

        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USER_NAME = "user_name"
        private const val COLUMN_USER_EMAIL = "user_email"
        private const val COLUMN_USER_PASSWORD = "user_password"

    }
}