package com.abdelrahman.rafaat.weatherapp.alert.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import java.util.*


class AlertAdapter(private var onDeleteAlert: OnAlertDeleteClickListener) :
    RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    private var alerts: List<SavedAlerts> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_alert, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertAdapter.ViewHolder, position: Int) {
        val alert = alerts[position]
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
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var startTime: TextView = itemView.findViewById(R.id.start_time_alert)
        var endTime: TextView = itemView.findViewById(R.id.end_time_alert)
        var startDate: TextView = itemView.findViewById(R.id.start_date_alert)
        var endDate: TextView = itemView.findViewById(R.id.end_date_alert)
        var deleteAlert: ImageView = itemView.findViewById(R.id.delete_alert)
    }


}