package com.example.myquizapp.listeners

interface AnswerListener {

    fun onCorrectAnswerSelected()
    fun onWrongAnswerSelected()
    fun onNoAnswerSelected()
}