package com.reddevx.quizin.listeners

interface AnswerListener {

    fun onCorrectAnswerSelected()
    fun onWrongAnswerSelected()
    fun onNoAnswerSelected()
}