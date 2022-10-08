package com.abdelrahman.rafaat.weatherapp.alert.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.databinding.CustomRowAlertBinding
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import java.util.*


class AlertAdapter(private var onDeleteAlert: OnAlertDeleteClickListener) :
    RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    private var alerts: List<SavedAlerts> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomRowAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertAdapter.ViewHolder, position: Int) {
        val alert = alerts[position]
        holder.bind(alert)
    }

    override fun getItemCount(): Int {
        return alerts.size
    }

    fun setList(alerts: List<SavedAlerts>) {
        this.alerts = alerts
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: CustomRowAlertBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alert: SavedAlerts) {
            binding.startTimeAlert.text = alert.startTime
            binding.startDateAlert.text = alert.startDate
            binding.endTimeAlert.text = alert.endTime
            binding.endDateAlert.text = alert.endDate
            binding.deleteAlert.setOnClickListener {
                onDeleteAlert.delete(alert.id!!, alert.tag)
            }
        }
    }


}