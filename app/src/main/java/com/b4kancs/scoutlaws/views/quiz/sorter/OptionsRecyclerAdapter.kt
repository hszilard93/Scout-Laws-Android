package com.b4kancs.scoutlaws.views.quiz.sorter

import android.util.Log.DEBUG
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import com.b4kancs.scoutlaws.databinding.ViewHolderSorterItemBinding
import com.b4kancs.scoutlaws.logger.Logger.Companion.log

/**
 * Created by hszilard on 15-Oct-18.
 */
class OptionsRecyclerAdapter(private val scoutLawSequence: ArrayList<ScoutLaw>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>(), DragHelperCallback.DragHandler {

    companion object {
        const val FOOTER_VIEW = 1000
        private val LOG_TAG = OptionsRecyclerAdapter::class.simpleName
    }

    lateinit var touchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeAndPosition: Int): ViewHolder {
        log(DEBUG, LOG_TAG, "onCreateViewHolder(.., viewTypeAndPosition = $viewTypeAndPosition)")
        val layoutInflater = LayoutInflater.from(parent.context)

        if (viewTypeAndPosition == FOOTER_VIEW) {
            // Empty footer view
            val footer = TextView(parent.context)
            footer.height = 4
            return FooterViewHolder(footer)
        } else {
            val binding = ViewHolderSorterItemBinding.inflate(layoutInflater, parent, false)
            binding.scoutLaw = scoutLawSequence[viewTypeAndPosition]
            return ItemViewHolder(binding).also {
                it.itemView.setOnTouchListener { _, e ->
                    if (e.action == MotionEvent.ACTION_DOWN) {
                        log(INFO, LOG_TAG, "Item dragged: ${scoutLawSequence[viewTypeAndPosition]}")
                        touchHelper.startDrag(it)
                    }
                    true
                }
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        log(DEBUG, LOG_TAG, "onBindViewHolder(.., position = $position)")
        if (viewHolder is ItemViewHolder) {
            viewHolder.itemBinding.scoutLaw = scoutLawSequence[position]
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        log(DEBUG, LOG_TAG, "onViewMoved(oldPosition = $oldPosition, newPosition = $newPosition)")
        val law = scoutLawSequence[oldPosition]
        scoutLawSequence.removeAt(oldPosition)
        scoutLawSequence.add(newPosition, law)
        notifyItemMoved(oldPosition, newPosition)
    }

    override fun getItemCount(): Int {
        return scoutLawSequence.size + 1    // Number of laws + footer
    }

    override fun getItemViewType(position: Int): Int {
        log(DEBUG, LOG_TAG, "getItemViewType(position = $position)")
        if (position == scoutLawSequence.size)  // Last item is footer
            return FOOTER_VIEW
        return super.getItemViewType(position)
    }

    inner class ItemViewHolder(val itemBinding: ViewHolderSorterItemBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemBinding.root)

    inner class FooterViewHolder(itemView: View) : ViewHolder(itemView)
}
