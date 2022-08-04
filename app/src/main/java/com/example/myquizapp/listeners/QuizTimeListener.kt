package com.example.myquizapp.listeners

interface QuizTimeListener {
    fun onProgressChanged(progress: Int,progressMax:Int)
    fun onProgressFinished()
}
