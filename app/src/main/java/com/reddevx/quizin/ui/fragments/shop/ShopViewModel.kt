package com.reddevx.quizin.ui.fragments.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.repositories.Repository
import com.reddevx.quizin.listeners.UpdateUserListener

open class ShopViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val _shopGold:MutableLiveData<Int> = MutableLiveData()
    val shopGold:LiveData<Int> get() = _shopGold

    private val _shopGems: MutableLiveData<Int> = MutableLiveData()
    val shopGems: LiveData<Int> get() = _shopGems

    fun postGold(amount: Int){
        _shopGold.postValue(amount)
    }
    fun postGems(amount: Int) {
        _shopGems.postValue(amount)
    }

    fun saveUserData(user: User, mListener: UpdateUserListener) {
        repository.saveUserData(user, mListener)
    }
}