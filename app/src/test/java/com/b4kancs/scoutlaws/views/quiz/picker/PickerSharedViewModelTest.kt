package com.b4kancs.scoutlaws.views.quiz.picker

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.App
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
class PickerSharedViewModelTest {

    @Inject lateinit var repository: Repository

    @BeforeAll
    fun setUpAll() {
        val testComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        testComponent.inject(this)
        App().appComponent = testComponent
    }

    @Test
    fun sharedViewModelShouldLoadPickChooseScoutLawsFromRepository() {
        val viewModel = PickerSharedViewModel()

        assertEquals(repository.pickerScoutLaws, viewModel.pickerScoutLaws)
    }
}
