package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.User

interface UpdateUserListener {
    fun onUserUpdatedSuccessfully(user: User)
    fun onUpdatingUserFailed(e:Exception)
}