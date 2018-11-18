@file:Suppress("RedundantLambdaArrow")

package com.b4kancs.scoutlaws.views.quiz.sorter

import android.os.Bundle
import android.util.Log.DEBUG
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.databinding.FragmentSorterBinding
import com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.*
import com.b4kancs.scoutlaws.views.utils.vibrate
import com.crashlytics.android.Crashlytics.log

/**
 * Created by hszilard on 01-Sep-18.
 */
class SorterFragment : androidx.fragment.app.Fragment() {

    companion object {
        const val FRAGMENT_TAG = "SORTER_FRAGMENT"
        private val LOG_TAG = SorterFragment::class.simpleName
    }

    private lateinit var sharedViewModel: SorterSharedViewModel
    private lateinit var viewModel: SorterViewModel
    private lateinit var binding: FragmentSorterBinding
    private lateinit var container: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log(INFO, LOG_TAG, "onCreateView(..)")
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
        log(DEBUG, LOG_TAG, "setUpViews()")
        binding.recyclerOptions.apply {
            setHasFixedSize(true)
            val optionsRecyclerAdapter = OptionsRecyclerAdapter(viewModel.sequence)
            val dragCallback = DragHelperCallback(optionsRecyclerAdapter)
            val touchHelper = ItemTouchHelper(dragCallback)
            touchHelper.attachToRecyclerView(this)
            optionsRecyclerAdapter.touchHelper = touchHelper
            adapter = optionsRecyclerAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        }
        binding.buttonCheck.setOnClickListener(onCheckButtonClicked)
        binding.buttonNext.setOnClickListener(onNextButtonClicked)
        binding.buttonFinish.setOnClickListener(onFinishButtonClicked)
    }

    private val onCheckButtonClicked = { _: View ->
        log(INFO, LOG_TAG, "Check button clicked.")
        val result = viewModel.evaluate()
        if (result)
            showCorrectFeedback(context, layoutInflater)
        else {
            showIncorrectFeedback(context, layoutInflater)
            vibrate(context!!, 300)
        }
    }

    private val onNextButtonClicked = { _: View ->
        log(INFO, LOG_TAG, "Next button clicked.")
        transitionToNextQuestion()
    }

    private val onFinishButtonClicked = { _: View ->
        log(INFO, LOG_TAG, "Finish button clicked.")
        showResultDialogFragment(container, activity, fragmentManager, SorterFragment(), sharedViewModel)
    }

    private fun transitionToNextQuestion() {
        log(DEBUG, LOG_TAG, "transitionToNextQuestion()")
        binding.unbind()
        val transaction = getFragmentTransaction(container, fragmentManager!!, SorterFragment())
        transaction.commit()
    }
}