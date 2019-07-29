package com.tabjin.dialogdemo

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_dialog.setOnClickListener {
            showDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        showDialog()
    }

    fun showDialog() {
        var dialog = Dialog(this,R.style.mydialog)
        var dialogContent = View.inflate(this, R.layout.dialog_text, null)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        //需要设置window的gravity确定显示位置
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#90f6f600")))
        dialog.setContentView(dialogContent)
        dialogContent.findViewById<TextView>(R.id.tv_dialog).setOnClickListener {
            dialog.dismiss()
        }
    }
}
