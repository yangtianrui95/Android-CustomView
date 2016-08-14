package com.terry.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.terry.customview.R;

/**
 * Created by shixi_tianrui1 on 16-8-14.
 * 自定义圆形进度条
 */
public class LoadingView extends View {

    private int mSpeed;
    private int mStartColor;
    private int mSecondColor;
    private int mCircleWidth;

    private Paint mPaint;
    private int mProgress;
    private boolean isNext;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs
                , R.styleable.LoadingView, defStyleAttr, 0);
        // 获取所有属性
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.LoadingView_circleWidth:
                    mCircleWidth = a.getDimensionPixelOffset(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.LoadingView_startColor:
                    mStartColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.LoadingView_secondColor:
                    mSecondColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.LoadingView_speed:
                    mSpeed = a.getInt(attr, 10);
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        // 开启绘图线程
        new Thread() {
            @Override
            public void run() {
                // 不停的绘制圆
                while (true) {
                    mProgress++;
                    if (mProgress == 360) {
                        mProgress = 0;
                        isNext = !isNext;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    /**
     * 绘制圆形
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = center - mCircleWidth / 2;
        // 设置圆环的宽度
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE); // 设置为空心
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        if (!isNext) {
            mPaint.setColor(mStartColor);
            // 画出圆环
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mSecondColor);
            // 根据进度进行绘制圆弧
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mStartColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        }
    }
}