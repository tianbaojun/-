package com.tabjin.dahh.model

import java.lang.IllegalArgumentException

class ContactProviderFactory {
    private constructor()
    private object Holder{
        val instance = ContactProviderFactory()
    }
    companion object {
        val instance = Holder.instance
    }

    fun getProvider(type:Int):IContact{
        when(type){
            1->{
                return GroupContactModel()
            }
            2-> return PrefixContactModel()
        }
        throw IllegalArgumentException("请传入正确的类型")
    }
}