package com.b4kancs.scoutlaws.views.quiz

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    private lateinit var binding: FragmentQuizShellBinding
    private lateinit var fragmentTag: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_shell, container, false)
        var sharedViewModel: AbstractSharedViewModel

        if (savedInstanceState != null) {
            fragmentTag = savedInstanceState.getString("TAG")
            sharedViewModel =
                    when (fragmentTag) {
                        MultipleChoiceFragment.FRAGMENT_TAG ->
                            ViewModelProviders.of(activity!!).get(MultipleChoiceSharedViewModel::class.java)
                        PickAndChooseFragment.FRAGMENT_TAG ->
                            ViewModelProviders.of(activity!!).get(PickAndChooseSharedViewModel::class.java)
                        else ->
                            throw IllegalArgumentException()
                    }
        } else {
            fragmentTag = arguments!!.getString("TAG", MultipleChoiceFragment.FRAGMENT_TAG)
            when (fragmentTag) {
                MultipleChoiceFragment.FRAGMENT_TAG -> {
                    childFragmentManager
                            .beginTransaction()
                            .add(R.id.fragment_container, MultipleChoiceFragment(), fragmentTag)
                            .commit()
                    sharedViewModel = ViewModelProviders.of(activity!!).get(MultipleChoiceSharedViewModel::class.java)
                }
                PickAndChooseFragment.FRAGMENT_TAG -> {
                    childFragmentManager
                            .beginTransaction()
                            .add(R.id.fragment_container, PickAndChooseFragment(), fragmentTag)
                            .commit()
                    sharedViewModel = ViewModelProviders.of(activity!!).get(MultipleChoiceSharedViewModel::class.java)
                }
                else -> throw IllegalArgumentException()
            }
        }
        binding.sharedViewModel = sharedViewModel

        setUpViews()

        return binding.root
    }

    private fun setUpViews() {
        binding.textInstruction.apply {
            when (fragmentTag) {
                MultipleChoiceFragment.FRAGMENT_TAG ->
                    text = resources.getText(R.string.multiple_tip)
                PickAndChooseFragment.FRAGMENT_TAG ->
                    text = resources.getText(R.string.pick_choose_tip)
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