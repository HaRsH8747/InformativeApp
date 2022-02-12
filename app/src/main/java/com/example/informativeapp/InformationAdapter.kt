package com.example.informativeapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.informativeapp.bannerAds.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InformationAdapter(
    val context: Context,
    val activity: Activity,
    val titleArray: Array<String>,
    val descriptionArray: Array<String>,
    val imageList: Map<String,List<Int>>

): RecyclerView.Adapter<InformationAdapter.InformationListViewHolder>() {

    private lateinit var dialog: Dialog
    private var isDialogClosed = false
    private var inter: String = context.getSharedPreferences(
        Constants.USER_PREFS,
        AppCompatActivity.MODE_PRIVATE
    ).getString(Constants.INTER1,"null")!!
    var mInterstitialAd: InterstitialAd? = null

    inner class InformationListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.ivImage)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val layoutId = itemView.findViewById<ConstraintLayout>(R.id.layoutId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.info_item,parent,false)
        return InformationListViewHolder(view)
    }

    override fun onBindViewHolder(holder: InformationListViewHolder, position: Int) {
        setInterstitialAd()
        holder.title.text = titleArray[position]
        holder.image.setImageResource(imageList[titleArray[position]]?.get(0)!!)

        dialog = Dialog(context)
        dialog.setContentView(R.layout.detail_dialog)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnClose = dialog.findViewById<FrameLayout>(R.id.btnClose)
        val vpimage = dialog.findViewById<ViewPager2>(R.id.vpImage)
        val slider = dialog.findViewById<LinearLayout>(R.id.slider)
        val title = dialog.findViewById<TextView>(R.id.tvTitle2)
        val description = dialog.findViewById<TextView>(R.id.tvDescription2)
        val imageAdapter = ImageAdapter(imageList[titleArray[position]]!!)

        vpimage.adapter = imageAdapter
        vpimage.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        if(imageAdapter.itemCount>1){
            val dots = mutableListOf<ImageView>()
            for (i in 0 until imageAdapter.itemCount){
                dots.add(ImageView(context))
                dots[i].setImageDrawable(ContextCompat.getDrawable(context.applicationContext, R.drawable.non_active_dot))

                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                params.setMargins(8,0,8,0)
                slider.addView(dots[i], params)
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(context.applicationContext, R.drawable.active_dot))

            vpimage.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    for (i in 0 until imageAdapter.itemCount){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(context.applicationContext, R.drawable.non_active_dot))
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(context.applicationContext, R.drawable.active_dot))
                }
            })
        }

        btnClose.setOnClickListener {
            MainActivity.ad_count++
            isDialogClosed = true
            if(mInterstitialAd!=null) {
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(activity)
                }else{
                    isDialogClosed = false
                    dialog.dismiss()
                }
            }else{
                isDialogClosed = false
                dialog.dismiss()
            }
        }
        Log.d("DEBUG","Title: ${titleArray[position]}")
        title.text = titleArray[position]
        description.text = descriptionArray[position]

        holder.layoutId.setOnClickListener {
            MainActivity.ad_count++
            if (mInterstitialAd != null) {
                if (MainActivity.ad_count % 3 == 0) {
                    mInterstitialAd!!.show(activity)
                } else {
                    dialog.show()
                }
            } else {
                dialog.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return titleArray.size
    }

    private fun setInterstitialAd(){
        val adRequest = AdRequest.Builder().build()

        MobileAds.initialize(context) {}
        val inter2 = context.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getString(Constants.INTER2,"null")!!
        InterstitialAd.load(context,inter, adRequest, object : InterstitialAdLoadCallback() {
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
                        if(isDialogClosed){
                            isDialogClosed = false
                            dialog.dismiss()
                        }else{
                            dialog.show()
                        }
                        setInterstitialAd()
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