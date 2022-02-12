package com.example.informativeapp.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.informativeapp.InformationAdapter
import com.example.informativeapp.MainActivity
import com.example.informativeapp.R
import com.example.informativeapp.bannerAds.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class CharactersActivity : AppCompatActivity() {

    private var inter: String = "null"
    var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters)
        var actionBar = supportActionBar
        actionBar?.title = "CHARACTERS"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!
        val titleArray = resources.getStringArray(R.array.characters_title)
        val descriptionArray = resources.getStringArray(R.array.characters_description)
        val images = mapOf<String,List<Int>>(
            titleArray[0] to listOf(R.drawable.d_bee),
            titleArray[1] to listOf(R.drawable.maro),
            titleArray[2] to listOf(R.drawable.xayne),
            titleArray[3] to listOf(R.drawable.skyler),
            titleArray[4] to listOf(R.drawable.chrono)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rvInformation)
        val informationAdapter = InformationAdapter(this, this, titleArray, descriptionArray, images)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = informationAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                MainActivity.ad_count++
                if(mInterstitialAd!=null){
                    if (MainActivity.ad_count % 3 == 0) {
                        mInterstitialAd!!.show(this)
                    }else{
                        finish()
                    }
                }else{
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
                        finish()
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