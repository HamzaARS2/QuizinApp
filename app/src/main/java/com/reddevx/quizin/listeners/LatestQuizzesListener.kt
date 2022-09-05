package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.LatestQuiz

interface LatestQuizzesListener {
    fun onLatestQuizzesRetrievedSuccessfully(latestQuizzes:ArrayList<LatestQuiz>)
    fun onRetrievingLatestQuizzesFailed(e:Exception)
}