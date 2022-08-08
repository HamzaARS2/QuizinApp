package com.example.myquizapp.ui.fragments.app_settings

import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.models.User
import com.example.myquizapp.data.repositories.AuthRepository
import com.example.myquizapp.listeners.SuccessListener
import com.example.myquizapp.listeners.UpdateUserListener
import com.example.myquizapp.utils.UserValidation

class SettingsViewModel(
    private val authRepository: AuthRepository = AuthRepository(),

) : ViewModel() {

    fun updateUser(user: User, mListener:UpdateUserListener) {
        authRepository.updateUserInfo(user, mListener)
    }

    fun deleteUserAccount(uid: String,mListener: SuccessListener) {
        authRepository.deleteUser(uid, mListener)
    }

    fun validateUpdatedUserInfo(username:String,email:String) : UserValidation.UpdateUserResponse {
        return UserValidation().updateUserValidation(username, email)
    }

    fun signOutCurrentUser() {
        authRepository.signOut()
    }
}