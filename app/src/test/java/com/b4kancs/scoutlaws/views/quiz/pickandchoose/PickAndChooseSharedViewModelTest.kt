package com.b4kancs.scoutlaws.views.quiz.pickandchoose

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.data.Repository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

/**
 * Created by hszilard on 09-May-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PickAndChooseSharedViewModelTest {

    @Inject lateinit var repository: Repository

    @BeforeAll
    fun setUpAll() {
        val testComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent
    }

    @Test
    fun sharedViewModelShouldLoadPickChooseScoutLawsFromRepository() {
        val viewModel = PickAndChooseSharedViewModel()

        assertEquals(repository.pickAndChooseLaws, viewModel.pickChooseScoutLaws)
    }
}