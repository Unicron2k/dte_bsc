package com.shadyshrimp.quizztime.jsonlogic

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuestionViewModel(
        var mRepository : Repository, var mQuestionData : MutableLiveData<QuestionData> = mRepository.questionData,
        var mErrorMessage : MutableLiveData<String> = mRepository.errorMessage, var categoryArray : ArrayList<Category> = mRepository.categoryArray, var mCategoryData: MutableLiveData<CategoryData> = mRepository.mCategoryData) : ViewModel(){

    constructor() : this(mRepository = Repository())

    //starts download and saves to file
    fun startDownload(context : Context){
        mRepository.download(context)
    }

    //reads from questions file and converts to data
    fun questionsFromFile(context : Context){
        mRepository.jsonQuestionsFromFile(context)
    }

    //starts download of categories and saves to file
    fun downloadCategories(context : Context){
        mRepository.downloadCategories(context)
    }

    //reads from categories file and converts to usable array (categories)
    fun categoriesFromFile(context : Context){
        mRepository.jsonCategoriesFromFile(context)
    }
}