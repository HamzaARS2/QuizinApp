package com.example.myquizapp.listeners

interface QuestionRoundListener {
    fun onRoundStarted(round:Int)
    fun onRoundFinished(finishedRound:Int,nextRound:Int)
}