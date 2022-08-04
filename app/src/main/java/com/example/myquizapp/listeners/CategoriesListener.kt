package com.example.myquizapp.listeners

import com.example.myquizapp.data.models.Category

interface CategoriesListener {
    fun onRetrievingCategoriesCompleted(categories:ArrayList<Category>)
    fun onRetrievingCategoriesFailed(e:Exception)
}