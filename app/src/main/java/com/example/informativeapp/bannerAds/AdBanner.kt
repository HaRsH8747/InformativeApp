package com.example.informativeapp.bannerAds

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import java.lang.Exception
import java.util.*

class AdBanner(val context: Context, val adsContainer: FrameLayout) {

//    var sharedPreferences = context.getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE)

    init {
        initAds()
    }

    private fun initAds() {
//        MobileAds.initialize(context)
        val adView = AdView(context)
        val min = 20
        val max = 80

        val random = Random().nextInt(max - min + 1) + min
        if (random % 2 == 0) {
            adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
//            Log.d("CLEAR","addd ${sharedPreferences.getString(Constants.BANNER1,"null")!!}")
            //            Toast.makeText(context, "Hello"+AppPref.getString(AppPref.BANNER1).toString(),Toast.LENGTH_SHORT).show();
        } else {
            adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
            //            Toast.makeText(context, "Hello"+AppPref.getString(AppPref.BANNER1).toString(),Toast.LENGTH_SHORT).show();
        }
        adView.adSize = AdSize.BANNER
        adsContainer.addView(adView)
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d("CLEAR","HEE ${p0.message}")

            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e("Banner Ad", "onAdLoaded")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}