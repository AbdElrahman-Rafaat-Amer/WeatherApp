package com.abdelrahman.rafaat.weatherapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewholders.DayInfoHomeViewHolder

class SpacingItemDecoration(
    private val verticalSpaceHeight: Int,
    private val horizontalSpaceWidth: Int,
    private val includeFirstItem: Boolean = true,
    private val spanCount: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            val position = parent.getChildAdapterPosition(view) // item position


            top = verticalSpaceHeight
            bottom = verticalSpaceHeight
            if (parent.getChildViewHolder(view) is DayInfoHomeViewHolder) {
                val column = position % spanCount // item column
                // Check if the item is at the left or right edge
                when (column) {
                    0 -> { // Item is in the last column (right edge)
                        outRect.left = horizontalSpaceWidth/4
                        outRect.right = horizontalSpaceWidth
                    }
                    spanCount - 1 -> { //Item is in the middle
                        outRect.left = horizontalSpaceWidth/4
                        outRect.right = horizontalSpaceWidth/4
                    }
                    else -> { // Item is in the first column (left edge)
                        outRect.left = horizontalSpaceWidth
                        outRect.right = horizontalSpaceWidth/4
                    }
                }
            } else {
                right = horizontalSpaceWidth
                if (includeFirstItem) {
                    left = horizontalSpaceWidth
                } else {
                    if (position != 0) {
                        left = horizontalSpaceWidth
                    }
                }
            }

            if (position == 0) {
                top = verticalSpaceHeight // Add extra space for the first item
            }
            if (position == parent.adapter?.itemCount?.minus(1)) {
                bottom = verticalSpaceHeight // Add extra space for the last item
            }
        }
    }
}
