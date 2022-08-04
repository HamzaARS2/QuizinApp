package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.User
import java.lang.Exception

interface UserListener {
    fun onUserCreatedSuccessfully(user: User)
    fun onUserCreationFailed(e:Exception?)
}