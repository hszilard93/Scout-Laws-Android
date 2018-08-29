package com.b4kancs.scoutlaws

import android.content.res.Resources
import org.mockito.Mockito.*

/**
 * Created by hszilard on 07-May-18.
 */

/* Stub responses from android resources */
internal const val numOfLawsId = 0
internal const val sl1Id = 10
internal const val sl1Text = "scoutlaw 1 name"
internal const val sl1DescId = 11
internal const val sl1Desc = "scoutlaw 1 description"
internal const val sl1DescOrigId = 12
internal const val sl1DescOrig = "scoutlaw 1 original description"
internal const val sl1PickId = 13
internal const val sl1PickText = "scoutlaw 1 pick and choose"
internal const val sl1PickOptionsId = 14
internal val sl1PickOptions = arrayOf("one", "two")

internal const val sl2Id = 20
internal const val sl2Text = "scoutlaw 2 name"
internal const val sl2DescId = 21
internal const val sl2Desc = "scoutlaw 2 description"
internal const val sl2DescOrigId = 22
internal const val sl2DescOrig = "scoutlaw 2 original description"
internal const val sl2PickId = 23
internal const val sl2PickText = "scoutlaw 2 pick and choose"
internal const val sl2PickOptionsId = 24
internal val sl2PickOptions = arrayOf("three", "four")

internal const val sl3Id = 30
internal const val sl3Text = "scoutlaw 3 name"
internal const val sl3DescId = 31
internal const val sl3Desc = "scoutlaw 3 description"
internal const val sl3DescOrigId = 33
internal const val sl3DescOrig = "scoutlaw 3 original description"
internal const val sl3PickId = 33
internal const val sl3PickText = "scoutlaw 3 pick and choose"
internal const val sl3PickOptionsId = 34
internal val sl3PickOptions = arrayOf("five", "six")

internal const val sl4Id = 40
internal const val sl4Text = "scoutlaw 4 name"
internal const val sl4DescId = 41
internal const val sl4Desc = "scoutlaw 4 description"
internal const val sl4DescOrigId = 44
internal const val sl4DescOrig = "scoutlaw 4 original description"
internal const val sl4PickId = 44
internal const val sl4PickText = "scoutlaw 4 pick and choose"
internal const val sl4PickOptionsId = 44
internal val sl4PickOptions = arrayOf("seven", "eight")

internal const val sl5Id = 50
internal const val sl5Text = "scoutlaw 5 name"
internal const val sl5DescId = 51
internal const val sl5Desc = "scoutlaw 5 description"
internal const val sl5DescOrigId = 55
internal const val sl5DescOrig = "scoutlaw 5 original description"
internal const val sl5PickId = 55
internal const val sl5PickText = "scoutlaw 5 pick and choose"
internal const val sl5PickOptionsId = 55
internal val sl5PickOptions = arrayOf("nine", "ten")


/* This function stubs the android resources necessary for a Repository object to build two ScoutLaws and PickAndChooseScoutLaws. */
internal fun getStubResourcesWithTwoLaws(): Resources {
    val packageName = "com.b4kancs.scoutlaws"
    val resources = mock(Resources::class.java)
    val numberOfLaws = 2

    `when`(resources.getIdentifier("number_of_laws", "integer", packageName)).thenReturn(numOfLawsId)
    `when`(resources.getInteger(numOfLawsId)).thenReturn(numberOfLaws)

    `when`(resources.getIdentifier("law_1", "string", packageName)).thenReturn(sl1Id)
    `when`(resources.getString(sl1Id)).thenReturn(sl1Text)
    `when`(resources.getIdentifier("law_1_desc", "string", packageName)).thenReturn(sl1DescId)
    `when`(resources.getString(sl1DescId)).thenReturn(sl1Desc)
    `when`(resources.getIdentifier("law_1_desc_orig", "string", packageName)).thenReturn(sl1DescOrigId)
    `when`(resources.getString(sl1DescOrigId)).thenReturn(sl1DescOrig)
    `when`(resources.getIdentifier("law_1_pick", "string", packageName)).thenReturn(sl1PickId)
    `when`(resources.getString(sl1PickId)).thenReturn(sl1PickText)
    `when`(resources.getIdentifier("law_1_pick_options", "array", packageName)).thenReturn(sl1PickOptionsId)
    `when`(resources.getStringArray(sl1PickOptionsId)).thenReturn(sl1PickOptions)

    `when`(resources.getIdentifier("law_2", "string", packageName)).thenReturn(sl2Id)
    `when`(resources.getString(sl2Id)).thenReturn(sl2Text)
    `when`(resources.getIdentifier("law_2_desc", "string", packageName)).thenReturn(sl2DescId)
    `when`(resources.getString(sl2DescId)).thenReturn(sl2Desc)
    `when`(resources.getIdentifier("law_2_desc_orig", "string", packageName)).thenReturn(sl2DescOrigId)
    `when`(resources.getString(sl2DescOrigId)).thenReturn(sl2DescOrig)
    `when`(resources.getIdentifier("law_2_pick", "string", packageName)).thenReturn(sl2PickId)
    `when`(resources.getString(sl2PickId)).thenReturn(sl2PickText)
    `when`(resources.getIdentifier("law_2_pick_options", "array", packageName)).thenReturn(sl2PickOptionsId)
    `when`(resources.getStringArray(sl2PickOptionsId)).thenReturn(sl2PickOptions)

    return resources
}

