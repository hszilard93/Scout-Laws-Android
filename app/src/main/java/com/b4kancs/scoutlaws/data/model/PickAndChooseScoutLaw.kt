package com.b4kancs.scoutlaws.data.model

/**
 * Created by hszilard on 20-Mar-18.
 * Wraps over ScoutLaw, has additional pick and choose quiz type specific data.
 */
data class PickAndChooseScoutLaw(val law: ScoutLaw, val pickChooseText: String, val pickChooseOptions: List<String>)
