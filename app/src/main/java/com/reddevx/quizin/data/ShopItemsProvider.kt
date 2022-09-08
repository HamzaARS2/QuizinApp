package com.reddevx.quizin.data

import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.ShopItem


fun getShopItems() : List<ShopItem> {
    return listOf(
        ShopItem(0,"Hammer", R.drawable.quiz_item_tr,"Removes two wrong answers from a single question",2500),
        ShopItem(1,"Magnet", R.drawable.quiz_item_tr,"Add 30 seconds to the current question time",2000),
        ShopItem(2,"Sword", R.drawable.quiz_item_tr,"Skip question",3250),
    )
}