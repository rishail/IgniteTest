package com.example.ignitetest.view

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ignitetest.R
import com.example.ignitetest.model.ToDoAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class RecyclerViewTouchHelper(adapter: ToDoAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val adapter: ToDoAdapter

    init {
        this.adapter = adapter
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.RIGHT) {
            val builder = AlertDialog.Builder(adapter.context)
            builder.setTitle("Delete Task")
            builder.setMessage("Are You Sure ?")
            builder.setPositiveButton(
                "Yes"
            ) { dialog, which -> adapter.deletTask(position) }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which -> adapter.notifyItemChanged(position) }
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
         RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(
                ContextCompat.getColor(
                    adapter.context,
                    R.color.colorPrimaryDark
                )
            )
            .addSwipeLeftActionIcon(R.drawable.baseline_mode_edit_outline_24)
            .addSwipeRightBackgroundColor(Color.RED)
            .addSwipeRightActionIcon(R.drawable.ic_delete)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}