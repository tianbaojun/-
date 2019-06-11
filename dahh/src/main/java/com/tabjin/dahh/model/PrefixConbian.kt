package com.tabjin.dahh.model

import com.tabjin.dahh.bean.Contact

class PrefixConbian:IContent {
    override fun combianContent(content: String,contact: Contact): String {
        if(contact.contactName.length<2){
            return ""
        }
        return contact.contactName.substring(1,2)+content
    }
}