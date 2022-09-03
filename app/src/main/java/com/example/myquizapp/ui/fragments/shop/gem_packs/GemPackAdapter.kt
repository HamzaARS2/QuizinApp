package com.example.myquizapp.ui.fragments.shop.gem_packs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myquizapp.R
import com.example.myquizapp.utils.GemPack

class GemPackAdapter(private val gemPacks:List<GemPack>) :
    RecyclerView.Adapter<GemPackAdapter.GemPackHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GemPackHolder {
        return GemPackHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.shop_gems_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: GemPackHolder, position: Int) {
        val gemPack = gemPacks[position]
        holder.setPackData(gemPack)
    }

    override fun getItemCount(): Int = gemPacks.size

    inner class GemPackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gemImage: ImageView = itemView.findViewById(R.id.shop_gems_image)
        private val gemAmountTv: TextView = itemView.findViewById(R.id.shop_gems_amount_tv)
        private val gemPackBtn: Button = itemView.findViewById(R.id.shop_gems_price_btn)

        @SuppressLint("SetTextI18n")
        fun setPackData(gemPack: GemPack) {
            gemImage.setImageResource(gemPack.imageRes)
            gemAmountTv.text = gemPack.gemAmount.toString()
            gemPackBtn.text = gemPack.gemPrice.toString() + "$"
        }
    }
}