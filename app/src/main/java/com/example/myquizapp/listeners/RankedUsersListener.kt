package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.User

interface RankedUsersListener {
    fun onRankedUsersReceivedSuccessfully(users:ArrayList<User>)
    fun onReceivingRankedUsersFailed(e:Exception)
}