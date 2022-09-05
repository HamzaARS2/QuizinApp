package com.reddevx.quizin.ui.fragments.registration

import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.repositories.AuthRepository
import com.reddevx.quizin.listeners.UserListener
import com.reddevx.quizin.utils.UserValidation

class RegisterViewModel(
    private val authRepo: AuthRepository = AuthRepository()
) : ViewModel() {

    fun validateRegister(
        username: String,
        email: String,
        password: String
    ): UserValidation.RegisterResponse {
        return UserValidation().registerValidation(username, email, password)
    }

    fun registerUser(username: String, email: String, password: String, mListener: UserListener) {
        authRepo.createNewUser(username, email, password, mListener)
    }
}