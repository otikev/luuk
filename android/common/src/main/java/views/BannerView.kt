package views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.luuk.common.R

/**
 * Created by Isaac.
 */
class BannerView : LinearLayout {
    private var ivColor: ImageView? = null
    private var ivIcon: ImageView? = null

    constructor(context: Context) : super(context) {
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeViews(context)
    }

    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.banner_view, this, true)
        ivColor = findViewById(R.id.image_view_banner_1)
        ivIcon = findViewById(R.id.image_view_banner_2)
    }

}