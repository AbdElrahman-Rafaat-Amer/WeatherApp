package com.abdelrahman.rafaat.weatherapp.alert.view

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Hourly
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.Format
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AlertAdapter(context: Context, var onDeleteAlert: OnAlertDeleteClickListener) :
    RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    private val TAG = "AlertAdapter"
    private var context = context
    private var alerts: List<SavedAlerts> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_alert, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        var alert = alerts[position]
        holder.startTime.text = alert.startTime
        holder.startDate.text = alert.startDate
        holder.endTime.text = alert.endTime
        holder.endDate.text = alert.endDate
        holder.deleteAlert.setOnClickListener {
            onDeleteAlert.delete(alert.id!!)
        }

    }

    override fun getItemCount(): Int {
        return alerts.size
    }

    fun setList(alerts: List<SavedAlerts>) {
        this.alerts = alerts
        Log.i(TAG, "setList: after")
        Log.i(TAG, "setList: hours" + alerts.size)
        Log.i(TAG, "setList: this.hours " + this.alerts.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var startTime: TextView = itemView.findViewById(R.id.start_time_alert)
        var endTime: TextView = itemView.findViewById(R.id.end_time_alert)
        var startDate: TextView = itemView.findViewById(R.id.start_date_alert)
        var endDate: TextView = itemView.findViewById(R.id.end_date_alert)
        var deleteAlert: ImageView = itemView.findViewById(R.id.delete_alert)
    }


}