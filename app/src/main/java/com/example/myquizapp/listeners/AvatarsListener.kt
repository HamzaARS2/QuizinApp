package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.Avatar

interface AvatarsListener {
    fun onAvatarsReceivedSuccessfully(avatar: Avatar)
    fun onReceivingAvatarsFailed(e:Exception)
}