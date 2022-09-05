package com.reddevx.quizin.ui.fragments.leaderboard

import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.repositories.Repository
import com.reddevx.quizin.listeners.RankedUsersListener

class LeaderboardViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    fun getRankedUsers(mListener: RankedUsersListener) {
        repository.getLeaderboardUsers(mListener)
    }


}