package com.b4kancs.scoutlaws.views.start

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.App
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.data.Repository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

/**
 * Created by hszilard on 08-May-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StartActivityViewModelTest {

    @Inject
    lateinit var repository: Repository

    @BeforeAll
    fun setUp() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        App().appComponent = testComponent
        testComponent.inject(this)
    }

    @Test
    fun viewModelShouldLoadScoutLawsFromRepository() {
        val viewModel = StartActivityViewModel()

        assertEquals(repository.scoutLaws, viewModel.scoutLaws())
    }
}
