package com.b4kancs.scoutlaws.views.quiz.sorter

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.App
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.data.Repository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

/**
 * Created by hszilard on 31-Oct-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SorterSharedViewModelTest {

    @Inject lateinit var repository: Repository

    @BeforeAll
    fun setUpAll() {
        val testComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        testComponent.inject(this)
        App().appComponent = testComponent
    }

    @Test
    fun nextLawIndexShouldNotGiveSameIndexTwice() {
        val sharedViewModel = SorterSharedViewModel()
        val usedIndexes = ArrayList<Int>()
        val times = repository.scoutLaws.size - SorterSharedViewModel.NUMBER_OF_OPTIONS

        for (i in 0..times)
            usedIndexes.add(sharedViewModel.nextLawIndex())

        assertTrue(usedIndexes == usedIndexes.distinct())
    }
}
