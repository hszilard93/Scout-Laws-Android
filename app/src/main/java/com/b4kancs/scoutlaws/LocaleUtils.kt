package com.b4kancs.scoutlaws

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * Created by hszilard on 11-Nov-18.
 */
private lateinit var currentLocale: String

fun getBaseContextWithLocale(context: Context, language: String): Context {
    currentLocale = language
    return updateResources(context, language)
}

fun getBaseContextWithCurrentLocale(context: Context): Context {
    return getBaseContextWithLocale(context, currentLocale)
}

fun refreshResources(context: Context) {
    updateResources(context, currentLocale)
}

private fun updateResources(context: Context, language: String): Context {
    val locale = Locale(language)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        val config = context.resources.configuration
        config.locales = localeList
        context.createConfigurationContext(config)
    } else {
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        context
    }
}