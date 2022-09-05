package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.Quiz

interface QuizzesListener {
    fun onRetrievingQuizzesCompleted(quizzes:ArrayList<Quiz>)
    fun onRetrievingQuizzesFailed(e:Exception)
}