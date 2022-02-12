package com.example.informativeapp.categories

import android.app.Dialog
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

class WinDiamondActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog
    private var inter: String = "null"
    var mInterstitialAd: InterstitialAd? = null
    private var backPressed = false
    private var btnNumber = 0
    lateinit var btnUnlock1: MaterialButton
    lateinit var btnUnlock2: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win_diamond)
        var actionBar = supportActionBar

        actionBar?.title = "WIN DIAMOND"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!
        progressDialog = Dialog(this).apply {
            val inflate = LayoutInflater.from(this@WinDiamondActivity).inflate(R.layout.dialog_loading, null)
            setContentView(inflate)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        btnUnlock1 = findViewById<MaterialButton>(R.id.btnUnlock1)
        btnUnlock1.setOnClickListener {
            MainActivity.ad_count++
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    btnNumber = 1
                    mInterstitialAd!!.show(this)
                }else{
                    progressDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressDialog.dismiss()
                        btnUnlock1.text = "PARTICIPATE NOW"
                    },2500)
                }
            }else{
                progressDialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                    btnUnlock1.text = "PARTICIPATE NOW"
                },2500)
            }
        }

        btnUnlock2 = findViewById<MaterialButton>(R.id.btnUnlock2)
        btnUnlock2.setOnClickListener {
            MainActivity.ad_count++
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    btnNumber = 2
                    mInterstitialAd!!.show(this)
                }else{
                    progressDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressDialog.dismiss()
                        btnUnlock2.text = "PARTICIPATE NOW"
                    },2500)
                }
            }else{
                progressDialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                    btnUnlock2.text = "PARTICIPATE NOW"
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
                        if (backPressed){
                            backPressed = false
                            finish()
                        }
                        if(btnNumber == 1){
                            progressDialog.show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog.dismiss()
                                btnUnlock1.text = "PARTICIPATE NOW"
                            },2500)
                        }
                        if (btnNumber == 2){
                            progressDialog.show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog.dismiss()
                                btnUnlock2.text = "PARTICIPATE NOW"
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