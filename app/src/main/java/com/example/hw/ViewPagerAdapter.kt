package com.example.hw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_page.view.*

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>() {
    private val images = intArrayOf(
        R.drawable.photo1,
        R.drawable.photo2,
        R.drawable.photo3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_page,
            parent, false)
        return ViewPagerHolder(v)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int){
        holder.bind(images[position])
    }

    inner class ViewPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(image: Int) {
            itemView.mainImage.setImageResource(image)
        }
    }
}