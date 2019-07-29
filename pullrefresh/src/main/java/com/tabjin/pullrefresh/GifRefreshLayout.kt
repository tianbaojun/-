package com.tabjin.pullrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import cn.appsdream.nestrefresh.base.NestRefreshLayout

class GifRefreshLayout constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : NestRefreshLayout(context, attrs, defStyleAttr) {
    constructor(context: Context,attrs: AttributeSet?) : this(context,attrs,0)
    init {
        var headerView = MyNestHeader(context)
        headerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        setHeaderView(headerView)
    }
}