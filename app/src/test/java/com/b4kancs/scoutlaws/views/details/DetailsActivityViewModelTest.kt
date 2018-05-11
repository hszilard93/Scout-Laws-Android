package com.b4kancs.scoutlaws.views.details

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.data.Repository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

/**
 * Created by hszilard on 10-May-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DetailsActivityViewModelTest {

    @Inject
    lateinit var repository: Repository

    @BeforeAll
    fun setUp() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule((TestModule())).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent
    }

    @Test
    fun givenIndexViewModelShouldLoadCorrespondingScoutLawAfterInitialization() {
        val viewModel = DetailsActivityViewModel(3)

        assertEquals(repository.laws[2], viewModel.scoutLaw())
    }

    @Test
    fun observableStateShouldBeModernAfterInitialization() {
        val viewModel = DetailsActivityViewModel(1)

        assertEquals(DetailsActivityViewModel.State.MODERN, viewModel.observableState().get())
    }

    @Test
    fun observableStateShouldBeOldAfterSet() {
        val viewModel = DetailsActivityViewModel(1)

        viewModel.setState(DetailsActivityViewModel.State.OLD)

        assertEquals(DetailsActivityViewModel.State.OLD, viewModel.observableState().get())
    }
}