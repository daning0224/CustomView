package com.bawei.a07;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作    者：云凯文
 * 时    间：2017/3/10
 * 描    述：绘制快速索引的字母
 * 修改时间：
 */

public class IndexView extends View {

    /**
     * 步骤一：
     * 1.将26个字母放入集合中
     * 2.在onMeasure计算每格的宽(itemWidth)和高(itemHeight)
     * 3.在onDraw计算字母的宽（wordWidth）和高（wordHeight）,wordX,wordY
     */

    /**
     * 步骤二:
     * 手指按下文字变色
     * 1.重写onTouchEvent(),返回true,在down/move的过程中计算
     * 2.在onDraw()方法对于的下标设置画笔变色
     * 3.在手指抬起的时候（up）颜色还原
     */

    private String words[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    //定义每格的宽高
    private int itemWidth;
    private int itemHeight;

    //初始化画笔
    private Paint mPaint;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //初始化画笔
        mPaint = new Paint();
        //设置画笔的颜色
        mPaint.setColor(Color.WHITE);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置粗字体
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //设置字体大小
        mPaint.setTextSize(20);

    }


    //测量的方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < words.length; i++) {

            if (touchIndex == i) {
                //设置灰色
                mPaint.setColor(Color.GRAY);
            } else {
                //设置白色
                mPaint.setColor(Color.WHITE);
            }


            String word = words[i];
            Rect rect = new Rect();
            mPaint.getTextBounds(word, 0, 1, rect);
            //获取字母的宽和高
            int wordWidth = rect.width();
            int wordHeight = rect.height();

            //计算每个字母在控件上的坐标位置
            float wordX = itemWidth / 2 - wordWidth / 2;
            float wordY = itemHeight / 2 + wordHeight / 2 + i * itemHeight;

            canvas.drawText(word, wordX, wordY, mPaint);
        }
    }

    //字母下标的位置
    private int touchIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                float Y = event.getY();
                int index = (int) (Y / itemHeight);//字母索引
                if (index != touchIndex) {
                    touchIndex = index;
                    //强制绘制
                    invalidate();
                    if (onIndexChangeListener != null && touchIndex < words.length) {
                        onIndexChangeListener.onIndexChange(words[touchIndex]);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                touchIndex = -1;

                //强制绘制
                invalidate();
                break;
        }
        return true;
    }

    //字母下标索引变化的监听器
    public interface OnIndexChangeListener {

        void onIndexChange(String word);
    }

    private OnIndexChangeListener onIndexChangeListener;

    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }
}
