package com.example.informativeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(
    val images: List<Int>
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        view.layoutParams = lp
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.image.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}