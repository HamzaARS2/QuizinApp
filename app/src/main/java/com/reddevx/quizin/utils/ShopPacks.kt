package com.reddevx.quizin.utils

import com.reddevx.quizin.R

data class GoldPack(
    val goldPrice: Int = 1,
    val goldAmount: Int = 25,
    val imageRes: Int = R.drawable.shop_gold_pack_lvl3
)

data class GemPack(
    val gemPrice: Float = 2.99F,
    val gemAmount: Int = 500,
    val imageRes: Int = R.drawable.shop_gems_pack_lvl1
)

fun getGoldPacks(): List<GoldPack> {
    return arrayListOf(
        GoldPack(600, 10000, R.drawable.shop_coins_pack_lvl2),
        GoldPack(150, 2500, R.drawable.shop_coins_pack_lvl2),
        GoldPack(1400, 25000)
    )
}

fun getGemPacks(): List<GemPack> {
    return arrayListOf(
        GemPack(),
        GemPack(7.99F, 1000, R.drawable.shop_gems_pack_lvl2),
        GemPack(14.99F, 1600, R.drawable.shop_gems_pack_lvl3)
    )
}