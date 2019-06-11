package com.tabjin.shorttext.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.tabjin.shorttext.R;

public class CircleBarView extends View {

    private Paint progressPaint;//绘制圆弧的画笔
    private RectF rectF;
    private int mRadius = 25;
    private int angle = 0;
    private int color = Color.WHITE;
    private int background  =  Color.WHITE;


    public CircleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public CircleBarView(Context context, AttributeSet attrs,int d) {
        super(context, attrs,d);
        init(context,attrs,d);
    }

    private void init(Context context,AttributeSet attrs,int d){
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);//只描边，不填充
        progressPaint.setColor(Color.BLUE);
        progressPaint.setAntiAlias(true);//设置抗锯齿
        rectF = new RectF();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleBarView, d, 0);
        mRadius = (int) a.getDimension(R.styleable.CircleBarView_radius,25f);
        Log.e("main",""+mRadius);
        color = a.getColor(R.styleable.CircleBarView_forgroundcolor,Color.WHITE);
        background =a.getColor(R.styleable.CircleBarView_backgroundcolor,Color.TRANSPARENT);
        a.recycle();
        progressPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        progressPaint.setColor(background);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),progressPaint);
        int x = getMeasuredWidth()/2;
        int y = getMeasuredHeight()/2;
        int left = x-mRadius;
        int top = y-mRadius;
        int right = x+mRadius;
        int bottom = y + mRadius;
        rectF.set(left,top,right,bottom);
        progressPaint.setColor(color);
        canvas.drawArc(rectF,-90+angle,360-angle,true,progressPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (wideMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            width = wideSize;
        } else {
            width = mRadius * 2 + getPaddingLeft() + getPaddingRight();
            if (wideMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, wideSize);
            }

        }

        if (heightMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            height = heightSize;
        } else {
            height = mRadius * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }

        }
        setMeasuredDimension(width, height);
    }

    public void setAngle(int percent){
        angle = 360*percent/100;
        invalidate();
    }

}