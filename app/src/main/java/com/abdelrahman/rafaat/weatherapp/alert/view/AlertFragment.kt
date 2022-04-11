package com.abdelrahman.rafaat.weatherapp.alert.view


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModel
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.abdelrahman.rafaat.weatherapp.reminder.NotificationAlert
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AlertFragment : Fragment(), OnAlertDeleteClickListener {

    private val TAG = "AlertFragment"
    private lateinit var noAlertTextView: TextView
    private lateinit var noAlertImageView: ImageView
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var addAlertFloatingActionButton: FloatingActionButton
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var viewModelFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel
    private lateinit var startHour: TextView
    private lateinit var startDate: TextView
    private lateinit var endHour: TextView
    private lateinit var endDate: TextView
    private var alarmStartYear: Int = 0
    private var alarmStartMonth: Int = 0
    private var alarmStartDay: Int = 0
    private var alarmStartHour: Int = 0
    private var alarmStartMinute: Int = 0
    private var initialDelay: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noAlertTextView = view.findViewById(R.id.no_alerts_textView)
        noAlertImageView = view.findViewById(R.id.no_alerts_imageView)

        alertRecyclerView = view.findViewById(R.id.recyclerView_alerts)
        addAlertFloatingActionButton = view.findViewById(R.id.add_alerts_floatingActionButton)
        alertAdapter = AlertAdapter(view.context, this)
        alertRecyclerView.layoutManager = LinearLayoutManager(view.context)
        alertRecyclerView.adapter = alertAdapter

        viewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource(view.context)
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AlertViewModel::class.java)

        viewModel.getStoredAlerts()
        viewModel.alertResponse.observe(viewLifecycleOwner, {
            Log.i(TAG, "onViewCreated: alertResponse----------> " + it.size)
            if (!it.isNullOrEmpty() || it.isNotEmpty()) {
                noAlertImageView.visibility = GONE
                noAlertTextView.visibility = GONE
                alertAdapter.setList(it)
                alertAdapter.notifyDataSetChanged()
            } else {
                Log.i(TAG, "onViewCreated: alertResponse----> isNullOrEmpty")
                Log.i(TAG, "observe: false---> $it")
                alertAdapter.setList(emptyList())
                alertAdapter.notifyDataSetChanged()
                noAlertImageView.visibility = View.VISIBLE
                noAlertTextView.visibility = View.VISIBLE
            }
        })

        addAlertFloatingActionButton.setOnClickListener {
            showDialog()
        }
    }

    override fun delete(id: Int) {
        viewModel.deleteAlertFromRoom(id)
        viewModel.getStoredAlerts()
    }

    private fun showDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .create()
        val alertView = layoutInflater.inflate(R.layout.alert_layout, null)

        val saveButton: Button = alertView.findViewById(R.id.alert_save_button)
        startHour = alertView.findViewById(R.id.alert_start_hour)
        startDate = alertView.findViewById(R.id.alert_start_date)
        endHour = alertView.findViewById(R.id.alert_end_hour)
        endDate = alertView.findViewById(R.id.alert_end_date)
        val startLiner: LinearLayout = alertView.findViewById(R.id.start_date_linerLayout)
        val endLinear: LinearLayout = alertView.findViewById(R.id.end_date_linerLayout)

        startLiner.setOnClickListener {
            Log.i(TAG, "startLiner: ")
            showDateDialogPicker(startDate, startHour, true)
        }

        endLinear.setOnClickListener {
            Log.i(TAG, "endLinear: ")
            showDateDialogPicker(endDate, endHour, false)
        }

        alertDialogBuilder.setView(alertView)
        saveButton.setOnClickListener {
            Log.i(TAG, "addAlertButton: ")


            initialDelay = checkDates()
            Log.i(TAG, "showDialog: initialDelay----> " + initialDelay)
            if (initialDelay > 0) {
                updateRoom()
                setupAlertRequest()
                alertDialogBuilder.dismiss()
                viewModel.getStoredAlerts()
            } else {
                showSankBar(alertView)
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
            { datePicker, year, monthIndex, dayIndex ->
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

        TimePickerDialog(requireContext(), { timePicker: TimePicker, hour: Int, minute: Int ->
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
            Math.abs(dates.parse(startDate).time - dates.parse(endDate).time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        Log.i(TAG, "calculateDifferenceBetweenDates: dayDifference >>>>>> $differenceDates")
        return differenceDates
    }


    private fun checkDates(): Long {
        val dates = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())

        var currentTime = Calendar.getInstance().timeInMillis
        Log.i(TAG, "checkDates: currentTime-----> $currentTime")

        // var startDate = dates.parse("$alarmStartYear/$alarmStartMonth/$alarmStartDay $alarmStartHour:$alarmStartMinute").getTime()
        var startTime =
            dates.parse("$alarmStartYear/$alarmStartMonth/$alarmStartDay $alarmStartHour:$alarmStartMinute")
                .time
        Log.i(TAG, "checkDates: startTime-----> $startTime")

        val initialDelay: Long = startTime - currentTime
        Log.i(TAG, "calculateDifferenceBetweenDates: initialDelay >>>>>> $initialDelay")

        return initialDelay

    }

    private fun updateRoom() {
        val repetitions =
            calculateDifferenceBetweenDates(startDate.text.toString(), endDate.text.toString())
        viewModel.insertAlert(
            SavedAlerts(
                startDate.text.toString(),
                endDate.text.toString(),
                startHour.text.toString(),
                endHour.text.toString(),
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

    private fun showSankBar(alertView: View) {
        var snackBar = Snackbar.make(
            alertView.findViewById(R.id.alertLayout_ConstraintLayout),
            getString(R.string.alert_time_error),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)
        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }

}