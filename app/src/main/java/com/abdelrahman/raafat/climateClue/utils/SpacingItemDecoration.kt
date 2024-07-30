package com.abdelrahman.raafat.climateClue.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.base.BaseItemDecoration
import com.abdelrahman.raafat.climateClue.homeplaces.viewholders.DayInfoHomeViewHolder

class SpacingItemDecoration(
    private val verticalSpaceHeight: Int,
    private val horizontalSpaceWidth: Int,
    private val includeFirstItem: Boolean = true,
    private val spanCount: Int = 0
) : BaseItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        with(outRect) {
            top = verticalSpaceHeight
            bottom = verticalSpaceHeight
            if (parent.getChildViewHolder(view) is DayInfoHomeViewHolder) {
                val column = position % spanCount // item column
                // Check if the item is at the left or right edge
                when (column) {
                    0 -> { // Item is in the last column (right edge)
                        setLeft(horizontalSpaceWidth/4)
                        setRight(horizontalSpaceWidth)
                    }
                    spanCount - 1 -> { //Item is in the middle
                        setLeft(horizontalSpaceWidth/4)
                        setRight(horizontalSpaceWidth/4)
                    }
                    else -> { // Item is in the first column (left edge)
                        setLeft(horizontalSpaceWidth)
                        setRight(horizontalSpaceWidth/4)
                    }
                }
            } else {
                setRight(horizontalSpaceWidth)
                if (includeFirstItem) {
                    setLeft(horizontalSpaceWidth)
                } else {
                    if (position != 0) {
                        setLeft(horizontalSpaceWidth)
                    }
                }
            }

            if (isFirstItem()) {
                top = verticalSpaceHeight // Add extra space for the first item
            }
            if (isLastItem()) {
                bottom = verticalSpaceHeight // Add extra space for the last item
            }
        }
    }
}
