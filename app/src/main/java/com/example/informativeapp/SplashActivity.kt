package com.example.informativeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowInsets
import android.view.WindowManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.informativeapp.bannerAds.Constants
import org.json.JSONException

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    var context = this
    private var requestQueue: RequestQueue? = null
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreference  = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        requestQueue = Volley.newRequestQueue(this)

        jsonParse()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun jsonParse() {
        val url = Constants.Ad_List
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
            Log.d("CLEAR","status ${response.getString("gg_banner1")}")
            if (response.getBoolean("status")){
                sharedPreference.edit().apply {
                    putString(Constants.BANNER1,response.getString("gg_banner1"))
                    putString(Constants.BANNER2,response.getString("gg_banner2"))
                    putString(Constants.INTER1,response.getString("gg_inter1"))
                    putString(Constants.INTER2,response.getString("gg_inter2"))
                    apply()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }
}