package com.bawei.a09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * 作    者：云凯文
 * 时    间：2017/4/1
 * 描    述：
 * 修改时间：
 */

public class WaveView extends View {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //刷新数据
            flushData();
            //强制绘制
            invalidate();
            //循环动画
            if (isRunning) {
                handler.sendEmptyMessageDelayed(0, 50);
            }
        }
    };


    private static final int DIS_SOLP = 13;
    private ArrayList<Wave> wList;
    private int[] colors = new int[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.DKGRAY, Color.LTGRAY};
    private boolean isRunning = false;


    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        wList = new ArrayList<>();
    }

    /**
     * 绘制水波纹
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < wList.size(); i++) {
            Wave wave = wList.get(i);
            canvas.drawCircle(wave.pointX, wave.pointY, wave.radius, wave.paint);
        }
    }

    /**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                addPoint(x, y);
                break;
        }
        return true;
    }

    /**
     * 添加新的水波浪中心点
     */
    private void addPoint(int x, int y) {
        if (wList.size() == 0) {
            addPoint2List(x, y);
            //第一次启动动画
            isRunning = true;
            handler.sendEmptyMessage(0);
        } else {
            //取最后一个圆环
            Wave w = wList.get(wList.size() - 1);
            if (Math.abs(w.pointX - x) > DIS_SOLP || Math.abs(w.pointY - y) > DIS_SOLP) {
                addPoint2List(x, y);
            }
        }
    }

    /**
     * 添加新的波浪
     */
    private void addPoint2List(int x, int y) {
        Wave w = new Wave();
        w.pointX = x;
        w.pointY = y;
        Paint mPaint = new Paint();
        mPaint.setColor(colors[(int) (Math.random() * 7)]);//设置随机颜色
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);//设置样式：圆环
        w.paint = mPaint;//将当前的画笔设置给Wave类的笔
        wList.add(w);//添加到集合中
    }

    /**
     * 刷新数据
     */
    private void flushData() {
        for (int i = 0; i < wList.size(); i++) {
            Wave w = wList.get(i);
            //如果透明为0，从集合中删除
            int alpha = w.paint.getAlpha();
            if (alpha == 0) {
                //删除i以后，i的值应该再减1，否则会漏掉一个对象，不过，在此处影响不大，效果看不出来
                wList.remove(i);
                continue;
            }
            alpha -= 5;
            if (alpha < 5) {
                alpha = 0;
            }
            //降低透明度
            w.paint.setAlpha(alpha);
            //扩大半径
            w.radius = w.radius + 3;
            //设置半径的厚度
            w.paint.setStrokeWidth(w.radius / 3);
        }
        //如果集合被清空，就停止刷新动画
        if (wList.size() == 0) {
            isRunning = false;
        }
    }

    /**
     * 定义一个水波浪
     */
    class Wave {
        //圆心
        int pointX;
        int pointY;
        //画笔
        Paint paint;
        //半径
        int radius;
    }
}
