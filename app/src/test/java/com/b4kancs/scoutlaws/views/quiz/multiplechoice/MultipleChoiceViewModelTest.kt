package com.b4kancs.scoutlaws.views.quiz.multiplechoice

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.*

/**
 * Created by hszilard on 09-May-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MultipleChoiceViewModelTest {

    private lateinit var sharedViewModel: MultipleChoiceSharedViewModel

    @BeforeAll
    fun setUpAll() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule((TestModule())).build()
        ScoutLawApp().applicationComponent = testComponent
    }

    @BeforeEach
    fun setUpEach() {
        sharedViewModel = MultipleChoiceSharedViewModel()
    }

    @Test
    fun viewModelShouldCallIncTurnOnSharedViewModelWhenInstantiated() {
        val spySharedViewModel = spy(sharedViewModel)
        MultipleChoiceViewModel(spySharedViewModel)

        verify(spySharedViewModel).incTurnCount()
    }

    @Test
    fun observableStateShouldBeInProgressWhenInstantiated() {
        val viewModel = MultipleChoiceViewModel(sharedViewModel)

        assertEquals(MultipleChoiceViewModel.State.IN_PROGRESS, viewModel.observableState.get())
    }

    @Test
    fun optionsShouldHaveDefinedNumberOfScoutLaws() {
        val viewModel = MultipleChoiceViewModel(sharedViewModel)

        assertEquals(MultipleChoiceViewModel.NUMBER_OF_OPTIONS, viewModel.options.size)
    }

    @Test
    fun optionsShouldContainAnswer() {
        val viewModel = MultipleChoiceViewModel(sharedViewModel)

        assertTrue(viewModel.options.contains(viewModel.answer))
    }

    @Test
    fun evaluateAnswerShouldReturnFalseWhenIncorrectAnswerSupplied() {
        val viewModel = MultipleChoiceViewModel(sharedViewModel)
        // Remove correct answer from options
        viewModel.options.remove(viewModel.answer)
        val incorrectAnswer = viewModel.options[0]

        assertFalse(viewModel.evaluateAnswer(incorrectAnswer))
    }

    @Test
    fun evaluateAnswerShouldReturnTrueWhenCorrectAnswerSupplied() {
        val viewModel = MultipleChoiceViewModel(sharedViewModel)

        assertTrue(viewModel.evaluateAnswer(viewModel.answer))
    }

    @Test
    fun observableStateShouldBeDoneWhenCorrectAnswerSupplied() {
        val viewModel = MultipleChoiceViewModel(sharedViewModel)

        viewModel.evaluateAnswer(viewModel.answer)

        assertEquals(MultipleChoiceViewModel.State.DONE, viewModel.observableState.get())
    }

    @Test
    fun observableStateShouldBeDoneWhenOutOfOptions() {
        val viewModel = MultipleChoiceViewModel(sharedViewModel)
        // Remove correct answer from options, so we don't supply it
        viewModel.options.remove(viewModel.answer)

        for (incorrectAnswer in viewModel.options)
            viewModel.evaluateAnswer(incorrectAnswer)

        assertEquals(MultipleChoiceViewModel.State.DONE, viewModel.observableState.get())
    }

    @Test
    fun incScoreShouldBeCalledWhenCorrectAnswerSuppliedAtFirstTry() {
        val spySharedViewModel = spy(sharedViewModel)
        val viewModel = MultipleChoiceViewModel(spySharedViewModel)

        viewModel.evaluateAnswer(viewModel.answer)

        verify(spySharedViewModel).incScore()
    }

    @Test
    fun incScoreShouldNotBeCalledWhenCorrectAnswerSuppliedNotAtFirstTry() {
        val spySharedViewModel = spy(sharedViewModel)
        val viewModel = MultipleChoiceViewModel(spySharedViewModel)
        val options = viewModel.options
        val correctAnswer = viewModel.answer
        val incorrectAnswer = if (options[0] != correctAnswer) options[0] else options[1]

        viewModel.evaluateAnswer(incorrectAnswer)
        viewModel.evaluateAnswer(correctAnswer)

        verify(spySharedViewModel, times(0)).incScore()
    }
}