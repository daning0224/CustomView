package com.bawei.a04.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bawei.a04.R;

/**
 * 作    者：云凯文
 * 时    间：2017/2/28
 * 描    述：自定义的步骤：
 * 1.实例化构造方法
 * 2.测量：OnMeasure()：如果当前的View是一个ViewGroup,它还有义务测量孩子，孩子有建议权
 * 3.指定的位置：OnLayout():一般View不用写这个方法，ViewGroup的时候才需要
 * 4.绘制视图：OnDraw(canvas);
 * 修改时间：
 */

public class MyToggleButton extends View implements View.OnClickListener {

    private Bitmap backgroundBitmap;
    private Bitmap slidingBitmap;
    private Paint paint;
    private int slidLeftMax;
    private int slidLeft;

    public MyToggleButton(Context context) {
        this(context, null);
    }

    public MyToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slidingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        slidLeftMax = backgroundBitmap.getWidth() - slidingBitmap.getWidth();

        //本控件的点击事件
        this.setOnClickListener(this);
    }

    private float startX;
    private float lastX;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                //1.记录按下的坐标
                lastX = startX = event.getX();
                isEnableClick = true;
                break;
            case MotionEvent.ACTION_MOVE://触摸滑动
                //2.计算结束值
                float endX = event.getX();
                //3.计算偏移量
                float distanceX = endX - startX;
                slidLeft = (int) (slidLeft + distanceX);
                //4.屏蔽非法值
                if (slidLeft < 0) {
                    slidLeft = 0;
                } else if (slidLeft > slidLeftMax) {
                    slidLeft = slidLeftMax;
                }
                //5.刷新
                invalidate();
                //6.数据还原
                startX = event.getX();
                if (Math.abs(endX - lastX) > 5) {
                    //就认为是滑动的
                    isEnableClick = false;
                }
                break;

            case MotionEvent.ACTION_UP://抬起
                if (!isEnableClick) {
                    if (slidLeft > slidLeftMax / 2) {
                        isOpen = true;
                    } else {
                        isOpen = false;
                    }
                    flushView();
                }
                break;
        }
        return true;
    }

    private boolean isOpen = false;
    private boolean isEnableClick = true;
    //true:点击事件生效，滑动事件不生效
    //false:点击事件不生效，滑动事件生效

    @Override
    public void onClick(View v) {
        if (isEnableClick) {
            isOpen = !isOpen;
            flushView();
        }
    }

    private void flushView() {
        if (isOpen) {
            slidLeft = slidLeftMax;
        } else {
            slidLeft = 0;
        }
        invalidate();//会导致OnDraw()执行，好比就是重新绘制
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //映射到画布上
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(slidingBitmap, slidLeft, 0, paint);
    }

}
