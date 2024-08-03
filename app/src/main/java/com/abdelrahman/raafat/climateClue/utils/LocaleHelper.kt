package com.abdelrahman.raafat.climateClue.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    private var config: Configuration? = null

    fun updateLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val newConfig = context.resources.configuration
        newConfig.setLocale(locale)
        config = newConfig
    }

    fun getLocalizedContext(context: Context): Context {
        if (config != null) {
            val newContext = context.createConfigurationContext(config!!)
            return newContext
        } else {
            return context
        }

    }

    fun isRTL(): Boolean {
        val rtlLanguages = setOf("ar", "fa", "he", "ur") // Arabic, Persian, Hebrew, Urdu
        val currentLanguage = Locale.getDefault().language
        return rtlLanguages.contains(currentLanguage)
    }
}