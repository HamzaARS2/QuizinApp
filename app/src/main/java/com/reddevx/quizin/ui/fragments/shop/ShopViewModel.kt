package com.reddevx.quizin.ui.fragments.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reddevx.quizin.data.models.ShopItem
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.models.UserShopData
import com.reddevx.quizin.data.repositories.Repository
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.utils.GoldPack

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

    fun hasEnoughGems(user: User,gemsAmount: Int) :Boolean {
        if (user.gems < gemsAmount)
            return false
        return true
    }

    fun hasEnoughGold(user: User,goldAmount: Int) : Boolean {
        if (user.gold < goldAmount)
            return false
        return true
    }

    fun buyGoldPack(user: User,goldPack: GoldPack, mListener: UpdateUserListener)  {
        user.gold += goldPack.goldAmount
        user.gems -= goldPack.goldPrice
        saveUserData(user,mListener)
    }

    fun buyShopItem(user: User,item: ShopItem,mListener: UpdateUserListener) : Boolean {
        if (user.inventory.allItems.size > 10)
            return true
        user.inventory.allItems.add(item)
        user.gold -= item.price
        saveUserData(user,mListener)
        return false
    }
}