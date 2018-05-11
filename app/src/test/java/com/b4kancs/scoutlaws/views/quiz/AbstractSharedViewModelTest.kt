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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractSharedViewModelTest {

    private lateinit var svm: TestSharedViewModel

    @BeforeAll
    fun setUpAll() {
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule((TestModule())).build()
        ScoutLawApp().applicationComponent = testComponent
    }

    @BeforeEach
    fun setUpEach() {
        svm = TestSharedViewModel()
    }

    @Test
    fun svmShouldHaveCorrectValuesWhenInitialized() {

        assertEquals(false, svm.isLastTurn.get())
        assertEquals(0, svm.turnCount)
        assertEquals(0, svm.score)
        assertTrue(svm.usedLaws.isEmpty())
        assertNotNull(svm.repository)
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
        // Since the Repository instance has five laws, 3 will be the only possible answer
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
    fun resetSvmShouldEqualUnmodifiedSvm() {
        val unmodifiedSvm = TestSharedViewModel()

        svm.nextLawIndex()
        svm.incTurnCount()
        svm.incScore()
        svm.isLastTurn.set(true)
        svm.reset()

        assertEquals(unmodifiedSvm, svm)
    }
}