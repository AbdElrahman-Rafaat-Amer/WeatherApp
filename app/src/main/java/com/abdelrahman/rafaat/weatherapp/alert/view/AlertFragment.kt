package com.abdelrahman.rafaat.weatherapp.alert.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModel
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.databinding.AlertLayoutBinding
import com.abdelrahman.rafaat.weatherapp.databinding.FragmentAlertBinding
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.abdelrahman.rafaat.weatherapp.reminder.NotificationAlert
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

private const val TAG = "AlertFragment"

class AlertFragment : Fragment(), OnAlertDeleteClickListener {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var viewModel: AlertViewModel

    private var alarmStartYear: Int = 0
    private var alarmStartMonth: Int = 0
    private var alarmStartDay: Int = 0
    private var alarmStartHour: Int = 0
    private var alarmStartMinute: Int = 0
    private var initialDelay: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initViewModel()
        observeViewModel()
    }

    private fun initUI() {
        alertAdapter = AlertAdapter(this)
        binding.recyclerViewAlerts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAlerts.adapter = alertAdapter
        binding.addAlertsFloatingActionButton.setOnClickListener {
            showDialog()
        }
    }

    private fun initViewModel() {
        val viewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource(requireContext())
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory)[AlertViewModel::class.java]

        viewModel.getStoredAlerts()
    }

    private fun observeViewModel() {
        viewModel.alertResponse.observe(viewLifecycleOwner) {
            Log.i(TAG, "onViewCreated: alertResponse----------> " + it.size)
            if (!it.isNullOrEmpty() || it.isNotEmpty()) {
                binding.noAlertsImageView.visibility = GONE
                binding.noAlertsTextView.visibility = GONE
                alertAdapter.setList(it)
            } else {
                Log.i(TAG, "onViewCreated: alertResponse----> isNullOrEmpty")
                Log.i(TAG, "observe: false---> $it")
                alertAdapter.setList(emptyList())
                binding.noAlertsImageView.visibility = View.VISIBLE
                binding.noAlertsTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun showDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .create()
        val bindingAlert: AlertLayoutBinding =
            AlertLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        bindingAlert.startDateLinerLayout.setOnClickListener {
            Log.i(TAG, "startLiner: ")
            showDateDialogPicker(bindingAlert.alertStartDate, bindingAlert.alertStartHour, true)
        }

        bindingAlert.endDateLinerLayout.setOnClickListener {
            Log.i(TAG, "endLinear: ")
            showDateDialogPicker(bindingAlert.alertEndDate, bindingAlert.alertEndHour, false)
        }

        alertDialogBuilder.setView(bindingAlert.root)
        bindingAlert.alertSaveButton.setOnClickListener {
            Log.i(TAG, "addAlertButton: ")


            initialDelay = checkDates()
            Log.i(TAG, "showDialog: initialDelay----> $initialDelay")
            if (initialDelay > 0) {
                updateRoom(
                    bindingAlert.alertStartDate.text.toString(),
                    bindingAlert.alertEndDate.text.toString(),
                    bindingAlert.alertStartHour.text.toString(),
                    bindingAlert.alertEndHour.text.toString()
                )
                setupAlertRequest()
                alertDialogBuilder.dismiss()
                viewModel.getStoredAlerts()
            } else {
                showSankBar(bindingAlert.root)
            }


        }
        alertDialogBuilder.setCanceledOnTouchOutside(false)
        alertDialogBuilder.show()
    }

    private fun showDateDialogPicker(date: TextView, time: TextView, isStart: Boolean) {
        Log.i(TAG, "showDateDialogPicker: ")
        val calender = Calendar.getInstance()
        val day: Int = calender.get(Calendar.DAY_OF_MONTH)
        val month: Int = calender.get(Calendar.MONTH)
        val year: Int = calender.get(Calendar.YEAR)

        DatePickerDialog(
            requireContext(),
            { _, year, monthIndex, dayIndex ->
                Log.i(TAG, "DatePickerDialog: ")
                val month = monthIndex + 1
                if (isStart) {
                    alarmStartYear = year
                    alarmStartMonth = month
                    alarmStartDay = dayIndex
                    Log.i(
                        TAG,
                        "showDateDialogPicker: start Time$alarmStartYear/$alarmStartMonth/$alarmStartDay"
                    )

                }
                date.text = formatDate("$dayIndex/$month/$year")
                showTimeDialogPicker(time, isStart)
            }, year, month, day
        ).show()
    }

    private fun formatDate(stringDate: String): String {
        Log.i(TAG, "formatDate: stringDate---------------------------> $stringDate")
        val stringFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

        return dateFormat.format(stringFormat.parse(stringDate))
    }

    private fun showTimeDialogPicker(time: TextView, isStart: Boolean) {
        Log.i(TAG, "showTimeDialogPicker: ")
        val calender = Calendar.getInstance()
        val hour: Int = calender.get(Calendar.HOUR)
        val minute: Int = calender.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _: TimePicker, hour: Int, minute: Int ->
            Log.i(TAG, "TimePickerDialog: ")
            if (isStart) {
                alarmStartHour = hour
                alarmStartMinute = minute
            }
            if (hour >= 12)
                time.text =
                    DecimalFormat("#").format(hour) + ":" + DecimalFormat("#").format(minute) + requireContext().getString(
                        R.string.PM
                    )
            else
                time.text =
                    DecimalFormat("#").format(hour) + ":" + DecimalFormat("#").format(minute) + requireContext().getString(
                        R.string.AM
                    )
        }, hour, minute, false).show()
    }

    private fun calculateDifferenceBetweenDates(startDate: String, endDate: String): Long {
        val dates = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val difference: Long =
            abs(dates.parse(startDate).time - dates.parse(endDate).time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        Log.i(TAG, "calculateDifferenceBetweenDates: dayDifference >>>>>> $differenceDates")
        return differenceDates
    }

    private fun checkDates(): Long {
        val dates = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())

        val currentTime = Calendar.getInstance().timeInMillis
        Log.i(TAG, "checkDates: currentTime-----> $currentTime")

        val startTime =
            dates.parse("$alarmStartYear/$alarmStartMonth/$alarmStartDay $alarmStartHour:$alarmStartMinute")
                .time
        Log.i(TAG, "checkDates: startTime-----> $startTime")

        val initialDelay: Long = startTime - currentTime
        Log.i(TAG, "calculateDifferenceBetweenDates: initialDelay >>>>>> $initialDelay")

        return initialDelay

    }

    private fun updateRoom(startDate: String, endDate: String, startHour: String, endHour: String) {
        val repetitions =
            calculateDifferenceBetweenDates(startDate, endDate)
        viewModel.insertAlert(
            SavedAlerts(
                startDate,
                endDate,
                startHour,
                endHour,
                repetitions
            )
        )

    }

    private fun setupAlertRequest() {
        val reminderRequest = OneTimeWorkRequest.Builder(
            NotificationAlert::class.java
        ).setInitialDelay(initialDelay, TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance(requireContext()).enqueue(reminderRequest)

    }

    private fun showSankBar(rootView: View) {
        val snackBar = Snackbar.make(
            rootView,
            getString(R.string.alert_time_error),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)
        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }

    override fun delete(id: Int) {
        viewModel.deleteAlertFromRoom(id)
        viewModel.getStoredAlerts()
    }
}