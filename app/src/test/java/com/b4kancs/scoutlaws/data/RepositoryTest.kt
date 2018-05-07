package com.b4kancs.scoutlaws.data

import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import org.junit.Assert
import org.junit.Test

/**
 * Created by hszilard on 02-May-18.
 */

class RepositoryTest {

    @Test
    fun repositoryLoadsScoutLawsInOrder() {
        val stubResources = getStubResourcesWithTwoLaws()

        val scoutLaw1 = ScoutLaw(1, sl1Text, sl1Desc, sl1DescOrig)
        val scoutLaw2 = ScoutLaw(2, sl2Text, sl2Desc, sl2DescOrig)
        val pickChooseScoutLaw1 = PickAndChooseScoutLaw(scoutLaw1, sl1PickText, sl1PickOptions.toList())
        val pickChooseScoutLaw2 = PickAndChooseScoutLaw(scoutLaw2, sl2PickText, sl2PickOptions.toList())

        val repository = Repository(stubResources)

        Assert.assertEquals(numOfLaws, repository.numberOfScoutLaws)
        Assert.assertEquals(scoutLaw1, repository.laws[0])
        Assert.assertEquals(scoutLaw2, repository.laws[1])
        Assert.assertEquals(pickChooseScoutLaw1, repository.pickAndChooseLaws[0])
        Assert.assertEquals(pickChooseScoutLaw2, repository.pickAndChooseLaws[1])
    }
}