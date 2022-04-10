package com.abdelrahman.rafaat.weatherapp.alert.view


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
            Log.i(TAG, "addAlertButton: ")
            showDialog()
        }
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
        var startLiner: LinearLayout = alertView.findViewById(R.id.start_date_linerLayout)
        var endLinear: LinearLayout = alertView.findViewById(R.id.end_date_linerLayout)

        startLiner.setOnClickListener {
            Log.i(TAG, "startLiner: ")
            showDateDialogPicker(startDate, startHour)
        }

        endLinear.setOnClickListener {
            Log.i(TAG, "endLinear: ")
            showDateDialogPicker(endDate, endHour)
        }

        alertDialogBuilder.setView(alertView)
        saveButton.setOnClickListener {
            Log.i(TAG, "addAlertButton: ")
            var repetitions =
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
            alertDialogBuilder.dismiss()
            viewModel.getStoredAlerts()

            val reminderRequest = OneTimeWorkRequest.Builder(
                NotificationAlert::class.java
            ).setInitialDelay(4000, TimeUnit.MILLISECONDS).build()
            WorkManager.getInstance(requireContext()).enqueue(reminderRequest)
        }
        alertDialogBuilder.setCanceledOnTouchOutside(false)
        alertDialogBuilder.show()
    }

    private fun showDateDialogPicker(date: TextView, time: TextView) {
        Log.i(TAG, "showDateDialogPicker: ")
        val calender = Calendar.getInstance()
        val day: Int = calender.get(Calendar.DAY_OF_MONTH)
        val month: Int = calender.get(Calendar.MONTH)
        val year: Int = calender.get(Calendar.YEAR)

        DatePickerDialog(
            requireContext(),
            { datePicker, year, monthIndex, dayIndex ->
                Log.i(TAG, "DatePickerDialog: ")
                var month = monthIndex + 1
                date.text = formatDate("$dayIndex/$month/$year")
                showTimeDialogPicker(time)
            }, year, month, day
        ).show()
    }

    private fun formatDate(stringDate: String): String {
        val stringFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        return dateFormat.format(stringFormat.parse(stringDate))
    }

    private fun showTimeDialogPicker(time: TextView) {
        Log.i(TAG, "showTimeDialogPicker: ")
        val calender = Calendar.getInstance()
        val hour: Int = calender.get(Calendar.HOUR)
        val minute: Int = calender.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { timePicker: TimePicker, hour: Int, minute: Int ->
            Log.i(TAG, "TimePickerDialog: ")
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
            Math.abs(dates.parse(startDate).getTime() - dates.parse(endDate).getTime())
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        Log.i(TAG, "calculateDifferenceBetweenDates: dayDifference >>>>>> $differenceDates")
        return differenceDates
    }

    override fun delete(id: Int) {
        viewModel.deleteAlertFromRoom(id)
        viewModel.getStoredAlerts()
    }


}