package com.example.myquizapp.ui.fragments.avatars

import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.repositories.Repository
import com.example.myquizapp.listeners.AvatarsListener
import com.example.myquizapp.listeners.SuccessListener

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