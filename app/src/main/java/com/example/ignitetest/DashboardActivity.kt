package com.example.ignitetest


import AddTODOTASK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ignitetest.databinding.ActivityDashboardBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class DashboardActivity : AppCompatActivity(),OnDialogClickListener {

    private var fab: FloatingActionButton? = null
   private var button:Button?=null

    private var mRecyclerview: RecyclerView? = null
    private var myDB: DBHelper? = null
    private var mList: List<Todo>? = null
    private var adapter: ToDoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mRecyclerview = findViewById(R.id.recyclerview)
        button=findViewById(R.id.signout)
        fab = findViewById(R.id.fab)
        myDB = DBHelper(this@DashboardActivity)
        mList = ArrayList()
        adapter = ToDoAdapter(myDB!!, this)

        mRecyclerview?.setHasFixedSize(true)
        mRecyclerview?.layoutManager = LinearLayoutManager(this)
        mRecyclerview?.adapter = adapter


        fab?.setOnClickListener {
            AddTODOTASK.newInstance().show(supportFragmentManager, AddTODOTASK.TAG)
        }
        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter!!))
        itemTouchHelper.attachToRecyclerView(mRecyclerview)



        supportActionBar?.title = "To Do List";

        button?.setOnClickListener{

            val goBack = Intent(applicationContext, MainActivity::class.java)
            startActivity(goBack)
        }

    }

    override fun onDialogClose(dialogInterface: DialogInterface?) {
        mList = myDB!!.getAllTasks()
        Collections.reverse(mList)
        adapter!!.setTasks(mList)
        adapter!!.notifyDataSetChanged()
    }

}