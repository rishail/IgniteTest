package com.example.ignitetest.model

import com.example.ignitetest.view.AddTODOTASK
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.ignitetest.view.DashboardActivity
import com.example.ignitetest.R

class ToDoAdapter(myDB: DBHelper, private val activity: DashboardActivity) :
    RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {
    var mList: MutableList<Todo> = mutableListOf()

    private val myDB: DBHelper

    init {
        this.myDB = myDB
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.to_do_adapter, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: Todo = mList[position]
        holder.mCheckBox.text = item.task
        holder.mCheckBox.isChecked = toBoolean(item.status)
        holder.mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                myDB.updateStatus(item.id, 1)
            } else myDB.updateStatus(item.id, 0)
        }
    }

    fun toBoolean(num: Int): Boolean {
        return num != 0
    }

    val context: Context
        get() = activity

    fun setTasks(mList: List<Todo>?) {
        this.mList = mList as MutableList<Todo>
        notifyDataSetChanged()
    }

    fun deletTask(position: Int) {
        val item: Todo = mList[position]
        myDB.deleteTask(item.id)
        mList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item: Todo = mList[position]
        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("task", item.task)
        val task = AddTODOTASK()
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.getTag())
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCheckBox: CheckBox

        init {
            mCheckBox = itemView.findViewById(R.id.mcheckbox)
        }
    }
}