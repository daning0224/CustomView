package com.bawei.a06viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

/**
 * 作    者：云凯文
 * 时    间：2017/2/28
 * 描    述：仿ViewPager
 * 修改时间：
 */

public class MyViewPager extends ViewGroup {

    /*手势识别器*/
    //1.定义出来
    //2.实例化
    //3.在OnTouchEvent()把事件传递给手势识别器
    private GestureDetector detector;
    //当前页面下标的位置
    private int currentIndex;
    private Scroller scroller;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        scroller = new Scroller(context);
        //创建手势识别器
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(context, "长按", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                Toast.makeText(context, "滑动", Toast.LENGTH_SHORT).show();
                //x:要在X轴上移动的距离
                //y:要在Y轴上移动的距离
                scrollBy((int) distanceX, 0);
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(context, "双击", Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历孩子，给每个孩子指定在屏幕的坐标位置
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout(i * getWidth(), 0, (i + 1) * getWidth(), getHeight());
        }
    }


    /**
     * 拦截事件，只有ViewGroup才有这个方法
     * 返回true,拦截事件，将会触发当前事件的 onTouchEvent()方法
     * 返回false,不拦截事件，事件继续传递给孩子
     */
    private float downX;
    private float downY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        boolean result = false;//默认传递给孩子
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录按下坐标
                downX = ev.getX();
                downY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                //2.记录结束值
                float endX = ev.getX();
                float endY = ev.getY();

                //计算绝对值
                float distanceX = Math.abs(endX - downX);
                float distanceY = Math.abs(endY - downY);
                if (distanceX > distanceY && distanceX > 5) {
                    result = true;
                }else{
                    scrollToPager(currentIndex);
                }
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return result;
    }

    private float startX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //这里是第三步，把事件传递给手势识别器
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录坐标
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //2.来到新的坐标
                float endX = event.getX();
                //当前下标位置
                int tempIndex = currentIndex;
                if ((startX - endX) > getWidth() / 2) {
                    //显示下一个页面
                    tempIndex++;

                } else if ((endX - startX) > getWidth() / 2) {
                    //显示上一个页面
                    tempIndex--;
                }
                //根据下标位置移动到指定的页面
                scrollToPager(tempIndex);
                break;
        }
        return true;
    }

    //屏蔽非法值，根据位置移动到指定页面
    public void scrollToPager(int tempIndex) {
        if (tempIndex < 0) {
            tempIndex = 0;
        }
        if (tempIndex > getChildCount() - 1) {
            tempIndex = getChildCount() - 1;
        }
        //当前页面的下标
        currentIndex = tempIndex;
        if (monPagerChangListenter != null) {
            monPagerChangListenter.onScrollToPager(currentIndex);
        }


        /**
         * 处理左右滑动生硬的问题
         * */
        //计算出剩下的距离
        int distanceX = currentIndex * getWidth() - getScrollX();
        //移动到指定的位置
//        scrollTo(currentIndex * getWidth(), getScrollY());
//        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY(), Math.abs(distanceX));
        invalidate();//onDraw,computeScroll()
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            float currX = scroller.getCurrX();
            scrollTo((int) currX, getScrollY());
            invalidate();
        }
    }

    /**
     * 观察者模式，接口回调
     */
    //定义一个接口变量
    private OnPagerChangListenter monPagerChangListenter;

    //设置监听页面的改变
    public interface OnPagerChangListenter {
        //当页面改变的时候回调这个方法
        void onScrollToPager(int position);
    }

    //设置页面改变的监听
    public void setOnPagerChangListenter(OnPagerChangListenter l) {
        this.monPagerChangListenter = l;
    }

    //测量,显示测试页面的其他子View
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            //根据位置得到子View
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }


}