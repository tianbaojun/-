package com.tabjin.twelveanimals.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tabjin.twelveanimals.R
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var resultNum = 0
        resultNum = intent.getIntExtra("resultNum",0)
        var conditionStr = intent.getStringExtra("condition")
        var resultStr = intent.getStringExtra("resultStr")

//        tv_result.movementMethod = ScrollingMovementMethod()
        tv_condition.text = conditionStr
        tv_result.text = resultStr
        tv_num.text = "共$resultNum 个结果"


        tv_copy.setOnClickListener {
            //获取剪贴板管理器：
            var cm:ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var mClipData = ClipData.newPlainText("Label", resultStr)
            cm.primaryClip = mClipData
            Toast.makeText(this@ResultActivity,"复制成功",Toast.LENGTH_SHORT).show()
        }

        iv_back.setOnClickListener{
            finish()
        }
    }
}
