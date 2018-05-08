package com.b4kancs.scoutlaws.data

import android.content.res.Resources
import org.mockito.Mockito

/**
 * Created by hszilard on 07-May-18.
 */

/* Stub responses from android resources */
internal val numOfLawsId = 0
internal val sl1Id = 10
internal val sl1Text = "scoutlaw 1 name"
internal val sl1DescId = 11
internal val sl1Desc = "scoutlaw 1 description"
internal val sl1DescOrigId = 12
internal val sl1DescOrig = "scoutlaw 1 original description"
internal val sl1PickId = 13
internal val sl1PickText = "scoutlaw 1 pick and choose"
internal val sl1PickOptionsId = 14
internal val sl1PickOptions: Array<String> = arrayOf("one", "two")

internal val sl2Id = 20
internal val sl2Text = "scoutlaw 2 name"
internal val sl2DescId = 21
internal val sl2Desc = "scoutlaw 2 description"
internal val sl2DescOrigId = 22
internal val sl2DescOrig = "scoutlaw 2 original description"
internal val sl2PickId = 23
internal val sl2PickText = "scoutlaw 2 pick and choose"
internal val sl2PickOptionsId = 24
internal val sl2PickOptions: Array<String> = arrayOf("three", "four")

internal val sl3Id = 30
internal val sl3Text = "scoutlaw 3 name"
internal val sl3DescId = 31
internal val sl3Desc = "scoutlaw 3 description"
internal val sl3DescOrigId = 33
internal val sl3DescOrig = "scoutlaw 3 original description"
internal val sl3PickId = 33
internal val sl3PickText = "scoutlaw 3 pick and choose"
internal val sl3PickOptionsId = 34
internal val sl3PickOptions: Array<String> = arrayOf("five", "six")

internal val sl4Id = 40
internal val sl4Text = "scoutlaw 4 name"
internal val sl4DescId = 41
internal val sl4Desc = "scoutlaw 4 description"
internal val sl4DescOrigId = 44
internal val sl4DescOrig = "scoutlaw 4 original description"
internal val sl4PickId = 44
internal val sl4PickText = "scoutlaw 4 pick and choose"
internal val sl4PickOptionsId = 44
internal val sl4PickOptions: Array<String> = arrayOf("seven", "eight")

internal val sl5Id = 50
internal val sl5Text = "scoutlaw 5 name"
internal val sl5DescId = 51
internal val sl5Desc = "scoutlaw 5 description"
internal val sl5DescOrigId = 55
internal val sl5DescOrig = "scoutlaw 5 original description"
internal val sl5PickId = 55
internal val sl5PickText = "scoutlaw 5 pick and choose"
internal val sl5PickOptionsId = 55
internal val sl5PickOptions: Array<String> = arrayOf("nine", "ten")


