package com.abdelrahman.raafat.climateClue.ui.custom

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {

    init {
        marqueeRepeatLimit = -1
        isSelected = true
        isSingleLine = true
        ellipsize = TextUtils.TruncateAt.MARQUEE
        isFocusable = true
        isFocusableInTouchMode = true
        setHorizontallyScrolling(true)
    }
}