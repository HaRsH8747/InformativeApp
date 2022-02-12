package com.example.informativeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.informativeapp.bannerAds.Constants
import com.example.informativeapp.categories.*
import com.example.informativeapp.categories.headshottool.HeadshotToolActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.button.MaterialButton

class CategoryActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    var mInterstitialAd: InterstitialAd? = null
    private var inter: String = "null"
    private var type = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        inter = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE).getString(Constants.INTER1,"null")!!
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btnWeapons = findViewById<MaterialButton>(R.id.btnWeapons)
        btnWeapons.setOnClickListener {
            MainActivity.ad_count++
            type = "weapons"
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this, WeaponsActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, WeaponsActivity::class.java)
                startActivity(intent)
            }
        }

        val btnVehicles = findViewById<MaterialButton>(R.id.btnVehicles)
        btnVehicles.setOnClickListener {
            MainActivity.ad_count++
            type = "vehicles"
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this, VehiclesActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, VehiclesActivity::class.java)
                startActivity(intent)
            }
        }

        val btnCharacters = findViewById<MaterialButton>(R.id.btnCharacters)
        btnCharacters.setOnClickListener {
            MainActivity.ad_count++
            type = "characters"
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this, CharactersActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, CharactersActivity::class.java)
                startActivity(intent)
            }
        }

        val btnGFXTool = findViewById<MaterialButton>(R.id.btnGfxTool)
        btnGFXTool.setOnClickListener {
            MainActivity.ad_count++
            type = "gfx"
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this, GFXToolActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, GFXToolActivity::class.java)
                startActivity(intent)
            }
        }

        val btnHeadshotTool = findViewById<MaterialButton>(R.id.btnHeadshotTool)
        btnHeadshotTool.setOnClickListener {
            MainActivity.ad_count++
            type = "headshot"
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this, HeadshotToolActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, HeadshotToolActivity::class.java)
                startActivity(intent)
            }
        }

        val btnWinDiamond = findViewById<MaterialButton>(R.id.btnWinDiamond)
        btnWinDiamond.setOnClickListener {
            MainActivity.ad_count++
            type = "diamond"
            if(mInterstitialAd!=null){
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(this)
                }else{
                    val intent = Intent(this, WinDiamondActivity::class.java)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, WinDiamondActivity::class.java)
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
                        when(type){
                            "weapons" -> {val intent = Intent(this@CategoryActivity, WeaponsActivity::class.java)
                                startActivity(intent)}
                            "vehicles" -> {val intent = Intent(this@CategoryActivity, VehiclesActivity::class.java)
                                startActivity(intent)}
                            "characters" -> {val intent = Intent(this@CategoryActivity, CharactersActivity::class.java)
                                startActivity(intent)}
                            "gfx" -> {val intent = Intent(this@CategoryActivity, GFXToolActivity::class.java)
                                startActivity(intent)}
                            "headshot" -> {val intent = Intent(this@CategoryActivity, HeadshotToolActivity::class.java)
                                startActivity(intent)}
                            "diamond" -> {val intent = Intent(this@CategoryActivity, WinDiamondActivity::class.java)
                                startActivity(intent)}
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return  true
        return super.onOptionsItemSelected(item)
    }
}