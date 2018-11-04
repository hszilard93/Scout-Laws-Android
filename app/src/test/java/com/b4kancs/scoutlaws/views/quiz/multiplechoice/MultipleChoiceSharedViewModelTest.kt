package com.b4kancs.scoutlaws.views.quiz.multiplechoice

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.data.Repository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

/**
 * Created by hszilard on 09-May-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MultipleChoiceSharedViewModelTest {

    @Inject
    lateinit var repository: Repository

    @BeforeAll
    fun setUpAll() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent
    }

    @Test
    fun sharedViewModelShouldLoadScoutLawsFromRepository() {
        val viewModel = MultipleChoiceSharedViewModel()

        Assertions.assertEquals(repository.scoutLaws, viewModel.scoutLaws)
    }
}