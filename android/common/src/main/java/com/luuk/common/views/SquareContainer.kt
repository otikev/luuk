package com.luuk.common.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by kevin on 31/05/17.
 * Modified by Isaac
 */
class SquareContainer : LinearLayout {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredHeight)
    }
}