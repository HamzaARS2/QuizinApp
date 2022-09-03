package com.example.myquizapp.ui.fragments.shop.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myquizapp.R

class ShopItemsAdapter(private val items:ArrayList<String>) : RecyclerView.Adapter<ShopItemsAdapter.ShopItemsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemsHolder {
       return ShopItemsHolder(
           LayoutInflater
               .from(parent.context)
               .inflate(R.layout.shop_item,parent,false)
       )
    }

    override fun onBindViewHolder(holder: ShopItemsHolder, position: Int) {

    }

    override fun getItemCount(): Int = 6

    inner class ShopItemsHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    }
}