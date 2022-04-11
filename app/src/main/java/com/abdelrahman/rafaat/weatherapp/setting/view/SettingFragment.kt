package com.abdelrahman.rafaat.weatherapp.setting.view

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.abdelrahman.rafaat.weatherapp.InitializationScreenActivity
import com.abdelrahman.rafaat.weatherapp.MainActivity
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.maps.view.GoogleMapsActivity
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.isInternetAvailable
import com.google.android.material.snackbar.Snackbar
import java.util.*


class SettingFragment : Fragment() {

    private val TAG = "SettingFragment"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    //language
    private lateinit var languageRadioGroup: RadioGroup
    private lateinit var showChangeLanguageView: RelativeLayout
    private lateinit var visibilityChangeLanguageLinear: LinearLayout
    private lateinit var languageRadioButton: RadioButton

    private lateinit var cardViewLanguage: CardView
    private lateinit var showLanguageOptionImageView: ImageView

    //temperature
    private lateinit var temperatureRadioGroup: RadioGroup
    private lateinit var showChangeTemperatureUnitView: RelativeLayout
    private lateinit var visibilityChangeTemperatureLinear: LinearLayout
    private lateinit var temperatureRadioButton: RadioButton
    private lateinit var cardViewTemperature: CardView
    private lateinit var showTemperatureOptionImageView: ImageView

    // wind speed
    private lateinit var windSpeedRadioGroup: RadioGroup
    private lateinit var showChangeWindSpeedUnitView: RelativeLayout
    private lateinit var visibilityChangeWindSpeedLinear: LinearLayout
    private lateinit var windSpeedRadioButton: RadioButton
    private lateinit var cardViewWindSpeed: CardView
    private lateinit var showWindSpeedOptionImageView: ImageView

    //location
    private lateinit var locationRadioGroup: RadioGroup
    private lateinit var showChangeLocationMethodView: RelativeLayout
    private lateinit var visibilityChangeLocationLinear: LinearLayout
    private lateinit var locationRadioButton: RadioButton
    private lateinit var cardViewLocation: CardView
    private lateinit var showLocationOptionImageView: ImageView

    //notification
    private lateinit var notificationRadioGroup: RadioGroup
    private lateinit var showChangeNotificationMethodView: RelativeLayout
    private lateinit var visibilityChangeNotificationLinear: LinearLayout
    private lateinit var notificationRadioButton: RadioButton
    private lateinit var cardViewNotification: CardView
    private lateinit var showNotificationOptionImageView: ImageView

    private lateinit var radioButtonTag: String
    private lateinit var currentView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences("SETTING", MODE_PRIVATE)
        editor = sharedPreferences.edit()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        currentView = view
        initUI(view)


