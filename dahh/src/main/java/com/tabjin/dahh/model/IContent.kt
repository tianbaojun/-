package com.tabjin.dahh.model

import com.tabjin.dahh.bean.Contact

interface IContent {
    fun combianContent(content:String,contact: Contact):String
}