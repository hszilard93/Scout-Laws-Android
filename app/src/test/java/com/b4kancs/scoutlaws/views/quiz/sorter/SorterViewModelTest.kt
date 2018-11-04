package com.b4kancs.scoutlaws.views.quiz.sorter

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.data.Repository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

/**
 * Created by hszilard on 31-Oct-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SorterViewModelTest {

    private lateinit var mockSharedViewModel: SorterSharedViewModel
    @Inject lateinit var repository: Repository

    @BeforeAll
    fun setUpAll() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent
    }

    @BeforeEach
    fun setUpEach() {
        mockSharedViewModel = mock {
            on { nextLawIndex() }.doReturn(1)
            on { scoutLaws }.doReturn(repository.scoutLaws)
        }
    }


    @Test
    fun viewModelShouldCallIncTurnOnSharedViewModelWhenInstantiated() {
        SorterViewModel(mockSharedViewModel)

        verify(mockSharedViewModel).incTurnCount()
    }

    @Test
    fun evaluateShouldReturnFalseWhenSequenceIsInWrongOrderAndShouldNotChangeState() {
        val viewModel = SorterViewModel(mockSharedViewModel)

        viewModel.sequence.clear()
        viewModel.sequence.addAll(listOf(repository.scoutLaws[0], repository.scoutLaws[2], repository.scoutLaws[1]))

        assertFalse(viewModel.evaluate())
        assertEquals(SorterViewModel.State.CHECKABLE, viewModel.observableState.get())
    }

    @Test
    fun evaluateShouldReturnTrueWhenSequenceIsInCorrectOrderAndShouldChangeState() {
        val viewModel = SorterViewModel(mockSharedViewModel)

        viewModel.sequence.clear()
        viewModel.sequence.addAll(listOf(repository.scoutLaws[0], repository.scoutLaws[1], repository.scoutLaws[2]))

        assertTrue(viewModel.evaluate())
        assertEquals(SorterViewModel.State.DONE, viewModel.observableState.get())
    }
}