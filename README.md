# Android 自定义View

## 自定义View基础 RectTitleView
![这里写图片描述](http://img.blog.csdn.net/20160727234228654)
### 设置自定义属性
```
<resources>
    <!--声明自定义属性-->
    <attr name="titleText" format="string" />
    <attr name="titleTextColor" format="color" />
    <attr name="titleTextSize" format="dimension" />

    <!--将自定义属性与控件相结合-->
    <declare-styleable name="RectTitleView">
        <attr name="titleText" />
        <attr name="titleTextColor" />
        <attr name="titleTextSize" />
    </declare-styleable>
</resources>
```

### 自定义属性值的获取　
```
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
attrArray.recycle(); // 使用完成后记得回收
```

### 占用空间的测量
使用AT_MOST测量模式时，必须手动进行测量，否则会和match_parent的效果一样.
```
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
```

### 在onDraw()中绘制组件
```
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
```

### XML文件进行引用
```
 <com.terry.customview.widget.RectTitleView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:padding="5dp"
        app:titleText="@string/app_name"
        app:titleTextColor="@color/colorAccent"
        app:titleTextSize="20sp" />
```