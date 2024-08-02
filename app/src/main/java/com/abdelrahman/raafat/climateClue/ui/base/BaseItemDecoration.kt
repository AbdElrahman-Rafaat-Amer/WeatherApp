package com.abdelrahman.raafat.climateClue.ui.base

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.utils.LocaleHelper

open class BaseItemDecoration : RecyclerView.ItemDecoration() {

    private var mParent: RecyclerView? = null
    private var mView: View? = null
    private var mOutRect: Rect? = null

    protected val position: Int
        get() {
            return mParent?.getChildAdapterPosition(mView ?: return -1) ?: -1
        }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        mParent = parent
        mView = view
        mOutRect = outRect
    }

    protected fun setLeft(space: Int) {
        if (LocaleHelper.isRTL()) {
            mOutRect?.right = space
        } else {
            mOutRect?.left = space
        }
    }

    protected fun setRight(space: Int) {
        if (LocaleHelper.isRTL()) {
            mOutRect?.left = space
        } else {
            mOutRect?.right = space
        }
    }

    protected fun isFirstItem(): Boolean {
        return mParent?.getChildAdapterPosition(mView ?: return false) == 0
    }

    protected fun isLastItem(): Boolean {
        val adapter = mParent?.adapter ?: return false
        return mParent?.getChildAdapterPosition(mView ?: return false) == adapter.itemCount - 1
    }
}