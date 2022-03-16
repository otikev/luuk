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
class SimpleItemView : LinearLayout {
    private var tvDescription: TextView? = null
    private var ivImage: ImageView? = null

    constructor(context: Context) : super(context) {
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeViews(context)
        context.theme.obtainStyledAttributes(attrs, R.styleable.SimpleItemView, 0, 0).apply {

            try {
                val image = getResourceId(R.styleable.SimpleItemView_image, 0)
                ivImage?.setImageResource(image)
                tvDescription?.text = getString(R.styleable.SimpleItemView_title)
            } finally {
                recycle()
            }
        }
    }

    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.simple_item_view, this, true)
        ivImage = findViewById(R.id.image_view)
        tvDescription = findViewById(R.id.tv_description)

    }


}