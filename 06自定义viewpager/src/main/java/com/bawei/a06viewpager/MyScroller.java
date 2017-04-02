package com.bawei.a06viewpager;

import android.os.SystemClock;

/**
 * 作    者：云凯文
 * 时    间：2017/3/1
 * 描    述：
 * 修改时间：
 */

public class MyScroller {
    //X轴的起始坐标
    private float startX;
    //Y轴的起始坐标
    private float startY;
    //在X轴移动的距离
    private int distanceX;
    //在Y轴移动的距离
    private int distanceY;
    //开始时间
    private long startTime;
    //总时间
    private long totalTime = 500;
    /**
     * 是否移动完成
     * false没有移动完成
     * true:移动结束
     */
    private boolean isFinish;
    private float currX;

    /**
     * 得到坐标
     */
    public float getCurrX() {
        return currX;
    }

    public void startScroll(float startX, float startY, int distanceX, int distanceY, int abs) {
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.startTime = SystemClock.uptimeMillis();//系统开机时间
        this.isFinish = false;
    }

    /**
     * 求移动一小段的距离
     * 求移动一小段对应的坐标
     * 求移动一小段对应的时间
     * 返回true:正在移动
     * 返回false:移动结束
     */
    public boolean computeScrollOffset() {
        if (isFinish) {
            return false;
        }
        //结束时间
        long endTime = SystemClock.uptimeMillis();
        //这一小段所花的时间
        long passTime = endTime - startTime;
        if (passTime < totalTime) {//还没有移动结束
            //平均速度
//            float voleCity = distanceX / totalTime;
            //移动这一小段对应的距离
            float distanceSamllx = passTime * distanceX / totalTime;
            //移动这一小段后对应的X坐标
            currX = startX + distanceSamllx;
        } else {//移动结束
            isFinish = true;
            currX = startX + distanceX;
        }
        return true;
    }
}