package com.abdelrahman.raafat.climateClue.ui.timetable

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.databinding.CellHomeDayInfoBinding
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder

class DayAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<TimeTableItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayInfoTimeTableViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = CellHomeDayInfoBinding.inflate(layoutInflater, parent, false)
        return DayInfoTimeTableViewHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as? BaseViewHolder<Any>
        viewHolder?.bind(position, items[position])
    }


    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(days: List<TimeTableItem>) {
        this.items = days
        notifyDataSetChanged()
    }

    class DayInfoTimeTableViewHolder(private val binding: CellHomeDayInfoBinding) :
        BaseViewHolder<TimeTableItem.DayInfoItem>(binding.root) {

        override fun bind(position: Int, item: TimeTableItem.DayInfoItem) {
            binding.statusDetailsWidget.setTimeTableDayInfo(item)
        }
    }
}