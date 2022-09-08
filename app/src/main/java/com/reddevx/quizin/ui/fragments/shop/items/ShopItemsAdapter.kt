package com.reddevx.quizin.ui.fragments.shop.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.ShopItem

class ShopItemsAdapter(private val items:List<ShopItem>, val mListener:ItemListener) : RecyclerView.Adapter<ShopItemsAdapter.ShopItemsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemsHolder {
       return ShopItemsHolder(
           LayoutInflater
               .from(parent.context)
               .inflate(R.layout.shop_item,parent,false)
       )
    }

    override fun onBindViewHolder(holder: ShopItemsHolder, position: Int) {
        val item = items[position]
        holder.setItemInfo(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ShopItemsHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        private val questionMark: CardView = itemView.findViewById(R.id.shop_item_question_mark)
        private val itemImage: ImageView = itemView.findViewById(R.id.shop_item_image)
        private val buyBtn: Button = itemView.findViewById(R.id.shop_item_buy_btn)

        init {
            questionMark.setOnClickListener {
                mListener.onQuestionMarkClick(items[adapterPosition])
            }
            buyBtn.setOnClickListener {
                mListener.onBuyButtonClick(items[adapterPosition])
            }
        }

        fun setItemInfo(item: ShopItem) {
            itemImage.setImageResource(item.resImage)
            buyBtn.text = item.price.toString()
        }
    }

}

interface ItemListener {
    fun onQuestionMarkClick(item:ShopItem)
    fun onBuyButtonClick(item: ShopItem)
}