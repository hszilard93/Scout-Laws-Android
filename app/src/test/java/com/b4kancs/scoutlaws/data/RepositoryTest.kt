package com.b4kancs.scoutlaws.data

import com.b4kancs.scoutlaws.DaggerTestComponent
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.TestComponent
import com.b4kancs.scoutlaws.TestModule
import com.b4kancs.scoutlaws.TestModule.Companion.numberOfLaws
import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

/**
 * Created by hszilard on 02-May-18.
 */
class RepositoryTest {

    @Inject
    lateinit var injectedRepository: Repository

    private val scoutLaw1 = ScoutLaw(1, sl1Text, sl1Desc, sl1DescOrig)
    private val scoutLaw2 = ScoutLaw(2, sl2Text, sl2Desc, sl2DescOrig)
    private val pickChooseScoutLaw1 = PickAndChooseScoutLaw(scoutLaw1, sl1PickText, sl1PickOptions.toList())
    private val pickChooseScoutLaw2 = PickAndChooseScoutLaw(scoutLaw2, sl2PickText, sl2PickOptions.toList())

    @Test
    fun numberOfScoutLawsShouldBeCorrect() {
        val stubResources = getStubResourcesWithTwoLaws()

        val repository = Repository(stubResources)

        assertEquals(2, repository.numberOfScoutLaws)
    }

    @Test
    fun repositoryShouldLoadScoutLawsInOrder() {
        val stubResources = getStubResourcesWithTwoLaws()

        val repository = Repository(stubResources)

        assertEquals(scoutLaw1, repository.laws[0])
        assertEquals(scoutLaw2, repository.laws[1])
        assertEquals(pickChooseScoutLaw1, repository.pickAndChooseLaws[0])
        assertEquals(pickChooseScoutLaw2, repository.pickAndChooseLaws[1])
    }

    @Test
    fun repositoryShouldBeInjectedCorrectly() {
        /* Set Dagger up for testing */
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule((TestModule())).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent

        assertEquals(numberOfLaws, injectedRepository.laws.size)    // TestModule provides a resources stub with this many laws
    }
}