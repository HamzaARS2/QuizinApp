package com.reddevx.quizin.data.models

import java.io.Serializable

data class UserQuizInfo(
    val difficulty:String = "normal",
    val trophies:Int = 0,
    val corrects:Int = 0,
    val wrongs:Int = 0,
    val exp:Int = 0,
    val levelUp:Boolean = false,
    val questionMaxTime:Int = 0,
    val maxQuestions:Int = 0
) : Serializable
