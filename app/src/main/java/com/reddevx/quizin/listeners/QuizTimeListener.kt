package com.reddevx.quizin.listeners

interface QuizTimeListener {
    fun onProgressChanged(progress: Int,progressMax:Int)
    fun onProgressFinished()
}
