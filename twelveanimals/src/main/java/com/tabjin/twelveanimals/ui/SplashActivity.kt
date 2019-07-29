package com.tabjin.twelveanimals.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tabjin.twelveanimals.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() , View.OnClickListener{
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_animal ->{
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_diy ->{
                val intent = Intent(this@SplashActivity, MainActivity2::class.java)
                startActivity(intent)
            }
            R.id.btn_sicai ->{
                val intent = Intent(this@SplashActivity, MainActivity3::class.java)
                startActivity(intent)
            }
            R.id.btn_shengxiaodan ->{
                val intent = Intent(this@SplashActivity, MainActivity4::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        btn_animal.setOnClickListener(this)
        btn_diy.setOnClickListener(this)
        btn_sicai.setOnClickListener(this)
        btn_shengxiaodan.setOnClickListener(this)
    }
}
