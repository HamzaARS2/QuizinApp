package com.example.myquizapp.data.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Category(
    val id:String = "",
    val name:String = "",
    val description:String = "",
    val categoryLogo:String = "",
    val itemBg:String = "",
    val layoutBg:String = "",
    @ServerTimestamp val timestamp:Date? = null
) : Serializable
