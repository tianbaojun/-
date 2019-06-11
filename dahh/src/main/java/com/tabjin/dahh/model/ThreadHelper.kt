package com.tabjin.dahh.model

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadHelper private constructor(){
    private object Holder{
        val holder = ThreadHelper()
    }

    companion object {
        val instance = Holder.holder
        val threadPool = ThreadPoolExecutor(5,10,200,TimeUnit.MILLISECONDS, ArrayBlockingQueue<Runnable>(5))
    }

    fun addInsertTask(context: Context, phone:String, content:String, type:Int){
        threadPool.execute{
            val ADDRESS = "address"
            val DATE = "date"
            val READ = "read"
            val STATUS = "status"
            val TYPE = "type"
            val BODY = "body"
            val values = ContentValues()
            /* 手机号 */
            values.put(ADDRESS, phone)
            /* 时间 */
            values.put(DATE, System.currentTimeMillis())
            /* 类型1为收件箱，2为发件箱 */
            values.put(TYPE, type)
            /* 短信体内容 */
            values.put(BODY, content)
            /* 插入数据库操作 */
            context.contentResolver.insert(Uri.parse("content://sms"),values)
        }
    }

    fun addTask(task:Runnable){
        threadPool.execute(task)
    }

}