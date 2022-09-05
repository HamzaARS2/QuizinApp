package com.reddevx.quizin.data.models

data class UserShopData(
    val user: User = User(),
    val gemsAmount: Int =  0,
    val goldAmount: Int = 0
 )
