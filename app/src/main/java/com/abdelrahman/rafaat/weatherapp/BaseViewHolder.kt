package com.abdelrahman.rafaat.weatherapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

 abstract fun bind( position: Int, item: T)
}