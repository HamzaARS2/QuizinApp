package com.reddevx.quizin.ui.fragments.category_quizzes

import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.repositories.Repository
import com.reddevx.quizin.listeners.QuizzesListener

class CategoryQuizzesViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    fun getQuizzes(categoryId: String, mListener: QuizzesListener) {
        repository.getCategoryQuizzesById(categoryId, mListener)
    }

}