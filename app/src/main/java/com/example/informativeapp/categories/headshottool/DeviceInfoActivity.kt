package com.example.informativeapp.categories.headshottool

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import com.example.informativeapp.MainActivity
import com.example.informativeapp.R
import com.example.informativeapp.bannerAds.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.button.MaterialButton

class DeviceInfoActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog
    private var inter: String = "null"
    var mInterstitialAd: InterstitialAd? = null
    private var backPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)
        var actionBar = supportActionBar

        actionBar?.title = "DEVICE INFO"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!
        progressDialog = Dialog(this).apply {
            val inflate = LayoutInflater.from(this@DeviceInfoActivity).inflate(R.layout.dialog_loading, null)
            setContentView(inflate)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val intent = Intent(this, SensitivityActivity::class.java)

        val btnContinue = findViewById<MaterialButton>(R.id.btnContinue1)
        btnContinue.setOnClickListener {
            MainActivity.ad_count++
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    progressDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressDialog.dismiss()
                        startActivity(intent)
                    },2500)
                }
            }else{
                progressDialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                    startActivity(intent)
                },2500)
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
                            val intent = Intent(this@DeviceInfoActivity, SensitivityActivity::class.java)
                            progressDialog.show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog.dismiss()
                                startActivity(intent)
                            },2500)
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