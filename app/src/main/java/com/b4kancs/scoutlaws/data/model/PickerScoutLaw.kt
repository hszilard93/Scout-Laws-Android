package com.b4kancs.scoutlaws.data.model

/**
 * Created by hszilard on 20-Mar-18.
 * Wraps over ScoutLaw, has additional picker quiz type specific data.
 */
data class PickerScoutLaw(val law: ScoutLaw, val pickerText: String, val pickerOptions: List<String>)
