package com.tabjin.pullrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import cn.appsdream.nestrefresh.base.AbsRefreshLayout
import cn.appsdream.nestrefresh.base.AbsRefreshLayout.LoaderDecor.*
import kotlinx.android.synthetic.main.layout_header.view.*

class MyNestFooter constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr), AbsRefreshLayout.LoaderDecor{
    private val mContext: Context = context

    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    private var mState = AbsRefreshLayout.LoaderDecor.STATE_NORMAL
    init {
        inflate(context, R.layout.layout_header, this)
        tv.text = "下拉刷新"
    }


    override fun scrollRate(y: Int) {

    }

    override fun setState(state: Int) {
        if(mState == state){
            return
        }
        when(state){
            STATE_NORMAL->tv.setText(R.string.loader_load_more)
            STATE_READY->tv.text = "释放刷新"
            STATE_REFRESHING->tv.text = "正在刷新"
            STATE_SUCCESS->tv.text = "刷新成功"
        }
    }



}