package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.User
import java.lang.Exception

interface UserListener {
    fun onUserCreatedSuccessfully(user: User)
    fun onUserCreationFailed(e:Exception?)
}