package com.reddevx.quizin.ui.fragments.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.repositories.AuthRepository
import com.reddevx.quizin.data.repositories.Repository
import com.reddevx.quizin.listeners.CategoriesListener
import com.reddevx.quizin.listeners.LatestQuizzesListener

open class ExploreViewModel(
    private val authRepo: AuthRepository = AuthRepository(),
    private val repository: Repository = Repository()
) : ViewModel() {


    private val mutableUserData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = mutableUserData

    fun setUserData(user: User) {
        mutableUserData.postValue(user)
    }




    fun getCategories(mListener: CategoriesListener) {
        repository.getCategories(mListener)
    }

    fun getLatestQuizzes(mListener: LatestQuizzesListener) {
        repository.getLatestQuizzes(mListener)
    }


}