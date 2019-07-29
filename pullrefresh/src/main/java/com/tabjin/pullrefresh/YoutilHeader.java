package com.tabjin.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

public class YoutilHeader extends InternalAbstract implements RefreshHeader {

    private ImageView gifview;
    private TextView tv;
    public YoutilHeader(Context context) {
        this(context, null);
    }

    public YoutilHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    protected YoutilHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_header, this);
        tv = findViewById(R.id.tv);
        gifview = findViewById(R.id.gifview);
        GlideUtil.loadGif(context, R.drawable.header, gifview);
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
        switch (newState) {
            case None:
                tv.setText("");
                break;
            case PullDownToRefresh:
                tv.setText("下拉刷新");
                break;
            case Refreshing:
            case RefreshReleased:
                tv.setText("正在刷新");
                break;
            case ReleaseToRefresh:
                tv.setText("释放刷新");
                break;
            case ReleaseToTwoLevel:
                tv.setText("继续下拉刷新");
                break;
            case Loading:
                tv.setText("上拉加载");
                break;
        }
    }

    public void setRefreshStateString(String title){
        tv.setText(title);
    }
}
