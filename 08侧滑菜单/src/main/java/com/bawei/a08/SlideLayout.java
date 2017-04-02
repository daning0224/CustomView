package com.bawei.a08;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 作    者：云凯文
 * 时    间：2017/3/30
 * 描    述：侧滑菜单的item
 * 修改时间：
 */

public class SlideLayout extends FrameLayout {

    private static final String TAG = SlideLayout.class.getSimpleName();
    private View contentView;
    private View menuView;

    //滚动者
    private Scroller scroller;

    //Content的宽
    private int contentWidth;
    private int menuWidth;
    private int viewHeight;//三个控件的高都是一样的

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    /**
     * 当布局文件加载完成的时候回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    /**
     * 在测量方法里，得到各个控件的高和宽
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        viewHeight = getMeasuredHeight();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //指定菜单的位置
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);

    }

    private float startX;
    private float startY;
    private float downX;//只赋值一次
    private float downY;//只赋值一次

    /**
     * 点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "SlideLayout--onTouchEvent--ACTION_DOWN");
                //1.记录按下坐标
                downX = startX = event.getX();
                downY = startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "SlideLayout--onTouchEvent--ACTION_MOVE");
                //2.记录结束值
                float endX = event.getX();
                float endY = event.getY();

                //3.计算偏移量
                float distanceX = endX - startX;

                int toScrollX = (int) (getScrollX() - distanceX);
                //屏蔽非法值
                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());

                startX = event.getX();
                startY = event.getY();

                //在X轴和Y轴滑动的距离
                float DX = Math.abs(endX - downX);
                float DY = Math.abs(endY - downY);
                if (DX > DY && DX > 2) {
                    //水平方向滑动
                    //响应的是侧滑
                    //反拦截，事件给SlideLayout
                    getParent().requestDisallowInterceptTouchEvent(true);//请求不拦截触摸事件
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "SlideLayout--onTouchEvent--ACTION_UP");
                int totalScrollX = getScrollX();//偏移量
                if (totalScrollX < menuWidth / 2) {
                    //关闭Menu
                    closeMenu();
                } else {
                    //打开Menu
                    openMenu();
                }
                break;
        }
        return true;
    }

    /**
     * 拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "SlideLayout--onInterceptTouchEvent--ACTION_DOWN");
                //1.记录按下坐标
                downX = startX = event.getX();
                if (onStateChangeListener != null) {
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "SlideLayout--onInterceptTouchEvent--ACTION_MOVE");
                //2.记录结束值
                float endX = event.getX();

                //3.计算偏移量
                float distanceX = endX - startX;

                int toScrollX = (int) (getScrollX() - distanceX);
                //屏蔽非法值
                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());

                startX = event.getX();//这行是什么意思？还是不懂

                //在X轴和Y轴滑动的距离
                float DX = Math.abs(endX - downX);
                if (DX > 2) {
                    //拦截子控件的触摸事件
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "SlideLayout--onInterceptTouchEvent--ACTION_UP");
                break;
        }
        return intercept;
    }

    public void openMenu() {
        int distanceX = menuWidth - getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        invalidate();//强制刷新
        if (onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }

    public void closeMenu() {
        int distanceX = 0 - getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        invalidate();//强制刷新
        if (onStateChangeListener != null) {
            onStateChangeListener.onColse(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    //接口回调,监听SlideLayout状态改变
    public interface OnStateChangeListener {
        void onColse(SlideLayout layout);

        void onDown(SlideLayout layout);

        void onOpen(SlideLayout layout);
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }
}
