package com.reddevx.quizin.ui.fragments.avatars

import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.repositories.Repository
import com.reddevx.quizin.listeners.AvatarsListener
import com.reddevx.quizin.listeners.SuccessListener

class AvatarsViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {


    fun getAvatars(mListener:AvatarsListener) {
        repository.getAvailableAvatars(mListener)
    }

    fun setUserAvatar(userId:String,avatar:String,mListener:SuccessListener) {
        repository.addUserAvatar(userId, avatar, mListener)
    }
}