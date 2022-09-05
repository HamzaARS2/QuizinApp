package com.reddevx.quizin.data.models

import java.io.Serializable

data class GamePack(
    val questions:ArrayList<Question>,
    val quizTime:Int
) : Serializable
