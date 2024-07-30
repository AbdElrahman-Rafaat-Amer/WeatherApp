package com.abdelrahman.raafat.climateClue.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
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

    fun setAppLocale(localeCode: String, resources: Resources) {
        val dm: DisplayMetrics = resources.displayMetrics
        val configuration: Configuration = resources.configuration
        configuration.setLocale(Locale(localeCode.lowercase(Locale.ROOT)))
        resources.updateConfiguration(configuration, dm)
        Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
        config = configuration
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