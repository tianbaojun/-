package com.tabjin.dahh.model

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony

class SMSHelper {
    companion object {
        fun setDefault(context: Context, name: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val smsIntent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                smsIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, name)
                context.startActivity(smsIntent)
            }
        }
    }
}