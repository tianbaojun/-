package com.example.ex

import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addOnSoftKeyBoardVisibleListener()
        if(Kits.hasNotchScreen(this)){
            et_name.setText("刘海屏")
        }else{
            et_name.setText("正常屏")
        }
    }

    //一个静态变量存储高度
    var keyboardHeight = 0
    var isVisiableForLast = false

    fun addOnSoftKeyBoardVisibleListener() {
        if(keyboardHeight>0){
            return
        }
        var decorView = window.decorView
        var onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener{
            @Override
            fun onGlobalLayout() {
                var rect = Rect()
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度
                var displayHight = rect.bottom - rect.top
                //获得屏幕整体的高度
                var  hight = decorView.getHeight()
                var visible = displayHight / hight < 0.8
                var statusBarHeight = 0
                try {
                    var c = Class.forName("com.android.internal.R'$'dimen")
                    var obj = c.newInstance();
                    var field = c.getField("status_bar_height");
                    var x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = getResources().getDimensionPixelSize(x);
                } catch (e:Exception) {
                    e.printStackTrace();
                }

                if(visible&&visible!= isVisiableForLast){
                    //获得键盘高度
                    keyboardHeight = hight - displayHight-statusBarHeight;
                    Log.e("main","$keyboardHeight")
                }
                isVisiableForLast = visible;
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }
}
