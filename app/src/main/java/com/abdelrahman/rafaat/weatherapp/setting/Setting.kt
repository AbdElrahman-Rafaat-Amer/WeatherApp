package com.abdelrahman.rafaat.weatherapp.setting

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import java.util.*

private const val TAG = "PreferenceFragmentCompat"

class Setting : PreferenceFragmentCompat() {

    private val defaultPref by lazy { PreferenceManager.getDefaultSharedPreferences(requireContext()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultPref.registerOnSharedPreferenceChangeListener { _, _ ->
            Log.i(TAG, "onSharedPreferenceChanged: -------------------")
            var language = defaultPref.getString("language_name", "en")
            if (language == "auto")
                language = Resources.getSystem().configuration.locales[0].language
            ConstantsValue.language = language!!
            setLanguage(language)

            ConstantsValue.tempUnit = defaultPref.getString("temperature_unit", "K").toString()
            ConstantsValue.windSpeedUnit = defaultPref.getString("wind_speed", "S").toString()
            preferenceScreen = null
            addPreferencesFromResource(R.xml.setting_preference)
        }


    }


    private fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}