package com.tabjin.dahh.model

import android.content.Context
import com.tabjin.dahh.bean.Contact

interface IContact {
    fun getContact(context: Context,data:String):ArrayList<Contact>
}