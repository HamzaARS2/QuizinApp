package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.User

interface RankedUsersListener {
    fun onRankedUsersReceivedSuccessfully(users:ArrayList<User>)
    fun onReceivingRankedUsersFailed(e:Exception)
}