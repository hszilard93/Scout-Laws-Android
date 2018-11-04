package com.b4kancs.scoutlaws.views.quiz.sorter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import com.b4kancs.scoutlaws.databinding.ViewHolderSorterItemBinding

/**
 * Created by hszilard on 15-Oct-18.
 */
class OptionsRecyclerAdapter(private val scoutLawSequence: ArrayList<ScoutLaw>)
    : RecyclerView.Adapter<ViewHolder>(), DragHelperCallback.DragHandler {

    companion object {
        const val FOOTER_VIEW = 1000
        private val LOG_TAG = OptionsRecyclerAdapter::class.simpleName
    }

    lateinit var touchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeAndPosition: Int): ViewHolder {
        Log.d(LOG_TAG, "onCreateViewHolder viewTypeAndPosition: $viewTypeAndPosition")
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
                    if (e.action == MotionEvent.ACTION_DOWN)
                        touchHelper.startDrag(it)
                    true
                }
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(LOG_TAG, "onBindViewHolder position: $position")
        if (viewHolder is ItemViewHolder) {
            viewHolder.itemBinding.scoutLaw = scoutLawSequence[position]
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        Log.d(LOG_TAG, "onViewMoved oldPosition: $oldPosition newPosition: $newPosition")
        val law = scoutLawSequence[oldPosition]
        scoutLawSequence.removeAt(oldPosition)
        scoutLawSequence.add(newPosition, law)
        notifyItemMoved(oldPosition, newPosition)
    }

    override fun getItemCount(): Int {
        return scoutLawSequence.size + 1    // Number of laws + footer
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(LOG_TAG, "getItemViewType position: $position")
        if (position == scoutLawSequence.size)  // Last item is footer
            return FOOTER_VIEW
        return super.getItemViewType(position)
    }

    inner class ItemViewHolder(val itemBinding: ViewHolderSorterItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    inner class FooterViewHolder(itemView: View) : ViewHolder(itemView)
}
