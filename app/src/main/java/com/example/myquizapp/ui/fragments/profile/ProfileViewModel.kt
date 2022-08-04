package com.example.myquizapp.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.models.User

class ProfileViewModel() : ViewModel() {

    private val userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = userMutableLiveData

    fun postUserData(user: User) {
        userMutableLiveData.postValue(user)
    }
}