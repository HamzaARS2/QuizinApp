package com.example.myquizapp.ui.fragments.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.example.myquizapp.data.repositories.AuthRepository
import com.example.myquizapp.listeners.SignInListener
import com.example.myquizapp.listeners.SuccessListener
import com.example.myquizapp.utils.UserValidation

class LoginViewModel(
    private val authRepo: AuthRepository = AuthRepository()
) : ViewModel() {

    fun validateLogin(email: String, password: String): UserValidation.LoginResponse {
        return UserValidation().loginValidation(email, password)
    }

    fun signInUser(email: String, password: String, mListener: SignInListener) {
        authRepo.loginUser(email, password, mListener)
    }

    fun getUserData(uid: String, mListener: SuccessListener) {
        authRepo.getCurrentUserData(uid, mListener)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepo.getSignedInUser()
    }

}