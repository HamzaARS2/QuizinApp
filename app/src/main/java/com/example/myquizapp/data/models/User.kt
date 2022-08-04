package com.example.myquizapp.data.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class User(
    val id:String = "",
    var username:String = "",
    var email:String = "",
    var avatar:String = "",
    var level:Int = 1,
    var badge:String = "Beginner",
    var exp:Int = 1,
    var trophies:Int = 0,
    var correct:Int = 0,
    var wrong:Int = 0,
    @ServerTimestamp val timestamp:Date? = null,
) : Serializable