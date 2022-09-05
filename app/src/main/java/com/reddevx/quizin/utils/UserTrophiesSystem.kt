package com.reddevx.quizin.utils

import com.reddevx.quizin.data.models.User

class UserTrophiesSystem(
    private val user: User = User(),
    private val questionMaxTime: Int,
    private var elapsedTime: Int,
    private val quizDifficulty: String,
) {

    companion object {
        const val EASY_QUIZ_DIFFICULTY = "easy"
        const val NORMAL_QUIZ_DIFFICULTY = "normal"
        const val HARD_QUIZ_DIFFICULTY = "hard"
    }

    private var totalCollectedTrophies = 0

    fun getUpdatedTrophiesUser(): User {
        user.trophies = getCollectedTrophies()
        return user
    }

    fun getCollectedTrophies(): Int {
        totalCollectedTrophies = 0
        totalCollectedTrophies += calculateTrophiesByDifficulty()
        totalCollectedTrophies += calculateRemainingTimeBonus()
        totalCollectedTrophies += calculateQuestionTimeBonus()
        return totalCollectedTrophies
    }

    fun calculateTrophiesByDifficulty(): Int = when (quizDifficulty) {
        EASY_QUIZ_DIFFICULTY -> 2
        NORMAL_QUIZ_DIFFICULTY -> 4
        // Hard difficulty
        else -> 7
    }


    fun calculateRemainingTimeBonus(): Int {
        val percentage = (elapsedTime / questionMaxTime.toFloat()) * 100
        return when {
            percentage >= 67 -> 3
            percentage >= 34 -> 2
            else -> 1
        }
    }


    fun calculateQuestionTimeBonus(): Int = when (questionMaxTime) {
        LONG_QUESTION_TIME -> 2
        NORMAL_QUESTION_TIME -> 4
        else -> 7
    }


}