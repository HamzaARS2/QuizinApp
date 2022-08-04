package com.example.myquizapp.ui.fragments.registration

import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.repositories.AuthRepository
import com.example.myquizapp.listeners.UserListener
import com.example.myquizapp.utils.UserValidation

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