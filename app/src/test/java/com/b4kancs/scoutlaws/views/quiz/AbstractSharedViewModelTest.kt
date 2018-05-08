package com.b4kancs.scoutlaws.views.quiz

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Created by hszilard on 08-May-18.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractSharedViewModelTest {

    @BeforeAll
    fun setUp() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule((TestModule())).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent
    }

    @Test
    fun svmShouldHaveCorrectValuesWhenInitialized() {
        val svm = TestSharedViewModel()

        assertEquals(false, svm.isLastTurn.get())
        assertEquals(-1, svm.lastAnswerIndex)
        assertEquals(0, svm.turnCount)
        assertEquals(0, svm.score)
        assertTrue(svm.usedLaws.isEmpty())
        assertNotNull(svm.repository)
    }

    @Test
    fun isLastTurnShouldBeFalseAfterFourTurns() {
        val svm = TestSharedViewModel()

        for (i in 1..4)
            svm.incTurnCount()

        assertFalse(svm.isLastTurn.get())
    }

    @Test
    fun isLastTurnShouldBeTrueAfterFiveTurns() {
        val svm = TestSharedViewModel()

        for (i in 1..5)
            svm.incTurnCount()

        assertTrue(svm.isLastTurn.get())
    }

    @Test
    fun scoreShouldBeZeroAfterFiveTurnsWhenNotIncremented() {
        val svm = TestSharedViewModel()

        for (i in 1..5)
            svm.incTurnCount()

        assertEquals(0, svm.score)
    }

    @Test
    fun scoreShouldEqualTimesIncremented() {
        val svm = TestSharedViewModel()

        for (i in 1..3)
            svm.incScore()

        assertEquals(3, svm.score)
    }

    @Test
    fun nextLawShouldNotReturnUsedValue() {
        val svm = TestSharedViewModel()

        svm.usedLaws.clear()
        svm.usedLaws.addAll(arrayOf(0,1,2,4))   // Since Repository will have five laws, 3 is the only possible answer
        val nextIndex = svm.nextLawIndex()

        assertEquals(3, nextIndex)
    }

    @Test
    fun resetSvmShouldProduceExpectedStateAfterModifications() {
        val svm = TestSharedViewModel()

        svm.nextLawIndex()
        svm.incTurnCount()
        svm.incScore()
        svm.isLastTurn.set(true)
        svm.reset()

        assertEquals(false, svm.isLastTurn.get())
        assertNotEquals(-1, svm.lastAnswerIndex)
        assertEquals(0, svm.turnCount)
        assertEquals(0, svm.score)
        assertTrue(svm.usedLaws.isEmpty())
    }
}

// Test implementation of AbstractSharedViewModel
class TestSharedViewModel : AbstractSharedViewModel()