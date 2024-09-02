package com.abdelrahman.raafat.climateClue.ui.timetable

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.CellTimeTableBinding
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder


class TimeTableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var items: List<TimeTableItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
        context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = CellTimeTableBinding.inflate(layoutInflater, parent, false)
        return TimeTableViewHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as? BaseViewHolder<Any>
        viewHolder?.bind(position, items[position])

        if (position == 0){
            viewHolder?.itemView?.updateLayoutParams<MarginLayoutParams> {
                topMargin = context.resources.getDimensionPixelSize(R.dimen.horizontal_space)
            }
        }
    }


    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(days: List<TimeTableItem>) {
        this.items = days
        notifyDataSetChanged()
    }

    inner class TimeTableViewHolder(private val binding: CellTimeTableBinding) :
        BaseViewHolder<TimeTableItem.DayItem>(binding.root) {
        private val dayAdapter = DayAdapter()
        override fun bind(position: Int, item: TimeTableItem.DayItem) {
            initUI()
            binding.dayNameTextView.text = item.dayName
            dayAdapter.setData(item.dayInfo)
        }

        private fun initUI() {
            val spanCount = 3
            binding.dayInfoRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
            binding.dayInfoRecyclerView.adapter = dayAdapter
        }
    }
}