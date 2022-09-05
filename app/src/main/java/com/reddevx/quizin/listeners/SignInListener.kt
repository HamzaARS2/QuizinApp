package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.User

interface SignInListener {
    fun onUserSignedInSuccessfully(user: User)
    fun onUserSigningFailed(e:Exception?)
}