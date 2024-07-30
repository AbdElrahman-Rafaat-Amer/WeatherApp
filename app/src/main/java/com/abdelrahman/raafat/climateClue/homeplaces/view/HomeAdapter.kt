package com.abdelrahman.raafat.climateClue.homeplaces.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.homeplaces.viewholders.DailyHomeViewHolder
import com.abdelrahman.raafat.climateClue.homeplaces.viewholders.DayInfoHomeViewHolder
import com.abdelrahman.raafat.climateClue.homeplaces.viewholders.HourlyHomeViewHolder
import com.abdelrahman.raafat.climateClue.homeplaces.viewholders.StatusHomeViewHolder
import com.abdelrahman.raafat.climateClue.homeplaces.viewholders.TitleHomeViewHolder

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<HomeItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return when (viewType) {
            HomeItem.TitleItem::class.java.hashCode() -> {
                TitleHomeViewHolder(
                    layoutInflater.inflate(
                        R.layout.cell_home_item,
                        parent,
                        false
                    )
                )
            }

            HomeItem.StatusItem::class.java.hashCode() -> {
                StatusHomeViewHolder(
                    layoutInflater.inflate(
                        R.layout.cell_home_status,
                        parent,
                        false
                    )
                )
            }

            HomeItem.HourlyItem::class.java.hashCode() -> {
                HourlyHomeViewHolder(
                    layoutInflater.inflate(
                        R.layout.cell_home_hourly,
                        parent,
                        false
                    )
                )
            }

            HomeItem.DailyItem::class.java.hashCode() -> {
                DailyHomeViewHolder(
                    layoutInflater.inflate(
                        R.layout.cell_home_daily,
                        parent,
                        false
                    )
                )
            }

            HomeItem.DayInfoItem::class.java.hashCode() -> {
                DayInfoHomeViewHolder(
                    layoutInflater.inflate(
                        R.layout.cell_home_day_info,
                        parent,
                        false
                    )
                )
            }

            else -> {
                TitleHomeViewHolder(
                    layoutInflater.inflate(
                        R.layout.cell_home_item,
                        parent,
                        false
                    )
                )
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

    fun setData(items: List<HomeItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}