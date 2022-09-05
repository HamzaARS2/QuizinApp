package com.reddevx.quizin.ui.fragments.quiz_result

import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.models.UserQuizInfo
import com.reddevx.quizin.utils.BAD_RESULT
import com.reddevx.quizin.utils.GOOD_RESULT
import com.reddevx.quizin.utils.GREAT_RESULT
import com.reddevx.quizin.utils.QuizOverAll

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