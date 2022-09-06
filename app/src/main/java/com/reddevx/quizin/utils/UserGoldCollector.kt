package com.reddevx.quizin.utils

import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.models.UserQuizInfo
import kotlin.random.Random

class UserGoldCollector(
    private val user: User,
    private val difficulty: String = "normal",
    private val trophies: Int = 0,
    private val corrects: Int = 0,
    private val wrongs: Int = 0,
    private val questionMaxTime: Int = 0
) {

    fun getCollectedGold(): Int {
        val levelBonusGold = user.level.times(5)
        val randomBonusGold = Random.nextInt(1, 10)
        val trophiesBonusGold = trophies
        val correctsBonusGold = corrects
        val questionTimeBonusGold = calculateQuestionTimeBonus()
        val difficultyBonusGold  = calculateGoldByDifficulty()

        val wrongAnswers = wrongs

        val totalGoldCollected = (levelBonusGold +
                randomBonusGold +
                trophiesBonusGold +
                correctsBonusGold +
                questionTimeBonusGold +
                difficultyBonusGold ) - wrongAnswers
        if (totalGoldCollected > 0)
            return totalGoldCollected
        return 0;
    }

    private fun calculateQuestionTimeBonus(): Int = when (questionMaxTime) {
        LONG_QUESTION_TIME -> 2
        NORMAL_QUESTION_TIME -> 4
        else -> 7
    }

    private fun calculateGoldByDifficulty(): Int = when (difficulty) {
        UserTrophiesSystem.EASY_QUIZ_DIFFICULTY -> 2
        UserTrophiesSystem.NORMAL_QUIZ_DIFFICULTY -> 4
        // Hard difficulty
        else -> 7
    }
}