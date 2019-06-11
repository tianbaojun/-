package com.tabjin.dahh.model

import java.lang.IllegalArgumentException

class ContentProviderFactory {
    private constructor()
    private object Holder{
        val instance = ContentProviderFactory()
    }
    companion object {
        val instance = Holder.instance
    }

    fun getProvider(type:Int):IContent{
        when(type){
            1->{
                return PrefixConbian()
            }
        }
        throw IllegalArgumentException("请传入正确的类型")
    }
}