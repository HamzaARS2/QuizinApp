package com.reddevx.quizin.utils

import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.UserQuizInfo

class QuizOverAll(
    private val userQuizInfo: UserQuizInfo
) {


    fun getQuizOverAll(): OverAllResultInfo {
        val value = ((userQuizInfo.corrects) / userQuizInfo.maxQuestions.toFloat()).times(100)
        val overAll = value.toInt().toString()
        val message = getOverAllMessage(value)
        val color = getOverAllColor(value)
        return OverAllResultInfo(overAll, message, color)
    }

    fun getTrophiesOverAll(): OverAllResultInfo {
        val maxTrophies = getMaxCollectableTrophies()
        val overAll = ((userQuizInfo.trophies) / maxTrophies.toFloat()).times(100)
        val overAllValue = overAll.toInt().toString()
        val message = getOverAllMessage(overAll)
        val color = getOverAllColor(overAll)
        return OverAllResultInfo(overAllValue, message, color)
    }

    fun getOverAllMessage(overAll: Float): String = when {
        overAll >= 67 -> GREAT_RESULT
        overAll >= 34 -> GOOD_RESULT
        else -> BAD_RESULT
    }

    private fun getOverAllColor(overAll: Float): Int = when {
        overAll >= 67 -> R.color.great_result
        overAll >= 34 -> R.color.good_result
        else -> R.color.bad_result
    }

    private fun getMaxCollectableTrophies(): Int {
        return getMaxDifficultyTrophies() + getMaxQuestionTimeTrophies()
    }

    private fun getMaxDifficultyTrophies(): Int {
        return getDifficultyTrophies().times(QUESTIONS_PER_QUIZ)
    }

    private fun getMaxQuestionTimeTrophies(): Int {
        return getQuestionTimeTrophies().times(QUESTIONS_PER_QUIZ)
    }


    private fun getDifficultyTrophies()
            : Int = when (userQuizInfo.difficulty) {
        "easy" -> 2
        "normal" -> 4
        else -> 7
    }

    private fun getQuestionTimeTrophies(): Int = when (userQuizInfo.questionTime) {
        LONG_QUESTION_TIME -> 2
        NORMAL_QUESTION_TIME -> 4
        else -> 7
    }

    data class OverAllResultInfo(
        val value: String,
        val message: String = "You have completed this quiz with GREAT result",
        val color: Int
    )

}