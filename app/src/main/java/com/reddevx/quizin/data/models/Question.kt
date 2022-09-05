package com.reddevx.quizin.data.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Question(
    var id:String = "",
    var question:String = "",
    var difficulty:String = "easy",
    var answer1:String = "",
    var answer2:String = "",
    var answer3:String = "",
    var answer4:String = "",
    var correctAnswer:String = "",
    @ServerTimestamp val timestamp: Date? = null
)  : Serializable