        showChangeLanguageView.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "inside setOnClickListener: ")
            if (visibilityChangeLanguageLinear.visibility == View.GONE) {
                Log.i(TAG, "inside if: ")
                visibilityChangeLanguageLinear.visibility = View.VISIBLE
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                showLanguageOptionImageView.setImageResource(R.drawable.ic_up_arrow)
                languageRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                    val selectedOption: Int = languageRadioGroup.checkedRadioButtonId
                    languageRadioButton = view.findViewById(selectedOption)
                    radioButtonTag = languageRadioButton.tag.toString()
                    Log.i(TAG, "radioButtonTag language: $radioButtonTag")
                    when (radioButtonTag) {
                        "A" -> {
                            setLanguage("ar")
                            ConstantsValue.language = "ar"
                        }
                        "E" -> {
                            setLanguage("en")
                            ConstantsValue.language = "en"
                        }
                    }
                    Log.i(TAG, "language: " + ConstantsValue.language)
                }
            } else {
                Log.i(TAG, "inside else: ")
                visibilityChangeLanguageLinear.visibility = View.GONE
                showLanguageOptionImageView.setImageResource(R.drawable.ic_down_arrow)
            }
            TransitionManager.beginDelayedTransition(cardViewLanguage, AutoTransition())
        })

        showChangeTemperatureUnitView.setOnClickListener(View.OnClickListener {
            if (visibilityChangeTemperatureLinear.visibility == View.GONE) {
                visibilityChangeTemperatureLinear.visibility = View.VISIBLE
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                showTemperatureOptionImageView.setImageResource(R.drawable.ic_up_arrow)
                temperatureRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                    val selectedOption: Int = temperatureRadioGroup.checkedRadioButtonId
                    temperatureRadioButton = view.findViewById(selectedOption)
                    radioButtonTag = temperatureRadioButton.tag.toString()
                    Log.i(TAG, "radioButtonTag Temperature: $radioButtonTag")
                    when (radioButtonTag) {
                        "K" -> {
                            ConstantsValue.tempUnit = "K"
                            temperatureRadioButton.isChecked = true
                            editor.putString("CURRENT_TEMPERATURE", "K").commit()
                        }
                        "C" -> {
                            ConstantsValue.tempUnit = "C"
                            temperatureRadioButton.isChecked = true
                            editor.putString("CURRENT_TEMPERATURE", "C").commit()
                        }
                        "F" -> {
                            ConstantsValue.tempUnit = "F"
                            temperatureRadioButton.isChecked = true
                            editor.putString("CURRENT_TEMPERATURE", "F").commit()
                        }
                    }
                    Log.i(TAG, "tempUnit: " + ConstantsValue.tempUnit)
                }
            } else {
                visibilityChangeTemperatureLinear.visibility = View.GONE
                showTemperatureOptionImageView.setImageResource(R.drawable.ic_down_arrow)
            }
            TransitionManager.beginDelayedTransition(cardViewTemperature, AutoTransition())
        })

        showChangeWindSpeedUnitView.setOnClickListener(View.OnClickListener {
            if (visibilityChangeWindSpeedLinear.visibility == View.GONE) {
                visibilityChangeWindSpeedLinear.visibility = View.VISIBLE
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                showWindSpeedOptionImageView.setImageResource(R.drawable.ic_up_arrow)
                windSpeedRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                    val selectedOption: Int = windSpeedRadioGroup.checkedRadioButtonId
                    windSpeedRadioButton = view.findViewById(selectedOption)
                    radioButtonTag = windSpeedRadioButton.tag.toString()
                    Log.i(TAG, "radioButtonTag Wind: $radioButtonTag")
                    when (radioButtonTag) {
                        "S" -> {
                            ConstantsValue.windSpeedUnit = "S"
                            editor.putString("CURRENT_WIND_SPEED", "S").commit()
                        }
                        "H" -> {
                            ConstantsValue.windSpeedUnit = "H"
                            editor.putString("CURRENT_WIND_SPEED", "H").commit()
                        }
                    }
                    Log.i(TAG, "windSpeedUnit: " + ConstantsValue.windSpeedUnit)
                }
            } else {
                visibilityChangeWindSpeedLinear.visibility = View.GONE
                showWindSpeedOptionImageView.setImageResource(R.drawable.ic_down_arrow)
            }
            TransitionManager.beginDelayedTransition(cardViewWindSpeed, AutoTransition())
        })

        showChangeLocationMethodView.setOnClickListener(View.OnClickListener {
            if (visibilityChangeLocationLinear.visibility == View.GONE) {
                visibilityChangeLocationLinear.visibility = View.VISIBLE
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                showLocationOptionImageView.setImageResource(R.drawable.ic_up_arrow)
                locationRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                    val selectedOption: Int = locationRadioGroup.checkedRadioButtonId
                    locationRadioButton = view.findViewById(selectedOption)
                    radioButtonTag = locationRadioButton.tag.toString()
                    Log.i(TAG, "radioButtonTag Location: $radioButtonTag")
                    when (radioButtonTag) {
                        "G" -> {

                            if (isInternetAvailable(requireContext())) {
                                ConstantsValue.locationMethod = "G"
                                editor.putString("CURRENT_LOCATION", "G").commit()
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        InitializationScreenActivity::class.java
                                    )
                                )
                                requireActivity().finish()
                            } else {
                                showSnackBar()
                            }

                        }
                        "M" -> {

                            if (isInternetAvailable(requireContext())) {
                                ConstantsValue.locationMethod = "M"
                                editor.putString("CURRENT_LOCATION", "M").commit()
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        GoogleMapsActivity::class.java
                                    )
                                )
                                requireActivity().finish()
                            } else {
                                showSnackBar()
                            }
                        }
                    }
                    Log.i(TAG, "locationMethod: " + ConstantsValue.locationMethod)
                }
            } else {
                visibilityChangeLocationLinear.visibility = View.GONE
                showLocationOptionImageView.setImageResource(R.drawable.ic_down_arrow)
            }
            TransitionManager.beginDelayedTransition(cardViewLocation, AutoTransition())
        })

        showChangeNotificationMethodView.setOnClickListener(View.OnClickListener {
            if (visibilityChangeNotificationLinear.visibility == View.GONE) {
                visibilityChangeNotificationLinear.visibility = View.VISIBLE
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                showNotificationOptionImageView.setImageResource(R.drawable.ic_up_arrow)
                notificationRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                    val selectedOption: Int = notificationRadioGroup.checkedRadioButtonId
                    notificationRadioButton = view.findViewById(selectedOption)
                    radioButtonTag = notificationRadioButton.tag.toString()
                    Log.i(TAG, "radioButtonTag notification: $radioButtonTag")
                    when (radioButtonTag) {
                        "E" -> {
                            ConstantsValue.notificationMethod = "E"
                            editor.putString("CURRENT_NOTIFICATION", "E").commit()
                        }
                        "D" -> {
                            ConstantsValue.notificationMethod = "D"
                            editor.putString("CURRENT_NOTIFICATION", "D").commit()
                        }
                    }
                    Log.i(TAG, "notificationMethod: " + ConstantsValue.notificationMethod)
                }
            } else {
                visibilityChangeNotificationLinear.visibility = View.GONE
                showNotificationOptionImageView.setImageResource(R.drawable.ic_down_arrow)
            }
            TransitionManager.beginDelayedTransition(cardViewNotification, AutoTransition())
        })


        return view
    }


    private fun showSnackBar() {
        var snackBar = Snackbar.make(
            currentView.findViewById(R.id.ConstraintLayout_SettingFragment),
            getString(R.string.error_network_for_update),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)

        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }


    override fun onResume() {
        super.onResume()

        //language
        if (ConstantsValue.language == "ar") {
            languageRadioGroup.check(R.id.language_arabic)
        } else {
            languageRadioGroup.check(R.id.language_english)
        }

        //Temperature
        when (ConstantsValue.tempUnit) {
            "C" ->
                temperatureRadioGroup.check(R.id.temperature_celsius)
            "F" ->
                temperatureRadioGroup.check(R.id.temperature_fahrenheit)
            else ->
                temperatureRadioGroup.check(R.id.temperature_kelvin)
        }

        //Wind Speed
        when (ConstantsValue.windSpeedUnit) {
            "H" ->
                windSpeedRadioGroup.check(R.id.wind_speed_MH)
            "S" ->
                windSpeedRadioGroup.check(R.id.wind_speed_MS)
        }

        //Location
        when (ConstantsValue.locationMethod) {
            "G" ->
                locationRadioGroup.check(R.id.location_GPS)
            "M" ->
                locationRadioGroup.check(R.id.location_MAP)
        }

        //Notification
        when (ConstantsValue.notificationMethod) {
            "E" ->
                notificationRadioGroup.check(R.id.notification_enabled)
            "D" ->
                notificationRadioGroup.check(R.id.notification_disabled)
        }

    }


    private fun initUI(view: View) {

        languageRadioGroup = view.findViewById(R.id.language_radio_group)
        temperatureRadioGroup = view.findViewById(R.id.temperature_radio_group)
        windSpeedRadioGroup = view.findViewById(R.id.wind_speed_radio_group)
        locationRadioGroup = view.findViewById(R.id.location_radio_group)
        notificationRadioGroup = view.findViewById(R.id.notification_radio_group)

        showChangeLanguageView = view.findViewById(R.id.show_change_language_View)
        showChangeLocationMethodView = view.findViewById(R.id.show_change_location_method_View)
        showChangeTemperatureUnitView = view.findViewById(R.id.show_change_temperature_unit_View)
        showChangeWindSpeedUnitView = view.findViewById(R.id.show_change_wind_speed_unit_View)
        showChangeNotificationMethodView =
            view.findViewById(R.id.show_change_Notification_method_View)

        visibilityChangeLanguageLinear =
            view.findViewById(R.id.linear_layout_visibility_change_language)
        visibilityChangeLocationLinear =
            view.findViewById(R.id.linear_layout_visibility_change_location_method)
        visibilityChangeTemperatureLinear =
            view.findViewById(R.id.linear_layout_visibility_change_temperature_unit)
        visibilityChangeWindSpeedLinear =
            view.findViewById(R.id.linear_layout_visibility_change_wind_speed_unit)
        visibilityChangeNotificationLinear =
            view.findViewById(R.id.linear_layout_visibility_change_Notification_method)

        cardViewLanguage = view.findViewById(R.id.change_language_cardView)
        cardViewTemperature = view.findViewById(R.id.change_temperature_unit_cardView)
        cardViewWindSpeed = view.findViewById(R.id.change_wind_speed_unit_cardView)
        cardViewLocation = view.findViewById(R.id.change_location_method_cardView)
        cardViewNotification = view.findViewById(R.id.change_Notification_method_cardView)

        showLanguageOptionImageView = view.findViewById(R.id.show_language_options_imageView)
        showTemperatureOptionImageView = view.findViewById(R.id.show_temperature_options_imageView)
        showWindSpeedOptionImageView = view.findViewById(R.id.show_wind_speed_options_imageView)
        showLocationOptionImageView = view.findViewById(R.id.show_location_options_imageView)
        showNotificationOptionImageView =
            view.findViewById(R.id.show_notification_options_imageView)
    }

    private fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        // val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)//locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        editor.putString("CURRENT_LANGUAGE", language).commit()
        (requireActivity() as MainActivity).restartFragment(this)

    }


}