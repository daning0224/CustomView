package com.bawei.a02_viewpager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //实例化控件
    private ViewPager viewPager;
    private TextView tv_title;
    private LinearLayout ll_point_group;

    //添加图片的集合
    private ArrayList<ImageView> imageViews;

    //上一次远点显示的位置
    private int prePosition = 0;

    private boolean isDragging = false;

    //图片资源的ID
    private final int[] imageids = {
            R.mipmap.a,
            R.mipmap.b,
            R.mipmap.c,
            R.mipmap.d,
            R.mipmap.e,
    };

    //图片标题的集合
    private final String[] imageDescriptions = {
            "尚硅谷拔河争霸赛",
            "凝聚你我，放飞梦想",
            "抱歉，没座位了",
            "3月份就业名单全部曝光",
            "平均起薪11345元"
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(item);

            //延迟发送空消息
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取全部控件
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);

        //添加图片的集合
        imageViews = new ArrayList();

        for (int i = 0; i < imageids.length; i++) {

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageids[i]);
            //将图片添加到集合中
            imageViews.add(imageView);

            //添加点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);

            if (i == 0) {
                point.setEnabled(true);//显示红色
            } else {
                point.setEnabled(false);//显示灰色
                params.leftMargin = 8;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);

        }

        //ViewPager的适配器
        viewPager.setAdapter(new MyPagetAdapter());

        //ViewPager的监听事件
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置ViewPager的中间位置
        int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageViews.size();
        viewPager.setCurrentItem(item);

        //发消息
        handler.sendEmptyMessageDelayed(0, 3000);


    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动的时候回调的方法
         *
         * @param position             当前页面的位置
         * @param positionOffset       滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调
         *
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int realPosition = position % imageViews.size();
            //设置文本信息
            tv_title.setText(imageDescriptions[realPosition]);

            //将上一个小圆点设置为灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //将当前的小圆点设置为红色
            ll_point_group.getChildAt(realPosition).setEnabled(true);
            prePosition = realPosition;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                handler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                isDragging = false;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0, 4000);
            }
        }
    }

    class MyPagetAdapter extends PagerAdapter {

        //条目的总数
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realposition = position % imageViews.size();
            ImageView imageView = imageViews.get(realposition);
            container.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);
                            break;

                        case MotionEvent.ACTION_UP:
                            handler.removeCallbacksAndMessages(null);
                            handler.sendEmptyMessageDelayed(0, 4000);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            break;

                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }
                    return false;
                }
            });

            //ViewPager的点击事件
            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag() % imageViews.size();
                    String text = imageDescriptions[position];
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
