package com.example.myquizapp.ui.fragments.leaderboard

import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.repositories.Repository
import com.example.myquizapp.listeners.RankedUsersListener

class LeaderboardViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    fun getRankedUsers(mListener: RankedUsersListener) {
        repository.getLeaderboardUsers(mListener)
    }


}