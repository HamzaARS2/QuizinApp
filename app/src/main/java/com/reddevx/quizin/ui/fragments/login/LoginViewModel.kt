package com.reddevx.quizin.ui.fragments.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.reddevx.quizin.data.repositories.AuthRepository
import com.reddevx.quizin.listeners.SignInListener
import com.reddevx.quizin.listeners.SuccessListener
import com.reddevx.quizin.utils.UserValidation

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