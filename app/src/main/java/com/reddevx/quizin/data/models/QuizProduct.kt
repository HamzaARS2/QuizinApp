package com.reddevx.quizin.data.models

import com.android.billingclient.api.ProductDetails
import com.reddevx.quizin.R

data class QuizProduct(
    val productDetails: ProductDetails,
    val gemsAmount:Int = 500,
    val imageRes:Int = R.drawable.shop_gems_pack_lvl1
)
