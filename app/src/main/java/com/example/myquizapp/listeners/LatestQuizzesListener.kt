package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.LatestQuiz

interface LatestQuizzesListener {
    fun onLatestQuizzesRetrievedSuccessfully(latestQuizzes:ArrayList<LatestQuiz>)
    fun onRetrievingLatestQuizzesFailed(e:Exception)
}