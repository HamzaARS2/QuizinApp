package com.example.myquizapp.utils

import android.util.Patterns

class UserValidation(


) {

    private object Constants {
        const val PASSWORD_REGEX =
            "^" +                   // start-of-string
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"                    //end-of-string
    }

    private fun isValidPasswordForm(password: String): Boolean {
        return Regex(Constants.PASSWORD_REGEX).matches(password)
    }

    private fun validateUsername(username: String): ValidationResponse {
        return when {
            username.isEmpty() -> ValidationResponse(false, "Please enter your username")
            username.contains(" ") -> ValidationResponse(
                false,
                "Username must not contains white space"
            )
            username.length < 5 -> ValidationResponse(false, "Username must be 5 chars at least")
            username.length > 16 -> ValidationResponse(false, "Username must be less than 16 chars")
            else -> ValidationResponse(true, null)
        }


    }

    private fun validatePassword(password: String): ValidationResponse {
        return if (password.isEmpty())
            ValidationResponse(false, "Please enter your password")
        else if (!isValidPasswordForm(password))
            ValidationResponse(false, "Password must be at least 8 chars and 1 digit")
        else
            ValidationResponse(true, null)

    }

    private fun validateEmail(email: String): ValidationResponse {
        return if (email.isEmpty())
            ValidationResponse(false, "Please enter your email")
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            ValidationResponse(false, "Please enter valid email")
        else
            ValidationResponse(true, null)
    }

    fun loginValidation(email: String, password: String): LoginResponse {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        return LoginResponse(
            emailValidation.isValid,
            passwordValidation.isValid,
            emailValidation.message,
            passwordValidation.message
        )
    }

    fun registerValidation(username: String, email: String, password: String): RegisterResponse {
        val usernameValidation = validateUsername(username)
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        return RegisterResponse(
            usernameValidation.isValid,
            emailValidation.isValid,
            passwordValidation.isValid,
            usernameValidation.message,
            emailValidation.message,
            passwordValidation.message
        )
    }

    fun updateUserValidation(username: String, email: String): UpdateUserResponse {
        val usernameValidation = validateUsername(username)
        val emailValidation = validateEmail(email)
        return UpdateUserResponse(
            usernameValidation.isValid,
            emailValidation.isValid,
            usernameValidation.message,
            emailValidation.message,
        )
    }


    data class LoginResponse(
        val isValidEmail: Boolean,
        val isValidPassword: Boolean,
        val emailMessage: String?,
        val passwordMessage: String?
    )

    data class RegisterResponse(
        val isValidUsername: Boolean,
        val isValidEmail: Boolean,
        val isValidPassword: Boolean,
        val usernameMessage: String?,
        val emailMessage: String?,
        val passwordMessage: String?
    )

    data class UpdateUserResponse(
        val isValidUsername: Boolean,
        val isValidEmail: Boolean,
        val usernameMessage: String?,
        val emailMessage: String?,
    )


}