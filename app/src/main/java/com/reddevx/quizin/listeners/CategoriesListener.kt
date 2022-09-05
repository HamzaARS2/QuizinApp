package com.reddevx.quizin.listeners

import com.reddevx.quizin.data.models.Category

interface CategoriesListener {
    fun onRetrievingCategoriesCompleted(categories:ArrayList<Category>)
    fun onRetrievingCategoriesFailed(e:Exception)
}