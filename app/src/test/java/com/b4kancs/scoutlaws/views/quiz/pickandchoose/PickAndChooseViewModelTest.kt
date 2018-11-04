package com.b4kancs.scoutlaws.views.quiz.pickandchoose

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Created by hszilard on 10-May-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PickAndChooseViewModelTest {
    private val stubSharedViewModel: PickAndChooseSharedViewModel = mock()
    private lateinit var viewModel: PickAndChooseViewModel

    private val testScoutLaw = PickAndChooseScoutLaw(
            ScoutLaw(1, "", "", ""),
            "Something #option1 something something #option2",
            listOf("option3", "option4", "option5")
    )

    @BeforeAll
    fun setUpAll() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        ScoutLawApp().applicationComponent = testComponent

        // If we have only one scout law in the list, it is guaranteed to be chosen for the first turn
        whenever(stubSharedViewModel.pickChooseScoutLaws).thenReturn(listOf(testScoutLaw))
        whenever(stubSharedViewModel.isThisTheLastTurn).thenReturn(false)
    }

    @BeforeEach
    fun setUpEach() {
        viewModel = PickAndChooseViewModel(stubSharedViewModel)
    }

    @Test
    fun viewModelShouldCallIncTurnOnSharedViewModelWhenInstantiated() {
        clearInvocations(stubSharedViewModel)

        val newViewModel = PickAndChooseViewModel(stubSharedViewModel)

        verify(stubSharedViewModel).incTurnCount()
    }

    @Test
    fun correctAnswerShouldContainPredefinedStringsAfterInitialization() {
        assertTrue(viewModel.correctAnswers.containsAll(listOf("option1", "option2")))
    }

    @Test
    fun optionsShouldContainPredefinedStringsAfterInitialization() {
        assertTrue(viewModel.options.containsAll(listOf("option1", "option2", "option3", "option4", "option5")))
    }

    @Test
    fun userAnswersShouldBeEmptyAfterInitialization() {
        assertTrue(viewModel.userAnswers.isEmpty())
    }

    @Test
    fun observableStateShouldBeInProgressAfterInitialization() {
        assertEquals(PickAndChooseViewModel.State.IN_PROGRESS, viewModel.observableState.get())
    }

    @Test
    fun addUserAnswerShouldAddAnswerToUserAnswers() {
        viewModel.addUserAnswer(1, "option1")

        assertTrue(viewModel.userAnswers[1]?.get() == "option1")
    }

    @Test
    fun addUserAnswerShouldRemoveAnswerFromOptions() {
        viewModel.addUserAnswer(1, "option1")

        assertFalse(viewModel.options.contains("option1"))
    }

    @Test
    fun observableStateShouldBeCheckableAfterAllAnswersAdded() {
        viewModel.apply {
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option3")

            assertEquals(PickAndChooseViewModel.State.CHECKABLE, observableState.get())
        }
    }

    @Test
    fun clearShouldNotRemoveItemsFromUserAnswers() {
        viewModel.apply {
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option2")
            addUserAnswer(3, "option3")

            clear()

            assertTrue(userAnswers.keys.containsAll(listOf(1, 2, 3)))
        }
    }

    @Test
    fun clearShouldReplaceAllItemsInUserAnswersWithNullObjects() {
        viewModel.apply {
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option2")
            addUserAnswer(3, "option3")

            clear()

            assertTrue(
                    fun(): Boolean {
                        for (item in userAnswers.values)
                            if (item.get() != null)
                                return false
                        return true
                    }
            )
        }
    }

    @Test
    fun clearShouldRestoreItemsToOptions() {
        viewModel.apply {
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option2")
            addUserAnswer(3, "option3")

            clear()

            assertTrue(options.containsAll(listOf("option1", "option2", "option3")))
        }
    }

    @Test
    fun observableStateShouldBeInProgressAfterClearCalled() {
        viewModel.apply {
            observableState.set(PickAndChooseViewModel.State.CHECKABLE)

            clear()

            assertEquals(PickAndChooseViewModel.State.IN_PROGRESS, observableState.get())
        }
    }

    @Test
    fun addOptionShouldAddNewOptionToOptions() {
        viewModel.apply {
            addOption("option6")
            addOption("option6")

            assertEquals(1, options.count { it == "option6" })

        }
    }

    @Test
    fun helpShouldEliminateCorrectNumberOfAnswers() {
        viewModel.apply {
            val optionsSizeBefore = options.size

            help()

            assertEquals(1, optionsSizeBefore - options.size)
        }
    }

    @Test
    fun helpShouldNotEliminateCorrectAnswers() {
        viewModel.apply {
            help()
            help()
            help()

            assertTrue(options.containsAll(listOf("option1", "option2")))
        }
    }

    @Test
    fun helpUsedUpShouldBeFalseAfterInitialization() {
        assertFalse(viewModel.helpUsedUp.get())
    }

    @Test
    fun helpUsedUpShouldBeTrueWhenHelpUsedTooMuch() {
        viewModel.apply {
            help()
            help()

            assertTrue(helpUsedUp.get())
        }
    }

    @Test
    fun evaluateUserAnswersShouldReturnFalseWhenUserAnswersSizeDoesNotEqualCorrectAnswersSize() {
        // userAnswers is empty after initialization
        assertFalse(viewModel.evaluateUserAnswers())
    }

    @Test
    fun evaluateShouldReturnFalseWhenUserAnswersAreNotCorrect() {
        viewModel.apply {
            addUserAnswer(1, "option3")
            addUserAnswer(2, "option4")

            assertFalse(evaluateUserAnswers())
        }
    }

    @Test
    fun evaluateShouldReturnTrueWhenUserAnswersAreCorrect() {
        viewModel.apply {
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option2")

            assertTrue(evaluateUserAnswers())
        }
    }

    @Test
    fun incScoreShouldNotBeenCalledWhenEvaluateSucceedsNotAtFirstTime() {
        viewModel.apply {
            clearInvocations(stubSharedViewModel)

            // Supply incorrect answer
            addUserAnswer(1, "option3")
            addUserAnswer(2, "option4")
            evaluateUserAnswers()
            // Supply correct answer
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option2")
            evaluateUserAnswers()

            verify(stubSharedViewModel, times(0)).incScore()
        }
    }

    @Test
    fun incScoreShouldBeenCalledWhenEvaluateSucceedsAtFirstTime() {
        viewModel.apply {
            clearInvocations(stubSharedViewModel)

            addUserAnswer(1, "option1")
            addUserAnswer(2, "option2")
            evaluateUserAnswers()

            verify(stubSharedViewModel).incScore()
        }
    }

    @Test
    fun givenOnlyIncorrectAnswersAllUserOptionsShouldBeNullWhenEvaluateCalled() {
        viewModel.apply {
            addUserAnswer(1, "option3")
            addUserAnswer(2, "option4")

            evaluateUserAnswers()

            assertTrue(
                    fun(): Boolean {
                        for (item in userAnswers)
                            if (item.value.get() != null)
                                return false
                        return true
                    }
            )
        }
    }

    @Test
    fun partiallyCorrectUserOptionsShouldBePreservedWhenEvaluateCalled() {
        viewModel.apply {
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option3")

            evaluateUserAnswers()

            assertTrue(userAnswers[1]?.get() == "option1" && userAnswers[2]?.get() == null)
        }
    }

    @Test
    fun givenWrongAnswersObservableStateShouldBeInProgressAfterEvaluateCalled() {
        viewModel.apply {
            addUserAnswer(1, "option3")
            addUserAnswer(2, "option4")
            observableState.set(PickAndChooseViewModel.State.CHECKABLE)

            evaluateUserAnswers()

            assertEquals(PickAndChooseViewModel.State.IN_PROGRESS, observableState.get())
        }
    }

    @Test
    fun givenCorrectAnswersObservableStateShouldBeDoneAfterEvaluateCalled() {
        viewModel.apply {
            addUserAnswer(1, "option1")
            addUserAnswer(2, "option2")

            evaluateUserAnswers()

            assertEquals(PickAndChooseViewModel.State.DONE, observableState.get())
        }
    }

    @Test
    fun givenNullUserAnswersGiveUpShouldFillInCorrectAnswers() {
        viewModel.apply {
            // In the real use case, userAnswers is always set up with indexes and null values by the view
            addUserAnswer(1, null)
            addUserAnswer(2, null)

            giveUp()

            assertTrue(userAnswers[1]?.get() == "option1")
            assertTrue(userAnswers[2]?.get() == "option2")
        }
    }

    @Test
    fun observableStateShouldBeDoneAfterGiveUpCalled() {
        viewModel.apply {
            // Give up would fail without existing answers
            addUserAnswer(1, null)
            addUserAnswer(2, null)

            giveUp()

            assertEquals(PickAndChooseViewModel.State.DONE, observableState.get())
        }
    }
}