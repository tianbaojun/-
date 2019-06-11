package com.tabjin.mvvm

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "word_table")
class Word constructor(){
    constructor(stringExtra: String?):this(){
        mWord = stringExtra!!
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="word")
    var mWord:String = ""
}