package com.reddevx.quizin.data.models

import com.reddevx.quizin.R

data class ShopItem(
    val id: Int = -1,
    val name: String = "",
    val resImage: Int = R.drawable.quiz_item_tr,
    val description: String = "",
    val price: Int = 100
)
