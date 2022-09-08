package com.reddevx.quizin.data.models

data class Inventory(
    val allItems: MutableList<ShopItem> = mutableListOf(),
    val inUseItems: MutableList<ShopItem> = mutableListOf()
)
