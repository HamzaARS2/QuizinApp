package com.example.myquizapp.ui.fragments.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myquizapp.data.models.User
import com.example.myquizapp.data.repositories.AuthRepository
import com.example.myquizapp.data.repositories.Repository
import com.example.myquizapp.listeners.CategoriesListener
import com.example.myquizapp.listeners.LatestQuizzesListener

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