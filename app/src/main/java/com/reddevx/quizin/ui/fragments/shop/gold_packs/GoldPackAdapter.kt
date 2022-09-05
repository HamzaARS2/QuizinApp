package com.reddevx.quizin.ui.fragments.shop.gold_packs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.quizin.R
import com.reddevx.quizin.utils.GoldPack

class GoldPackAdapter(private val goldPacks: List<GoldPack>, val mListener:GoldClickListener) :
    RecyclerView.Adapter<GoldPackAdapter.GoldPackHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoldPackAdapter.GoldPackHolder {
        return GoldPackHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.shop_gold_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: GoldPackAdapter.GoldPackHolder, position: Int) {
        val goldPack = goldPacks[position]
        holder.setItemData(goldPack)
    }

    override fun getItemCount(): Int = goldPacks.size

    inner class GoldPackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val goldImage: ImageView = itemView.findViewById(R.id.shop_gold_image)
        private val goldAmountTv: TextView = itemView.findViewById(R.id.shop_gold_amount_tv)
        private val goldPriceBtn: Button = itemView.findViewById(R.id.shop_gold_price_btn)

        init {
            goldPriceBtn.setOnClickListener {
                mListener.onGoldPackClick(goldPacks[adapterPosition])
            }
        }

        fun setItemData(goldPack: GoldPack) {
            goldImage.setImageResource(goldPack.imageRes)
            goldAmountTv.text = goldPack.goldAmount.toString()
            goldPriceBtn.text = goldPack.goldPrice.toString()
        }
    }

    interface GoldClickListener {
        fun onGoldPackClick(goldPack: GoldPack)
    }
}