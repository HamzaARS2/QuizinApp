package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.User

interface UpdateUserListener {
    fun onUserUpdatedSuccessfully(user: User)
    fun onUpdatingUserFailed(e:Exception)
}