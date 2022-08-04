package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.Question

interface QuestionsListener {

    fun onQuestionsRetrievedSuccessfully(questions:ArrayList<Question>)
    fun onRetrievingQuestionsFailed(e:Exception)
}