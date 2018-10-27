package com.b4kancs.scoutlaws.views.quiz.sorter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by hszilard on 24-Oct-18.
 */
class DragHelperCallback(private val adapter: DragHandler) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recycleView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags)
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder1: RecyclerView.ViewHolder,
                        viewHolder2: RecyclerView.ViewHolder)
            : Boolean {
        adapter.onViewMoved(viewHolder1.adapterPosition, viewHolder2.adapterPosition)
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        // Not needed
    }

    interface DragHandler {
        fun onViewMoved(oldPosition: Int, newPosition: Int)
    }
}
