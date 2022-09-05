package com.reddevx.quizin.ui.fragments.app_settings

import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.repositories.AuthRepository
import com.reddevx.quizin.listeners.SuccessListener
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.utils.UserValidation

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