package com.b4kancs.scoutlaws.views.quiz

import android.arch.lifecycle.ViewModelProviders
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.databinding.FragmentQuizShellBinding
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceSharedViewModel
import com.b4kancs.scoutlaws.views.quiz.pickandchoose.PickAndChooseFragment
import com.b4kancs.scoutlaws.views.quiz.pickandchoose.PickAndChooseSharedViewModel

/**
 * Created by hszilard on 20-May-18.
 */
class QuizShellFragment : Fragment() {
    companion object {
        const val FRAGMENT_TAG = "QUIZ_SHELL_FRAGMENT"
        private val LOG_TAG = QuizShellFragment::class.simpleName
    }

    private lateinit var binding: FragmentQuizShellBinding
    private lateinit var sharedViewModel: AbstractSharedViewModel
    private lateinit var fragmentTag: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(LOG_TAG, "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_shell, container, false)

        if (savedInstanceState != null)
            restoreNestedFragments(savedInstanceState)
        else
            createNewNestedFragments()

        setUpViews()

        return binding.root
    }

    private fun restoreNestedFragments(savedInstanceState: Bundle) {
        fragmentTag = savedInstanceState.getString("TAG")
        Log.d(LOG_TAG, "Restoring fragment $fragmentTag.")
        sharedViewModel = when (fragmentTag) {
            MultipleChoiceFragment.FRAGMENT_TAG ->
                ViewModelProviders.of(activity!!).get(MultipleChoiceSharedViewModel::class.java)
            PickAndChooseFragment.FRAGMENT_TAG ->
                ViewModelProviders.of(activity!!).get(PickAndChooseSharedViewModel::class.java)
            else ->
                throw IllegalArgumentException()
        }
    }

    private fun createNewNestedFragments() {
        fragmentTag = arguments!!.getString("TAG")
        Log.d(LOG_TAG, "Creating fragment $fragmentTag.")
        val fragment: Fragment
        when (fragmentTag) {
            MultipleChoiceFragment.FRAGMENT_TAG -> {
                fragment = MultipleChoiceFragment()
                sharedViewModel = ViewModelProviders.of(activity!!).get(MultipleChoiceSharedViewModel::class.java)
            }
            PickAndChooseFragment.FRAGMENT_TAG -> {
                fragment = PickAndChooseFragment()
                sharedViewModel = ViewModelProviders.of(activity!!).get(PickAndChooseSharedViewModel::class.java)
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
        binding.sharedViewModel = sharedViewModel
        binding.apply {
            when (fragmentTag) {
                MultipleChoiceFragment.FRAGMENT_TAG ->
                    textInstruction.text = resources.getText(R.string.multiple_tip)
                PickAndChooseFragment.FRAGMENT_TAG ->
                    textInstruction.text = resources.getText(R.string.pick_choose_tip)
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