package com.example.informativeapp.bannerAds

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RequiresApi


class MyBannerAdsView : FrameLayout {
    constructor(context: Context) : super(context) {
        initBanner()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initBanner()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initBanner()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initBanner()
    }

    private fun initBanner() {
        AdBanner(context, this)
    }
}
