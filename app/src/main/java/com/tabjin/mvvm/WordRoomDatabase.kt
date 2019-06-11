package com.tabjin.mvvm

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.AsyncTask

@Database(entities = [Word::class],version = 1)
abstract class WordRoomDatabase:RoomDatabase(){
    abstract fun  wordDao():WordDao

    companion object {

        fun getDatabase(context: Context): WordRoomDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        WordRoomDatabase::class.java, "word_table")
                        .build()
            }
            return instance!!
        }

        var instance:WordRoomDatabase? = null

        var roomCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopuplateDbAsync(instance!!).execute()
            }
        }

        class PopuplateDbAsync (db:WordRoomDatabase): AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void {
                mDao!!.deleteAll()
                var word1 = Word()
                word1.mWord = "hello"
                mDao!!.insert(word1)
                var word2 = Word()
                word2.mWord = "world"
                mDao!!.insert(word2)
                null!!
            }

            var mDao: WordDao? = null

            init {
                mDao = db.wordDao()
            }
        }
    }
}