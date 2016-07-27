package com.terry.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.terry.customview.R;

import java.util.Random;

/**
 * Created by yangtianrui on 16-7-18.
 * 自定义View1 实现一个长方形的标题效果
 */
public class RectTitleView extends View {
    private String mTitleText;
    private int mTitleTextSize;
    private int mTitleTextColor;

    private Rect mRect;
    private Paint mPaint;


    // 构造函数中,获取自定义的属性
    public RectTitleView(Context context) {
        this(context, null);
    }

    public RectTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    /**
     * 获取所有的属性值
     */
    public RectTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 使用TypedArray获取自定义属性
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs
                , R.styleable.RectTitleView, defStyleAttr, 0);
        // 遍历所有属性
        int n = attrArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attrIndex = attrArray.getIndex(i);
            switch (attrIndex) {
                case R.styleable.RectTitleView_titleText:
                    mTitleText = attrArray.getString(attrIndex);
                    break;
                case R.styleable.RectTitleView_titleTextColor:
                    mTitleTextColor = attrArray.getColor(attrIndex, Color.BLACK);
                    break;
                case R.styleable.RectTitleView_titleTextSize:
                    // 设置默认值,同时使用TypedArray将sp转换成px
                    // 参数二为默认值16sp
                    mTitleTextSize = attrArray.getDimensionPixelSize(attrIndex
                            , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                                    , 16, getResources().getDisplayMetrics()));
            }
        }
        attrArray.recycle();
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);

        mRect = new Rect();
        // 设置边框,边框通过最后一个参数传入
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
        // 给这个View设置点击事件
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int[] nums = new int[4];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < nums.length; i++) {
                    nums[i] = random.nextInt(10);
                    sb.append(nums[i]);
                }
                mTitleText = sb.toString();
                // 通知重绘
                postInvalidate();
            }
        });
    }


    /**
     * 绘制View的显示效果
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTitleTextColor);
        // 将字体居中显示
        canvas.drawText(mTitleText, getWidth() / 2 - mRect.width() / 2
                , getHeight() / 2 + mRect.height() / 2, mPaint);

    }


    /**
     * 当测量模式为AT_MOST(属性值设置为wrap_content时,需要重写onMeasure()方法进行测量)
     * 测量模式为EXACTLY(属性值为一个精确值(match_parent)时,不需要重写此方法进行测量)
     * 测量模式SPECIFIED很少使用
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取测量值和测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 实际宽高
        int width;
        int height;
        // 先判断测量模式
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textWidth = mRect.width();
            // 获取实际的宽度
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            // 测量文字的高度
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textHeight = mRect.height();
            height = (int) (getPaddingBottom() + textHeight + getPaddingTop());
        }
        setMeasuredDimension(width, height);
    }
}
