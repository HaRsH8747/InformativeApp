package com.example.informativeapp.categories.headshottool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import com.example.informativeapp.MainActivity
import com.example.informativeapp.R
import com.example.informativeapp.bannerAds.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class SensitivityActivity : AppCompatActivity() {

    private var inter: String = "null"
    var mInterstitialAd: InterstitialAd? = null
    private var backPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sensitivity)
        var actionBar = supportActionBar
        actionBar?.title = "SENSITIVITY"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!

        val sbGeneral = findViewById<AppCompatSeekBar>(R.id.sbGeneral)
        val tvGeneral = findViewById<MaterialTextView>(R.id.tvGeneral)
        setSeekBar(sbGeneral, tvGeneral)

        val sbRedDot = findViewById<AppCompatSeekBar>(R.id.sb_red_dot)
        val tvRedDot = findViewById<MaterialTextView>(R.id.tv_red_dot)
        setSeekBar(sbRedDot, tvRedDot)

        val sb2xScope = findViewById<AppCompatSeekBar>(R.id.sb_2x_scope)
        val tv2xScope = findViewById<MaterialTextView>(R.id.tv_2x_scope)
        setSeekBar(sb2xScope, tv2xScope)

        val sb4xScope = findViewById<AppCompatSeekBar>(R.id.sb_4x_scope)
        val tv4xScope = findViewById<MaterialTextView>(R.id.tv_4x_scope)
        setSeekBar(sb4xScope, tv4xScope)

        val sbAwmScope = findViewById<AppCompatSeekBar>(R.id.sb_awm_scope)
        val tvAwmScope = findViewById<MaterialTextView>(R.id.tv_awm_scope)
        setSeekBar(sbAwmScope, tvAwmScope)

        val btnContinue = findViewById<MaterialButton>(R.id.btnContinue2)
        btnContinue.setOnClickListener {
            MainActivity.ad_count++
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }
            }
        }
    }

    private fun setSeekBar(seekBar: AppCompatSeekBar, textView: MaterialTextView) {
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("DEBUG","Progress: ${seekBar!!.progress}")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("DEBUG","Progress: ${seekBar!!.progress}")
            }
        })
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