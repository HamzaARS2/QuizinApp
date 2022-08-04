package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.User

interface SignInListener {
    fun onUserSignedInSuccessfully(user: User)
    fun onUserSigningFailed(e:Exception?)
}