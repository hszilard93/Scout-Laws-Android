package com.b4kancs.scoutlaws.views.quiz.sorter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.b4kancs.scoutlaws.views.quiz.sorter.OptionsRecyclerAdapter.FooterViewHolder
import com.b4kancs.scoutlaws.views.quiz.sorter.OptionsRecyclerAdapter.ItemViewHolder

/**
 * Created by hszilard on 24-Oct-18.
 */
class DragHelperCallback(private val adapter: DragHandler) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recycleView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = when (viewHolder) {
            is ItemViewHolder -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            else -> 0
        }
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags)
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder1: RecyclerView.ViewHolder,
                        viewHolder2: RecyclerView.ViewHolder)
            : Boolean {
        if (viewHolder1 !is FooterViewHolder && viewHolder2 !is FooterViewHolder)
            adapter.onViewMoved(viewHolder1.adapterPosition, viewHolder2.adapterPosition)
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // Triggers the lift animation
        viewHolder?.itemView?.isPressed = true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        // Cancels the lift animation when dropped
        viewHolder.itemView.isPressed = false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        // Not needed
    }

    interface DragHandler {
        fun onViewMoved(oldPosition: Int, newPosition: Int)
    }
}
