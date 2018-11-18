package com.b4kancs.scoutlaws.views.quiz

import android.os.Bundle
import android.util.Log.DEBUG
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.databinding.FragmentQuizShellBinding
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceSharedViewModel
import com.b4kancs.scoutlaws.views.quiz.picker.PickerFragment
import com.b4kancs.scoutlaws.views.quiz.picker.PickerSharedViewModel
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterFragment
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterSharedViewModel
import com.crashlytics.android.Crashlytics.log

/**
 * Created by hszilard on 20-May-18.
 */
class QuizShellFragment : androidx.fragment.app.Fragment() {
    companion object {
        const val FRAGMENT_TAG = "QUIZ_SHELL_FRAGMENT"
        private val LOG_TAG = QuizShellFragment::class.simpleName
    }

    private lateinit var binding: FragmentQuizShellBinding
    private lateinit var sharedViewModel: AbstractSharedViewModel
    private lateinit var fragmentTag: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log(INFO, LOG_TAG, "onCreateView(..)")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_shell, container, false)

        if (savedInstanceState != null)
            restoreNestedFragments(savedInstanceState)
        else
            createNewNestedFragments()

        setUpViews()

        return binding.root
    }

    private fun restoreNestedFragments(savedInstanceState: Bundle) {
        log(DEBUG, LOG_TAG, "restoreNestedFragments(..)")
        fragmentTag = savedInstanceState.getString("TAG")!!
        log(DEBUG, LOG_TAG, "Restoring fragment $fragmentTag.")
        sharedViewModel = when (fragmentTag) {
            MultipleChoiceFragment.FRAGMENT_TAG ->
                ViewModelProviders.of(activity!!).get(MultipleChoiceSharedViewModel::class.java)
            PickerFragment.FRAGMENT_TAG ->
                ViewModelProviders.of(activity!!).get(PickerSharedViewModel::class.java)
            SorterFragment.FRAGMENT_TAG ->
                ViewModelProviders.of(activity!!).get(SorterSharedViewModel::class.java)
            else ->
                throw IllegalArgumentException()
        }
    }

    private fun createNewNestedFragments() {
        log(DEBUG, LOG_TAG, "createNestedFragments()")
        fragmentTag = arguments!!.getString("TAG")!!
        log(DEBUG, LOG_TAG, "Creating fragment $fragmentTag.")
        val fragment: androidx.fragment.app.Fragment
        when (fragmentTag) {
            MultipleChoiceFragment.FRAGMENT_TAG -> {
                fragment = MultipleChoiceFragment()
                sharedViewModel = ViewModelProviders.of(activity!!).get(MultipleChoiceSharedViewModel::class.java)
            }
            PickerFragment.FRAGMENT_TAG -> {
                fragment = PickerFragment()
                sharedViewModel = ViewModelProviders.of(activity!!).get(PickerSharedViewModel::class.java)
            }
            SorterFragment.FRAGMENT_TAG -> {
                fragment = SorterFragment()
                sharedViewModel = ViewModelProviders.of(activity!!).get(SorterSharedViewModel::class.java)
            }
            else -> throw IllegalArgumentException()
        }
        sharedViewModel.start()
        childFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment, fragmentTag)
                .commit()
    }

    private fun setUpViews() {
        log(DEBUG, LOG_TAG, "setUpViews()")
        binding.sharedViewModel = sharedViewModel
        binding.apply {
            when (fragmentTag) {
                MultipleChoiceFragment.FRAGMENT_TAG ->
                    textInstruction.text = resources.getText(R.string.multiple_tip)
                PickerFragment.FRAGMENT_TAG ->
                    textInstruction.text = resources.getText(R.string.picker_tip)
                SorterFragment.FRAGMENT_TAG ->
                    textInstruction.text = resources.getText(R.string.sorter_tip)
            }
        }
    }

    // This is a workaround for the bug where child fragments disappear when
    // the parent is removed (as all children are first removed from the parent)
    // See https://code.google.com/p/android/issues/detail?id=55228
    fun triggerChildExitAnimation() {
        childFragmentManager.apply {
            beginTransaction()
                    .setCustomAnimations(R.anim.none, R.anim.none)
                    .show(fragments.last())
                    .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("TAG", fragmentTag)
    }
}

@BindingAdapter("chrono_start")
fun chronometerStartBindingAdapter(chrono: Chronometer, toStart: Boolean) {
    when (toStart) {
        true -> chrono.start()
        false -> chrono.stop()
    }
}

@BindingAdapter("chrono_base")
fun chronometerStopBindingAdapter(chrono: Chronometer, base: Long) {
    chrono.base = base
}