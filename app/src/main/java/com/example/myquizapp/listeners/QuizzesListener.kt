package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.Quiz

interface QuizzesListener {
    fun onRetrievingQuizzesCompleted(quizzes:ArrayList<Quiz>)
    fun onRetrievingQuizzesFailed(e:Exception)
}