package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.Question

interface QuestionsListener {

    fun onQuestionsRetrievedSuccessfully(questions:ArrayList<Question>)
    fun onRetrievingQuestionsFailed(e:Exception)
}