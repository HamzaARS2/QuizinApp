package com.example.myquizapp.ui.fragments.category_quizzes

import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.repositories.Repository
import com.example.myquizapp.listeners.QuizzesListener

class CategoryQuizzesViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    fun getQuizzes(categoryId: String, mListener: QuizzesListener) {
        repository.getCategoryQuizzesById(categoryId, mListener)
    }

}