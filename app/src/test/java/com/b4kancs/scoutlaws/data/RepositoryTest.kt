package com.b4kancs.scoutlaws.data

import android.content.SharedPreferences
import com.b4kancs.scoutlaws.*
import com.b4kancs.scoutlaws.TestModule.Companion.NUMBER_OF_LAWS
import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by hszilard on 02-May-18.
 */
class RepositoryTest {

    @Inject
    lateinit var injectedRepository: Repository
    @field: [Inject Named("default_preferences")]
    lateinit var mockSharedPreferences: SharedPreferences

    init {
        /* Set up Dagger for testing */
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule((TestModule())).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent
    }

    @Test
    fun numberOfScoutLawsShouldBeCorrect() {
        val stubResources = getStubResourcesWithTwoLaws()

        val repository = Repository(stubResources, StubUserDataStore(), mockSharedPreferences)

        assertEquals(2, repository.numberOfScoutLaws)
    }

    @Test
    fun repositoryShouldLoadScoutLawsInOrder() {
        val stubResources = getStubResourcesWithTwoLaws()
        val scoutLaw1 = ScoutLaw(1, sl1Text, sl1Desc, sl1DescOrig)
        val scoutLaw2 = ScoutLaw(2, sl2Text, sl2Desc, sl2DescOrig)
        val pickChooseScoutLaw1 = PickAndChooseScoutLaw(scoutLaw1, sl1PickText, sl1PickOptions.toList())
        val pickChooseScoutLaw2 = PickAndChooseScoutLaw(scoutLaw2, sl2PickText, sl2PickOptions.toList())

        val repository = Repository(stubResources, StubUserDataStore(), mockSharedPreferences)

        assertEquals(scoutLaw1, repository.laws[0])
        assertEquals(scoutLaw2, repository.laws[1])
        assertEquals(pickChooseScoutLaw1, repository.pickAndChooseLaws[0])
        assertEquals(pickChooseScoutLaw2, repository.pickAndChooseLaws[1])
    }

    @Test
    fun repositoryShouldBeInjectedCorrectly() {
        assertEquals(NUMBER_OF_LAWS, injectedRepository.laws.size)    // TestModule provides a resources stub with this many laws
    }

    @Test
    fun settingUserDataShouldSetCorrectValues() {
        val repository = Repository(getStubResourcesWithTwoLaws(), StubUserDataStore(), mockSharedPreferences)

        repository.apply {
            increaseTotalScoreBy(5)
            increaseTotalPossibleScoreBy(10)
            setBestMultipleTime(15)
            setBestPickChooseTime(20)

            assertEquals(5, getTotalScore())
            assertEquals(10, getTotalPossibleScore())
            assertEquals(15L, getBestMultipleTime())
            assertEquals(20L, getBestPickChooseTime())
        }
    }

    @Test
    fun resettingUserDataShouldResetUserData() {
        val repository = Repository(getStubResourcesWithTwoLaws(), StubUserDataStore(), mockSharedPreferences)
        repository.apply {
            increaseTotalScoreBy(5)
            increaseTotalPossibleScoreBy(10)
            setBestMultipleTime(15)
            setBestPickChooseTime(20)

            resetUserData()

            assertEquals(0, getTotalScore())
            assertEquals(0, getTotalPossibleScore())
            assertEquals(0L, getBestMultipleTime())
            assertEquals(0L, getBestPickChooseTime())
        }
    }
}