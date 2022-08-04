package com.example.myquizapp.data.models

import java.io.Serializable

data class GamePack(
    val questions:ArrayList<Question>,
    val quizTime:Int
) : Serializable
