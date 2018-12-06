package com.b4kancs.scoutlaws.views.quiz.sorter

import android.util.Log.DEBUG
import androidx.recyclerview.widget.ItemTouchHelper
import com.b4kancs.scoutlaws.views.quiz.sorter.OptionsRecyclerAdapter.FooterViewHolder
import com.b4kancs.scoutlaws.views.quiz.sorter.OptionsRecyclerAdapter.ItemViewHolder
import com.b4kancs.scoutlaws.logger.Logger.Companion.log

/**
 * Created by hszilard on 24-Oct-18.
 */
class DragHelperCallback(private val adapter: DragHandler) : ItemTouchHelper.Callback() {

    private companion object {
        val LOG_TAG = DragHelperCallback::class.simpleName
    }

    override fun getMovementFlags(recycleView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
        log(DEBUG, LOG_TAG, "getMovementFlags(..)")
        val dragFlags = when (viewHolder) {
            is ItemViewHolder -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            else -> 0
        }
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags)
    }

    override fun onMove(recyclerView: androidx.recyclerview.widget.RecyclerView,
                        viewHolder1: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                        viewHolder2: androidx.recyclerview.widget.RecyclerView.ViewHolder)
            : Boolean {
        log(DEBUG, LOG_TAG, "onMove(..)")
        if (viewHolder1 !is FooterViewHolder && viewHolder2 !is FooterViewHolder)
            adapter.onViewMoved(viewHolder1.adapterPosition, viewHolder2.adapterPosition)
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onSelectedChanged(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?, actionState: Int) {
        log(DEBUG, LOG_TAG, "onSelectedChanged(..)")
        // Triggers the lift animation
        viewHolder?.itemView?.isPressed = true
    }

    override fun clearView(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        log(DEBUG, LOG_TAG, "clearView(..)")
        // Cancels the lift animation when dropped
        viewHolder.itemView.isPressed = false
    }

    override fun onSwiped(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        // Not needed
    }

    interface DragHandler {
        fun onViewMoved(oldPosition: Int, newPosition: Int)
    }
}
