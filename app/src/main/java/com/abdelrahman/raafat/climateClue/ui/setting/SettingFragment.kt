package com.abdelrahman.raafat.climateClue.ui.setting

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.abdelrahman.raafat.climateClue.MainActivity
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.utils.LocaleHelper


class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languagePreference: ListPreference? = findPreference("language_name")
        languagePreference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                var language: String = newValue as String
                if (language == "auto")
                    language = Resources.getSystem().configuration.locales[0].language
                setLanguage(language)
                true
            }

        val tempPreference: ListPreference? = findPreference("temperature_unit")
        tempPreference?.setOnPreferenceChangeListener { _, tempUnit ->
            ConstantsValue.tempUnit = tempUnit as String
            true
        }

        val windSpeedPreference: ListPreference? = findPreference("wind_speed")
        windSpeedPreference?.setOnPreferenceChangeListener { _, windSpeedUnit ->
            ConstantsValue.windSpeedUnit = windSpeedUnit as String
            true
        }

        val notificationPreference: SwitchPreference? = findPreference("toggle_notification")
        notificationPreference?.setOnPreferenceChangeListener { _, notificationMethod ->
            ConstantsValue.notificationMethod = notificationMethod as Boolean
            true
        }

        val is24HoursEnabledPreference: SwitchPreference? = findPreference("IS_24HOURS_ENABLED")
        is24HoursEnabledPreference?.setOnPreferenceChangeListener { _, isEnabled ->
            ConstantsValue.is24HoursEnabled = isEnabled as Boolean
            true
        }
    }

    private fun setLanguage(language: String) {
        ConstantsValue.language = language
        LocaleHelper.updateLocale(requireContext(), language)
        (requireActivity() as MainActivity).restartFragment()
    }
}