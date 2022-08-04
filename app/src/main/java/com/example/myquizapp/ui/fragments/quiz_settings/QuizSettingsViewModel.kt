package com.example.myquizapp.ui.fragments.quiz_settings

import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.repositories.Repository
import com.example.myquizapp.listeners.QuestionsListener
import com.example.myquizapp.listeners.SuccessListener

class QuizSettingsViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    // Selected Quiz Difficulty
    var selectedDifficulty: String = "normal"

    // Selected Quiz Time
    var selectedQuizTime = 15

    fun getQuestionsByDifficulty(
        categoryId: String,
        quizId: String,
        difficulty: String,
        mListener: QuestionsListener
    ) {
        repository.getQuestionsByDifficulty(categoryId, quizId, difficulty, mListener)
    }

    fun getQuizById(categoryId: String, quizId: String, mListener: SuccessListener) {
        repository.getQuiz(categoryId, quizId, mListener)
    }

    fun resetQuizSettings() {
        selectedDifficulty = "normal"
        selectedQuizTime = 15
    }

}