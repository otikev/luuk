package com.luuk.common.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.luuk.common.R

/**
 * Created by Isaac.
 */
class CustomToolBar : LinearLayout {
    private var tvTitle: TextView? = null
    private var ivImage: ImageView? = null

    constructor(context: Context) : super(context) {
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeViews(context)
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomToolBar, 0, 0).apply {
            try {
                tvTitle?.text = getString(R.styleable.CustomToolBar_toolbar_title)
            } finally {
                recycle()
            }
        }
    }

    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.custom_toolbar, this, true)
        ivImage = findViewById(R.id.image_view)
        tvTitle = findViewById(R.id.tv_title)
    }


}