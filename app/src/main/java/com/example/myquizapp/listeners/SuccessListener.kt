package com.example.myquizapp.listeners

interface SuccessListener {
    fun onSuccess(any: Any?)
    fun onFailure(e:Exception)
}