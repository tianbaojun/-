package com.tabjin.dahh.model

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.tabjin.dahh.bean.Contact

class SMSsendModel {
    private object Holder{
        val holder = SMSsendModel()
    }

    companion object {
        val instance = Holder.holder
    }

    private val TAG = "SMSsendModel"
    val SENT_SMS_ACTION = "SENT_SMS_ACTION"
    val DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"
    fun createTask(context:Context, content: String, phone: String,index:Int,contacts:List<Contact>,delay:Int):Runnable{
        return Runnable {
            Log.e(TAG,"sleep $delay")
            Thread.sleep(delay*1000L)
            Log.e(TAG,"$index    sendSMS()")
            val smsManager = SmsManager.getDefault()
            // 拆分短信内容（手机短信长度限制）
            if(content.length > 70){
                val divideContents = smsManager.divideMessage(content)
                val sendList = ArrayList<PendingIntent>()
                val delieveList = ArrayList<PendingIntent>()
                var needinsert = true
                contacts[index].perNum = divideContents.size
                for (text in divideContents) {
                    val sentIntent = Intent(SENT_SMS_ACTION)
                    sentIntent.putExtra("index", index)
                    sentIntent.putExtra("needInsert",needinsert)
                    needinsert = false
                    val deliverIntent = Intent(DELIVERED_SMS_ACTION)
                    deliverIntent.putExtra("index", index)
                    val deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0)
                    val sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0)
                    sendList.add(sentPI)
                    delieveList.add(deliverPI)
                }
                //发送短信
                smsManager.sendMultipartTextMessage(phone, null, divideContents,sendList , delieveList)
                Log.e(TAG,"createTask $index    sendMultipartTextMessage()")
            }
            else {
                val divideContents = smsManager.divideMessage(content)
                contacts[index].perNum = divideContents.size
                for (text in divideContents) {
                    //不超过70字时使用sendTextMessage发送
                    val sentIntent = Intent(SENT_SMS_ACTION)
                    sentIntent.putExtra("index", index)
                    val deliverIntent = Intent(DELIVERED_SMS_ACTION)
                    deliverIntent.putExtra("index", index)
                    val deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0)
                    val sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0)
                    smsManager.sendTextMessage(phone, null, text, sentPI, deliverPI)
                    Log.e(TAG, "createTask  $index    sendTextMessage()")
                }
            }
        }
    }

    var continueNum = 0

    val restNum = 10

    /**
     * 提交任务
     */
    fun submitTask(context:Context, content: String, index:Int,contacts:List<Contact>){

        var delay = 5
        if(continueNum >= restNum) {
            delay = 60
            continueNum = 0
        }
        ThreadHelper.instance.addTask(SMSsendModel.instance.createTask(context,content,
                contacts[index].contactPhone, index, contacts,delay))
        continueNum++
    }
}