package com.abdelrahman.rafaat.weatherapp.alert.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModel
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.databinding.AlertLayoutBinding
import com.abdelrahman.rafaat.weatherapp.databinding.DialogLayoutBinding
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

private const val TAG = "AlertFragment"

class AlertFragment : Fragment(), OnAlertDeleteClickListener {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var viewModel: AlertViewModel
    private lateinit var alert: SavedAlerts
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
            if (!it.isNullOrEmpty() || it.isNotEmpty()) {
                binding.noAlertsImageView.visibility = GONE
                binding.noAlertsTextView.visibility = GONE
                binding.recyclerViewAlerts.visibility = VISIBLE
                alertAdapter.setList(it)
            } else {
                alertAdapter.setList(emptyList())
                binding.recyclerViewAlerts.visibility = GONE
                binding.noAlertsImageView.visibility = VISIBLE
                binding.noAlertsTextView.visibility = VISIBLE
            }
        }
    }

    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialog).create()
        val bindingAlert = AlertLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        alertDialogBuilder.setView(bindingAlert.root)

        bindingAlert.startDateLinerLayout.setOnClickListener {
            showDateDialogPicker(bindingAlert.alertStartDate, bindingAlert.alertStartHour, true)
        }

        bindingAlert.endDateLinerLayout.setOnClickListener {
            showDateDialogPicker(bindingAlert.alertEndDate, bindingAlert.alertEndHour, false)
        }

        bindingAlert.alertSaveButton.setOnClickListener {
            initialDelay = checkDates()
            val repetitions =
                calculateDifferenceBetweenDates(
                    bindingAlert.alertStartDate.text.toString(),
                    bindingAlert.alertEndDate.text.toString()
                )
            if (repetitions > -1) {
                if (initialDelay > 0) {
                    updateRoom(
                        bindingAlert.alertStartDate.text.toString(),
                        bindingAlert.alertEndDate.text.toString(),
                        bindingAlert.alertStartHour.text.toString(),
                        bindingAlert.alertEndHour.text.toString(), repetitions
                    )
                    setupAlertRequest()
                    alertDialogBuilder.dismiss()
                    viewModel.getStoredAlerts()
                } else {
                    showSankBar(bindingAlert.root, getString(R.string.alert_start_time_error))
                }
            } else {
                showSankBar(bindingAlert.root, getString(R.string.alert_end_time_error))
            }
        }
        alertDialogBuilder.setCanceledOnTouchOutside(false)
        alertDialogBuilder.show()
    }

    private fun showDateDialogPicker(date: TextView, time: TextView, isStart: Boolean) {
        val calender = Calendar.getInstance()
        val day: Int = calender.get(Calendar.DAY_OF_MONTH)
        val month: Int = calender.get(Calendar.MONTH)
        val year: Int = calender.get(Calendar.YEAR)

        DatePickerDialog(
            requireContext(),
            { _, yearIndex, monthIndex, dayIndex ->
                if (isStart) {
                    alarmStartYear = yearIndex
                    alarmStartMonth = monthIndex + 1
                    alarmStartDay = dayIndex
                }
                date.text = formatDate("$dayIndex/${monthIndex + 1}/$yearIndex")
                showTimeDialogPicker(time, isStart)
            }, year, month, day
        ).show()
    }

    private fun formatDate(stringDate: String): String {
        val stringFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        return dateFormat.format(stringFormat.parse(stringDate)!!)
    }

    private fun showTimeDialogPicker(time: TextView, isStart: Boolean) {
        val calender = Calendar.getInstance()
        val hour: Int = calender.get(Calendar.HOUR)
        val minute: Int = calender.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _: TimePicker, hourIndex: Int, minuteIndex: Int ->
            if (isStart) {
                alarmStartHour = hourIndex
                alarmStartMinute = minuteIndex
            }
            if (hourIndex >= 12)
                time.text =
                    DecimalFormat("#").format(hourIndex).plus(
                        ":" + DecimalFormat("#").format(
                            minuteIndex
                        ) + requireContext().getString(
                            R.string.PM
                        )
                    )
            else
                time.text =
                    DecimalFormat("#").format(hourIndex).plus(
                        ":" + DecimalFormat("#").format(
                            minuteIndex
                        ) + requireContext().getString(
                            R.string.AM
                        )
                    )
        }, hour, minute, false).show()
    }

    private fun checkDates(): Long {
        val dates = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val currentTime = Calendar.getInstance().timeInMillis
        val startTime =
            dates.parse("$alarmStartYear/$alarmStartMonth/$alarmStartDay $alarmStartHour:$alarmStartMinute")!!
                .time
        val initialDelay: Long = startTime - currentTime
        return initialDelay
    }

    private fun updateRoom(
        startDate: String,
        endDate: String,
        startHour: String,
        endHour: String,
        repetitions: Long
    ) {
        alert = SavedAlerts(
            startDate,
            endDate,
            startHour,
            endHour,
            repetitions,
            System.currentTimeMillis()
        )
        viewModel.insertAlert(alert)
    }

    private fun calculateDifferenceBetweenDates(startDate: String, endDate: String): Long {
        val dates = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val difference: Long =
            dates.parse(endDate)!!.time - dates.parse(startDate)!!.time
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        return differenceDates
    }

    private fun setupAlertRequest() {
        val reminderRequest = OneTimeWorkRequestBuilder<NotificationAlert>().setInitialDelay(
            initialDelay,
            TimeUnit.MILLISECONDS
        )
            .addTag(alert.tag.toString())
            .build()
        Log.i(TAG, "setupAlertRequest: tag-----------> ${alert.tag}")
        WorkManager.getInstance(requireContext()).enqueue(reminderRequest)
    }

    private fun showSankBar(rootView: View, message: String) {
        val snackBar = Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)
        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }

    override fun delete(id: Int, tag: Long) {
        showDialog(id, tag)
    }

    private fun showDialog(id: Int, tag: Long) {
        val binding = DialogLayoutBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        val displayRectangle = Rect()
        val window = requireActivity().window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setLayout(
            (displayRectangle.width() * 0.82f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.dialogMessageTextView.text = getString(R.string.delete_alert)
        binding.removeButton.setOnClickListener {
            viewModel.deleteAlertFromRoom(id)
            alertDialog.dismiss()
            Log.i(TAG, "showDialog: Tag------------$tag")
            WorkManager.getInstance(requireContext()).cancelAllWorkByTag(tag.toString())
        }
        binding.cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}