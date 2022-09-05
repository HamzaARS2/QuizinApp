package com.reddevx.quizin.listeners

interface SuccessListener {
    fun onSuccess(any: Any?)
    fun onFailure(e:Exception)
}