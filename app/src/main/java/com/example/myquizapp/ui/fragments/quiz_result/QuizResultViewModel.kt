package com.example.myquizapp.ui.fragments.quiz_result

import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.models.UserQuizInfo
import com.example.myquizapp.utils.BAD_RESULT
import com.example.myquizapp.utils.GOOD_RESULT
import com.example.myquizapp.utils.GREAT_RESULT
import com.example.myquizapp.utils.QuizOverAll

class QuizResultViewModel(
) : ViewModel() {


    fun getQuizOverAll(userQuizInfo: UserQuizInfo): QuizOverAll.OverAllResultInfo {
        val quizOverAll = QuizOverAll(userQuizInfo)
        return quizOverAll.getQuizOverAll()
    }

    fun getOverAllResult(overAll: Float): String = when {
        overAll >= 67 -> GREAT_RESULT
        overAll >= 34 -> GOOD_RESULT
        else -> BAD_RESULT
    }


}