/* This function stubs the android resources necessary for a Repository object to build two ScoutLaws and PickAndChooseScoutLaws. */
internal fun getStubResourcesWithTwoLaws(): Resources {
    val packageName = "com.b4kancs.scoutlaws"
    val resources = Mockito.mock(Resources::class.java)
    val numberOfLaws = 2

    Mockito.`when`(resources.getIdentifier("number_of_laws", "integer", packageName)).thenReturn(numOfLawsId)
    Mockito.`when`(resources.getInteger(numOfLawsId)).thenReturn(numberOfLaws)

    Mockito.`when`(resources.getIdentifier("law_1", "string", packageName)).thenReturn(sl1Id)
    Mockito.`when`(resources.getString(sl1Id)).thenReturn(sl1Text)
    Mockito.`when`(resources.getIdentifier("law_1_desc", "string", packageName)).thenReturn(sl1DescId)
    Mockito.`when`(resources.getString(sl1DescId)).thenReturn(sl1Desc)
    Mockito.`when`(resources.getIdentifier("law_1_desc_orig", "string", packageName)).thenReturn(sl1DescOrigId)
    Mockito.`when`(resources.getString(sl1DescOrigId)).thenReturn(sl1DescOrig)
    Mockito.`when`(resources.getIdentifier("law_1_pick", "string", packageName)).thenReturn(sl1PickId)
    Mockito.`when`(resources.getString(sl1PickId)).thenReturn(sl1PickText)
    Mockito.`when`(resources.getIdentifier("law_1_pick_options", "array", packageName)).thenReturn(sl1PickOptionsId)
    Mockito.`when`(resources.getStringArray(sl1PickOptionsId)).thenReturn(sl1PickOptions)

    Mockito.`when`(resources.getIdentifier("law_2", "string", packageName)).thenReturn(sl2Id)
    Mockito.`when`(resources.getString(sl2Id)).thenReturn(sl2Text)
    Mockito.`when`(resources.getIdentifier("law_2_desc", "string", packageName)).thenReturn(sl2DescId)
    Mockito.`when`(resources.getString(sl2DescId)).thenReturn(sl2Desc)
    Mockito.`when`(resources.getIdentifier("law_2_desc_orig", "string", packageName)).thenReturn(sl2DescOrigId)
    Mockito.`when`(resources.getString(sl2DescOrigId)).thenReturn(sl2DescOrig)
    Mockito.`when`(resources.getIdentifier("law_2_pick", "string", packageName)).thenReturn(sl2PickId)
    Mockito.`when`(resources.getString(sl2PickId)).thenReturn(sl2PickText)
    Mockito.`when`(resources.getIdentifier("law_2_pick_options", "array", packageName)).thenReturn(sl2PickOptionsId)
    Mockito.`when`(resources.getStringArray(sl2PickOptionsId)).thenReturn(sl2PickOptions)

    return resources
}