/* This function stubs the android resources necessary for a Repository object to build five ScoutLaws and PickAndChooseScoutLaws. */
internal fun getStubResourcesWithFiveLaws(): Resources {
    val packageName = "com.b4kancs.scoutlaws"
    val resources = mock(Resources::class.java)
    val numberOfLaws = 5

    `when`(resources.getIdentifier("number_of_laws", "integer", packageName)).thenReturn(numOfLawsId)
    `when`(resources.getInteger(numOfLawsId)).thenReturn(numberOfLaws)

    `when`(resources.getIdentifier("law_1", "string", packageName)).thenReturn(sl1Id)
    `when`(resources.getString(sl1Id)).thenReturn(sl1Text)
    `when`(resources.getIdentifier("law_1_desc", "string", packageName)).thenReturn(sl1DescId)
    `when`(resources.getString(sl1DescId)).thenReturn(sl1Desc)
    `when`(resources.getIdentifier("law_1_desc_orig", "string", packageName)).thenReturn(sl1DescOrigId)
    `when`(resources.getString(sl1DescOrigId)).thenReturn(sl1DescOrig)
    `when`(resources.getIdentifier("law_1_pick", "string", packageName)).thenReturn(sl1PickId)
    `when`(resources.getString(sl1PickId)).thenReturn(sl1PickText)
    `when`(resources.getIdentifier("law_1_pick_options", "array", packageName)).thenReturn(sl1PickOptionsId)
    `when`(resources.getStringArray(sl1PickOptionsId)).thenReturn(sl1PickOptions)

    `when`(resources.getIdentifier("law_2", "string", packageName)).thenReturn(sl2Id)
    `when`(resources.getString(sl2Id)).thenReturn(sl2Text)
    `when`(resources.getIdentifier("law_2_desc", "string", packageName)).thenReturn(sl2DescId)
    `when`(resources.getString(sl2DescId)).thenReturn(sl2Desc)
    `when`(resources.getIdentifier("law_2_desc_orig", "string", packageName)).thenReturn(sl2DescOrigId)
    `when`(resources.getString(sl2DescOrigId)).thenReturn(sl2DescOrig)
    `when`(resources.getIdentifier("law_2_pick", "string", packageName)).thenReturn(sl2PickId)
    `when`(resources.getString(sl2PickId)).thenReturn(sl2PickText)
    `when`(resources.getIdentifier("law_2_pick_options", "array", packageName)).thenReturn(sl2PickOptionsId)
    `when`(resources.getStringArray(sl2PickOptionsId)).thenReturn(sl2PickOptions)

    `when`(resources.getIdentifier("law_3", "string", packageName)).thenReturn(sl3Id)
    `when`(resources.getString(sl3Id)).thenReturn(sl3Text)
    `when`(resources.getIdentifier("law_3_desc", "string", packageName)).thenReturn(sl3DescId)
    `when`(resources.getString(sl3DescId)).thenReturn(sl3Desc)
    `when`(resources.getIdentifier("law_3_desc_orig", "string", packageName)).thenReturn(sl3DescOrigId)
    `when`(resources.getString(sl3DescOrigId)).thenReturn(sl3DescOrig)
    `when`(resources.getIdentifier("law_3_pick", "string", packageName)).thenReturn(sl3PickId)
    `when`(resources.getString(sl3PickId)).thenReturn(sl3PickText)
    `when`(resources.getIdentifier("law_3_pick_options", "array", packageName)).thenReturn(sl3PickOptionsId)
    `when`(resources.getStringArray(sl3PickOptionsId)).thenReturn(sl3PickOptions)

    `when`(resources.getIdentifier("law_4", "string", packageName)).thenReturn(sl4Id)
    `when`(resources.getString(sl4Id)).thenReturn(sl4Text)
    `when`(resources.getIdentifier("law_4_desc", "string", packageName)).thenReturn(sl4DescId)
    `when`(resources.getString(sl4DescId)).thenReturn(sl4Desc)
    `when`(resources.getIdentifier("law_4_desc_orig", "string", packageName)).thenReturn(sl4DescOrigId)
    `when`(resources.getString(sl4DescOrigId)).thenReturn(sl4DescOrig)
    `when`(resources.getIdentifier("law_4_pick", "string", packageName)).thenReturn(sl4PickId)
    `when`(resources.getString(sl4PickId)).thenReturn(sl4PickText)
    `when`(resources.getIdentifier("law_4_pick_options", "array", packageName)).thenReturn(sl4PickOptionsId)
    `when`(resources.getStringArray(sl4PickOptionsId)).thenReturn(sl4PickOptions)

    `when`(resources.getIdentifier("law_5", "string", packageName)).thenReturn(sl5Id)
    `when`(resources.getString(sl5Id)).thenReturn(sl5Text)
    `when`(resources.getIdentifier("law_5_desc", "string", packageName)).thenReturn(sl5DescId)
    `when`(resources.getString(sl5DescId)).thenReturn(sl5Desc)
    `when`(resources.getIdentifier("law_5_desc_orig", "string", packageName)).thenReturn(sl5DescOrigId)
    `when`(resources.getString(sl5DescOrigId)).thenReturn(sl5DescOrig)
    `when`(resources.getIdentifier("law_5_pick", "string", packageName)).thenReturn(sl5PickId)
    `when`(resources.getString(sl5PickId)).thenReturn(sl5PickText)
    `when`(resources.getIdentifier("law_5_pick_options", "array", packageName)).thenReturn(sl5PickOptionsId)
    `when`(resources.getStringArray(sl5PickOptionsId)).thenReturn(sl5PickOptions)

    return resources
}
