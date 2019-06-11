package com.tabjin.dahh

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CrashReport.initCrashReport(this,BuildConfig.bugly_id,true)
    }
}