/* This function stubs the android resources necessary for a Repository object to build five ScoutLaws and PickAndChooseScoutLaws. */
internal fun getStubResourcesWithFiveLaws(): Resources {
    val packageName = "com.b4kancs.scoutlaws"
    val resources = Mockito.mock(Resources::class.java)
    val numberOfLaws = 5

    Mockito.`when`(resources.getIdentifier("number_of_laws", "integer", packageName)).thenReturn(numOfLawsId)
    Mockito.`when`(resources.getInteger(numOfLawsId)).thenReturn(numberOfLaws)

    Mockito.`when`(resources.getIdentifier("law_1", "string", packageName)).thenReturn(sl1Id)
    Mockito.`when`(resources.getString(sl1Id)).thenReturn(sl1Text)
    Mockito.`when`(resources.getIdentifier("law_1_desc", "string", packageName)).thenReturn(sl1DescId)
    Mockito.`when`(resources.getString(sl1DescId)).thenReturn(sl1Desc)
    Mockito.`when`(resources.getIdentifier("law_1_desc_orig", "string", packageName)).thenReturn(sl1DescOrigId)
    Mockito.`when`(resources.getString(sl1DescOrigId)).thenReturn(sl1DescOrig)
    Mockito.`when`(resources.getIdentifier("law_1_pick", "string", packageName)).thenReturn(sl1PickId)
    Mockito.`when`(resources.getString(sl1PickId)).thenReturn(sl1PickText)
    Mockito.`when`(resources.getIdentifier("law_1_pick_options", "array", packageName)).thenReturn(sl1PickOptionsId)
    Mockito.`when`(resources.getStringArray(sl1PickOptionsId)).thenReturn(sl1PickOptions)

    Mockito.`when`(resources.getIdentifier("law_2", "string", packageName)).thenReturn(sl2Id)
    Mockito.`when`(resources.getString(sl2Id)).thenReturn(sl2Text)
    Mockito.`when`(resources.getIdentifier("law_2_desc", "string", packageName)).thenReturn(sl2DescId)
    Mockito.`when`(resources.getString(sl2DescId)).thenReturn(sl2Desc)
    Mockito.`when`(resources.getIdentifier("law_2_desc_orig", "string", packageName)).thenReturn(sl2DescOrigId)
    Mockito.`when`(resources.getString(sl2DescOrigId)).thenReturn(sl2DescOrig)
    Mockito.`when`(resources.getIdentifier("law_2_pick", "string", packageName)).thenReturn(sl2PickId)
    Mockito.`when`(resources.getString(sl2PickId)).thenReturn(sl2PickText)
    Mockito.`when`(resources.getIdentifier("law_2_pick_options", "array", packageName)).thenReturn(sl2PickOptionsId)
    Mockito.`when`(resources.getStringArray(sl2PickOptionsId)).thenReturn(sl2PickOptions)

    Mockito.`when`(resources.getIdentifier("law_3", "string", packageName)).thenReturn(sl3Id)
    Mockito.`when`(resources.getString(sl3Id)).thenReturn(sl3Text)
    Mockito.`when`(resources.getIdentifier("law_3_desc", "string", packageName)).thenReturn(sl3DescId)
    Mockito.`when`(resources.getString(sl3DescId)).thenReturn(sl3Desc)
    Mockito.`when`(resources.getIdentifier("law_3_desc_orig", "string", packageName)).thenReturn(sl3DescOrigId)
    Mockito.`when`(resources.getString(sl3DescOrigId)).thenReturn(sl3DescOrig)
    Mockito.`when`(resources.getIdentifier("law_3_pick", "string", packageName)).thenReturn(sl3PickId)
    Mockito.`when`(resources.getString(sl3PickId)).thenReturn(sl3PickText)
    Mockito.`when`(resources.getIdentifier("law_3_pick_options", "array", packageName)).thenReturn(sl3PickOptionsId)
    Mockito.`when`(resources.getStringArray(sl3PickOptionsId)).thenReturn(sl3PickOptions)

    Mockito.`when`(resources.getIdentifier("law_4", "string", packageName)).thenReturn(sl4Id)
    Mockito.`when`(resources.getString(sl4Id)).thenReturn(sl4Text)
    Mockito.`when`(resources.getIdentifier("law_4_desc", "string", packageName)).thenReturn(sl4DescId)
    Mockito.`when`(resources.getString(sl4DescId)).thenReturn(sl4Desc)
    Mockito.`when`(resources.getIdentifier("law_4_desc_orig", "string", packageName)).thenReturn(sl4DescOrigId)
    Mockito.`when`(resources.getString(sl4DescOrigId)).thenReturn(sl4DescOrig)
    Mockito.`when`(resources.getIdentifier("law_4_pick", "string", packageName)).thenReturn(sl4PickId)
    Mockito.`when`(resources.getString(sl4PickId)).thenReturn(sl4PickText)
    Mockito.`when`(resources.getIdentifier("law_4_pick_options", "array", packageName)).thenReturn(sl4PickOptionsId)
    Mockito.`when`(resources.getStringArray(sl4PickOptionsId)).thenReturn(sl4PickOptions)

    Mockito.`when`(resources.getIdentifier("law_5", "string", packageName)).thenReturn(sl5Id)
    Mockito.`when`(resources.getString(sl5Id)).thenReturn(sl5Text)
    Mockito.`when`(resources.getIdentifier("law_5_desc", "string", packageName)).thenReturn(sl5DescId)
    Mockito.`when`(resources.getString(sl5DescId)).thenReturn(sl5Desc)
    Mockito.`when`(resources.getIdentifier("law_5_desc_orig", "string", packageName)).thenReturn(sl5DescOrigId)
    Mockito.`when`(resources.getString(sl5DescOrigId)).thenReturn(sl5DescOrig)
    Mockito.`when`(resources.getIdentifier("law_5_pick", "string", packageName)).thenReturn(sl5PickId)
    Mockito.`when`(resources.getString(sl5PickId)).thenReturn(sl5PickText)
    Mockito.`when`(resources.getIdentifier("law_5_pick_options", "array", packageName)).thenReturn(sl5PickOptionsId)
    Mockito.`when`(resources.getStringArray(sl5PickOptionsId)).thenReturn(sl5PickOptions)

    return resources
}
