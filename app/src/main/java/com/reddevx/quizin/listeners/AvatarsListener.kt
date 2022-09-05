package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.Avatar

interface AvatarsListener {
    fun onAvatarsReceivedSuccessfully(avatar: Avatar)
    fun onReceivingAvatarsFailed(e:Exception)
}