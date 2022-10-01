package com.abdelrahman.rafaat.weatherapp.setting

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.preference.*
import com.abdelrahman.rafaat.weatherapp.MainActivity
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import java.util.*
import com.abdelrahman.rafaat.weatherapp.TAG


class Setting : PreferenceFragmentCompat() {

    private lateinit var defaultPref: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: --------------------------------")
        defaultPref = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val languagePreference: ListPreference = findPreference("language_name")!!
        languagePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                Log.i(TAG, "onPreferenceChange: language----------------> $newValue")
                var language: String = newValue as String
                if (language == "auto")
                    language = Resources.getSystem().configuration.locales[0].language
                setLanguage(language)
                true
            }

        val tempPreference: ListPreference = findPreference("temperature_unit")!!
        tempPreference.setOnPreferenceChangeListener { _, tempUnit ->
            Log.i(TAG, "onPreferenceChange: tempPreference----------------> $tempUnit")
            ConstantsValue.tempUnit = tempUnit as String
            true
        }

        val windSpeedPreference: ListPreference = findPreference("wind_speed")!!
        windSpeedPreference.setOnPreferenceChangeListener { _, windSpeedUnit ->
            Log.i(TAG, "windSpeedPreference----------------> $windSpeedUnit")
            ConstantsValue.windSpeedUnit = windSpeedUnit as String
            true
        }

        val locationPreference: ListPreference = findPreference("location_method")!!
        locationPreference.setOnPreferenceChangeListener { _, locationMethod ->
            Log.i(TAG, "locationPreference----------------> $locationMethod")
            ConstantsValue.locationMethod = locationMethod as String
            true
        }

        val notificationPreference: SwitchPreference = findPreference("toggle_notification")!!
        notificationPreference.setOnPreferenceChangeListener { _, notificationMethod ->
            Log.i(TAG, "notificationPreference----------------> $notificationMethod")
            ConstantsValue.notificationMethod = notificationMethod as Boolean
            true
        }
    }

    private fun setLanguage(language: String) {
        ConstantsValue.language = language
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        (requireActivity() as MainActivity).restartFragment()
    }
}