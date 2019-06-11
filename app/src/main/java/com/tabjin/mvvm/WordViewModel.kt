package com.tabjin.mvvm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class WordViewModel: AndroidViewModel {

    var mReposity:WordReposity? = null

    var mAllWords:LiveData<List<Word>>? = null

    constructor(application: Application) : super(application) {
        mReposity = WordReposity(application)
        mAllWords = mReposity!!.mAllWords
    }

    fun insert(word:Word){
        mReposity!!.insert(word)
    }

}