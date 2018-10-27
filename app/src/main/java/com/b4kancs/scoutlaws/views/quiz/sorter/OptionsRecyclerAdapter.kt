package com.b4kancs.scoutlaws.views.quiz.sorter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import com.b4kancs.scoutlaws.databinding.ViewHolderSorterItemBinding

/**
 * Created by hszilard on 15-Oct-18.
 */
class OptionsRecyclerAdapter(private val scoutLawSequence: ArrayList<ScoutLaw>)
    : RecyclerView.Adapter<OptionsRecyclerAdapter.ItemViewHolder>(), DragHelperCallback.DragHandler {

    lateinit var touchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderSorterItemBinding.inflate(layoutInflater, parent, false)
        binding.scoutLaw = scoutLawSequence[position]
        return ItemViewHolder(binding).also {
            it.itemView.setOnTouchListener { _, _ ->
                touchHelper.startDrag(it)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return scoutLawSequence.size
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        viewHolder.itemBinding.scoutLaw = scoutLawSequence[position]
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        val law = scoutLawSequence[oldPosition]
        scoutLawSequence.removeAt(oldPosition)
        scoutLawSequence.add(newPosition, law)
        notifyItemMoved(oldPosition, newPosition)
    }

    inner class ItemViewHolder(val itemBinding: ViewHolderSorterItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}
