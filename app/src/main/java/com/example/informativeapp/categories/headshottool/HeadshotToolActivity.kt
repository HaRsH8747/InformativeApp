package com.example.informativeapp.categories.headshottool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.informativeapp.MainActivity
import com.example.informativeapp.R
import com.example.informativeapp.bannerAds.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.button.MaterialButton

class HeadshotToolActivity : AppCompatActivity() {

    private var inter: String = "null"
    var mInterstitialAd: InterstitialAd? = null
    private var backPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headshot_tool)
        var actionBar = supportActionBar
        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!
        actionBar?.title = "HEADSHOT TOOL"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val btnSmartphone = findViewById<MaterialButton>(R.id.btn_smart_phone)
        btnSmartphone.setOnClickListener {
            MainActivity.ad_count++
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this, DeviceInfoActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, DeviceInfoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                backPressed = true
                MainActivity.ad_count++
                if(mInterstitialAd!=null){
                    if (MainActivity.ad_count % 3 == 0) {
                        mInterstitialAd!!.show(this)
                    }else{
                        backPressed = false
                        finish()
                    }
                }else{
                    backPressed = false
                    finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
                        if(backPressed) {
                            backPressed = false
                            finish()
                        }else {
                            val intent = Intent(this@HeadshotToolActivity, DeviceInfoActivity::class.java)
                            startActivity(intent)
                        }
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