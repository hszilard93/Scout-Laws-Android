package com.b4kancs.scoutlaws.views.quiz.sorter

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.databinding.FragmentSorterBinding
import com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.*
import com.b4kancs.scoutlaws.views.utils.vibrate

/**
 * Created by hszilard on 01-Sep-18.
 */
class SorterFragment : Fragment() {

    companion object {
        const val FRAGMENT_TAG = "SORTER_FRAGMENT"
        private val LOG_TAG = SorterFragment::class.simpleName
    }

    private lateinit var sharedViewModel: SorterSharedViewModel
    private lateinit var viewModel: SorterViewModel
    private lateinit var binding: FragmentSorterBinding
    private lateinit var container: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        this.container = container!!
        sharedViewModel = ViewModelProviders.of(activity!!).get(SorterSharedViewModel::class.java)
        val factory = SorterViewModelFactory(sharedViewModel)
        viewModel = ViewModelProviders.of(this, factory).get(SorterViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sorter, container, false)
        binding.viewModel = viewModel
        binding.sharedViewModel = sharedViewModel

        setUpViews()

        return binding.root
    }

    private fun setUpViews() {
        binding.recyclerOptions.apply {
            setHasFixedSize(true)
            val optionsRecyclerAdapter = OptionsRecyclerAdapter(viewModel.sequence)
            val dragCallback = DragHelperCallback(optionsRecyclerAdapter)
            val touchHelper = ItemTouchHelper(dragCallback)
            touchHelper.attachToRecyclerView(this)
            optionsRecyclerAdapter.touchHelper = touchHelper
            adapter = optionsRecyclerAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
        binding.buttonCheck.setOnClickListener(onButtonCheckClick)
        binding.buttonNext.setOnClickListener {
            Log.d(LOG_TAG, "Forward button clicked.")
            transitionToNextQuestion()
        }
        binding.buttonFinish.setOnClickListener{
            showResultDialogFragment(container, activity, fragmentManager, SorterFragment(), sharedViewModel)
        }
    }

    private val onButtonCheckClick = { e: Event ->
        val result = viewModel.evaluate()
        if (result)
            showCorrectFeedback(context, layoutInflater)
        else {
            showIncorrectFeedback(context, layoutInflater)
            vibrate(context!!, 300)
        }
    }

    private fun transitionToNextQuestion() {
        binding.unbind()
        val transaction = getFragmentTransaction(container, fragmentManager!!, SorterFragment())
        transaction.commit()
    }
}