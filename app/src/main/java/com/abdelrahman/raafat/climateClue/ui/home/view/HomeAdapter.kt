package com.abdelrahman.raafat.climateClue.ui.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.databinding.CellHomeDailyBinding
import com.abdelrahman.raafat.climateClue.databinding.CellHomeDayInfoBinding
import com.abdelrahman.raafat.climateClue.databinding.CellHomeHourlyBinding
import com.abdelrahman.raafat.climateClue.databinding.CellHomeItemBinding
import com.abdelrahman.raafat.climateClue.databinding.CellHomeStatusBinding
import com.abdelrahman.raafat.climateClue.ui.home.viewholders.DailyHomeViewHolder
import com.abdelrahman.raafat.climateClue.ui.home.viewholders.DayInfoHomeViewHolder
import com.abdelrahman.raafat.climateClue.ui.home.viewholders.HourlyHomeViewHolder
import com.abdelrahman.raafat.climateClue.ui.home.viewholders.StatusHomeViewHolder
import com.abdelrahman.raafat.climateClue.ui.home.viewholders.TitleHomeViewHolder
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<HomeItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        when (viewType) {
            HomeItem.TitleItem::class.java.hashCode() -> {
                val binding = CellHomeItemBinding.inflate(layoutInflater, parent, false)
                return TitleHomeViewHolder(binding)
            }

            HomeItem.StatusItem::class.java.hashCode() -> {
                val binding = CellHomeStatusBinding.inflate(layoutInflater, parent, false)
                return StatusHomeViewHolder(binding)
            }

            HomeItem.HourlyItem::class.java.hashCode() -> {
                val binding = CellHomeHourlyBinding.inflate(layoutInflater, parent, false)
                return HourlyHomeViewHolder(binding)
            }

            HomeItem.DailyItem::class.java.hashCode() -> {
                val binding = CellHomeDailyBinding.inflate(layoutInflater, parent, false)
                return DailyHomeViewHolder(binding)
            }

            HomeItem.DayInfoItem::class.java.hashCode() -> {
                val binding = CellHomeDayInfoBinding.inflate(layoutInflater, parent, false)
                return DayInfoHomeViewHolder(binding)
            }

            else -> {
                val binding = CellHomeItemBinding.inflate(layoutInflater, parent, false)
                return TitleHomeViewHolder(binding)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as? BaseViewHolder<Any>
        viewHolder?.bind(position, items[position])
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemViewType(position: Int): Int = items[position]::class.java.hashCode()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<HomeItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}