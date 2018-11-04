package com.b4kancs.scoutlaws.views.quiz

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Created by hszilard on 08-May-18.
 */

class TestSharedViewModel : AbstractSharedViewModel()   // Test implementation of AbstractSharedViewModel
{
    var saveNewBestTimeCalled = false

    override fun getBestTime(): Long {
        return 0L
    }

    override fun saveNewBestTime() {
        saveNewBestTimeCalled = true
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractSharedViewModelTest {

    private lateinit var svm: TestSharedViewModel

    @BeforeAll
    fun setUpAll() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        ScoutLawApp().applicationComponent = testComponent
    }

    @BeforeEach
    fun setUpEach() {
        svm = TestSharedViewModel()
    }

    @Test
    fun svmShouldHaveCorrectValuesWhenInitialized() {
        svm.apply {
            assertEquals(false, isLastTurn.get())
            assertEquals(0, turnCount.get())
            assertEquals(0, score)
            assertTrue(usedLaws.isEmpty())
            assertTrue(chronoStart.get())
            assertNotNull(repository)
        }
    }

    @Test
    fun isLastTurnShouldBeFalseAfterFourTurns() {
        for (i in 1..4)
            svm.incTurnCount()

        assertFalse(svm.isLastTurn.get())
    }

    @Test
    fun isLastTurnShouldBeTrueAfterFiveTurns() {
        for (i in 1..5)
            svm.incTurnCount()

        assertTrue(svm.isLastTurn.get())
    }

    @Test
    fun scoreShouldBeZeroAfterFiveTurnsWhenNotIncremented() {
        for (i in 1..5)
            svm.incTurnCount()

        assertEquals(0, svm.score)
    }

    @Test
    fun scoreShouldEqualTimesIncremented() {
        for (i in 1..3)
            svm.incScore()

        assertEquals(3, svm.score)
    }

    @Test
    fun nextLawShouldNotReturnUsedValue() {
        svm.usedLaws.clear()
        // Since the Repository instance has five scoutLaws, 3 will be the only possible answer
        svm.usedLaws.addAll(arrayOf(0, 1, 2, 4))
        // lastUsedLawIndex could possibly be 4, and that would cause an infinite loop in nextLawIndex()
        AbstractSharedViewModel.lastUsedLawIndex = -1

        val nextIndex = svm.nextLawIndex()

        assertEquals(3, nextIndex)
    }

    @Test
    fun lastUsedLawIndexShouldEqualLastNextLaw() {
        val index = svm.nextLawIndex()

        assertEquals(index, AbstractSharedViewModel.lastUsedLawIndex)
    }

    @Test
    fun chronoStartShouldBeFalseAfterFinishCalled() {
        svm.finish()

        assertFalse(svm.chronoStart.get())
    }

    @Test
    fun saveNewBestTimeShouldBeCalledDuringFinishWhenPerfectScore() {
        for (i in 1..5)
            svm.incScore()
        svm.baseTime.set(0)

        svm.finish()

        assertTrue(svm.saveNewBestTimeCalled)
    }
}