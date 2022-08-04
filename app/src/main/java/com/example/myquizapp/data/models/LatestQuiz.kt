package com.example.myquizapp.data.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class LatestQuiz(
    val id:String = "",
    val name:String = "",
    val category: Category = Category(),
    @ServerTimestamp val timestamp: Date? = null
) : Serializable
