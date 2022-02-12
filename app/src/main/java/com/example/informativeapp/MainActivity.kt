package com.example.informativeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.informativeapp.bannerAds.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {

    companion object {
        var ad_count = 0
    }
    var mInterstitialAd: InterstitialAd? = null
    private var inter: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<ImageView>(R.id.btnStart)

        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!
        btnStart.setOnClickListener {
            ad_count++
            if(mInterstitialAd!=null){
                if (ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this@MainActivity, CategoryActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this@MainActivity, CategoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val adRequest = AdRequest.Builder().build()

        MobileAds.initialize(this) {}
        val inter2 = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER2,"null")!!
        InterstitialAd.load(this,inter, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("CLEAR", "hello ${adError.message}")
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("CLEAR", "Ad was loaded")
                mInterstitialAd = interstitialAd
                mInterstitialAd!!.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("CLEAR", "Ad was dismissed.")
                        val intent = Intent(this@MainActivity, CategoryActivity::class.java)
                        startActivity(intent)
                    }
                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        Log.d("CLEAR", "Ad failed to show.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("CLEAR", "Ad showed fullscreen content.")
                        mInterstitialAd = null
                    }
                }
            }
        })
    }
}