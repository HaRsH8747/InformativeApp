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
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatToggleButton
import com.example.informativeapp.MainActivity
import com.example.informativeapp.R
import com.example.informativeapp.bannerAds.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.button.MaterialButton

class GFXToolActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog
    private var inter: String = "null"
    var mInterstitialAd: InterstitialAd? = null
    private var backPressed = false
    lateinit var btnApply: MaterialButton
    lateinit var tbCpu: AppCompatToggleButton
    lateinit var tbGpu: AppCompatToggleButton
    lateinit var tbRam: AppCompatToggleButton
    lateinit var tbSpeed: AppCompatToggleButton
    lateinit var spRes: AppCompatSpinner
    lateinit var spFps: AppCompatSpinner
    lateinit var spGraphics: AppCompatSpinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gfxtool)
        var actionBar = supportActionBar
        actionBar?.title = "GFX TOOL"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!
        progressDialog = Dialog(this).apply {
            val inflate = LayoutInflater.from(this@GFXToolActivity).inflate(R.layout.dialog_loading, null)
            setContentView(inflate)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        spRes = findViewById(R.id.sp_resolution)
        val adapter1 = ArrayAdapter.createFromResource(this, R.array.resolution, R.layout.spinner_item)
        spRes.adapter = adapter1

        spFps = findViewById(R.id.sp_fps)
        val adapter2 = ArrayAdapter.createFromResource(this, R.array.fps, R.layout.spinner_item)
        spFps.adapter = adapter2

        spGraphics = findViewById(R.id.sp_graphics)
        val adapter3 = ArrayAdapter.createFromResource(this, R.array.graphics, R.layout.spinner_item)
        spGraphics.adapter = adapter3

        btnApply = findViewById<MaterialButton>(R.id.btnApply)
        tbCpu = findViewById<AppCompatToggleButton>(R.id.tb_cpu)
        tbGpu = findViewById<AppCompatToggleButton>(R.id.tb_gpu)
        tbRam = findViewById<AppCompatToggleButton>(R.id.tb_ram)
        tbSpeed = findViewById<AppCompatToggleButton>(R.id.tb_speed)

        btnApply.setOnClickListener {
            if (btnApply.text == "APPLY"){
                MainActivity.ad_count++
                if (mInterstitialAd != null) {
                    if (MainActivity.ad_count % 3 == 0) {
                        mInterstitialAd!!.show(this)
                    } else {
                        resetSettings(btnApply, tbCpu, tbGpu, tbRam, tbSpeed, spRes, spFps, spGraphics)
                    }
                } else {
                    resetSettings(btnApply, tbCpu, tbGpu, tbRam, tbSpeed, spRes, spFps, spGraphics)
                }
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
                        }else{
                            resetSettings(btnApply, tbCpu, tbGpu, tbRam, tbSpeed, spRes, spFps, spGraphics)
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

    private fun resetSettings(
        btnApply: MaterialButton,
        tbCpu: AppCompatToggleButton,
        tbGpu: AppCompatToggleButton,
        tbRam: AppCompatToggleButton,
        tbSpeed: AppCompatToggleButton,
        spRes: AppCompatSpinner,
        spFps: AppCompatSpinner,
        spGraphics: AppCompatSpinner
    ) {
        progressDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            btnApply.text = "Start Game"
            tbCpu.isEnabled = false
            tbGpu.isEnabled = false
            tbRam.isEnabled = false
            tbSpeed.isEnabled = false
            spRes.isEnabled = false
            spFps.isEnabled = false
            spGraphics.isEnabled = false
        },2500)
    }
}