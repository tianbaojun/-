package com.tabjin.pullrefresh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import kotlinx.android.synthetic.main.activity_main4.*

class Main4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        var refresh = SmartRefreshLayout(this)
        refresh.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        refresh.setRefreshHeader(YoutilHeader(this@Main4Activity))
        content.addView(refresh,0)

        refresh.setOnMultiPurposeListener(object : SimpleMultiPurposeListener(){
            override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
                super.onStateChanged(refreshLayout, oldState, newState)
                when(newState){
                    RefreshState.Refreshing->{
                        Toast.makeText(this@Main4Activity,"刷新中",Toast.LENGTH_SHORT).show()
                        refreshLayout.layout.postDelayed({
                            refreshLayout.finishRefresh(500)
                        },3000)
                    }
                }
            }
        })
    }
}
