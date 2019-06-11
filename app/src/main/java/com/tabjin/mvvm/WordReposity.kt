package com.tabjin.mvvm

import android.app.Application
import android.os.AsyncTask

class WordReposity(application: Application){
    var db : WordRoomDatabase = WordRoomDatabase.getDatabase(application)
    var mWordDao = db.wordDao()
    var mAllWords = mWordDao.getAllWord()

    fun insert(word:Word){
        InsertWordTask(mWordDao).execute(word)
    }

    class InsertWordTask:AsyncTask<Word,Void,Void>{

        var mAsyncTaskDao:WordDao? = null

        constructor(dao:WordDao){
            mAsyncTaskDao = dao
        }

        override fun doInBackground(vararg params: Word?): Void {
            mAsyncTaskDao!!.insert(params[0]!!)
            null!!
        }

    }
}