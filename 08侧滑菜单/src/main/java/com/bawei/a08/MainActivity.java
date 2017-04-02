package com.bawei.a08;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView iv_main;
    private ArrayList<MyBean> myBean;
    private MyAdapter myAdapter;
    private SlideLayout mSlideLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_main = (ListView) findViewById(R.id.iv_main);
        //设置适配器
        //准备数据
        myBean = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            myBean.add(new MyBean("Content" + i));
        }
        myAdapter = new MyAdapter();
        iv_main.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myBean == null ? 0 : myBean.size();
        }

        @Override
        public Object getItem(int position) {
            return myBean.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //优化
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                viewHolder = new ViewHolder();
                viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
                viewHolder.item_menu = (TextView) convertView.findViewById(R.id.item_menu);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //根据位置得到内容
            final MyBean bean = myBean.get(position);
            viewHolder.item_content.setText(bean.getName());
            //content的点击事件
            viewHolder.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyBean myBean = MainActivity.this.myBean.get(position);
                    Toast.makeText(MainActivity.this, myBean.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            //menu的点击事件
            viewHolder.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SlideLayout slidelayout = (SlideLayout) v.getParent();
                    slidelayout.closeMenu();
                    myBean.remove(bean);
                    notifyDataSetChanged();
                }
            });

            //调用接口
            SlideLayout slidelayout = (SlideLayout) convertView;
            slidelayout.setOnStateChangeListener(new MyOnStateChangeListener());

            return convertView;
        }
    }

    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {

        @Override
        public void onColse(SlideLayout layout) {
            if (mSlideLayout == layout) {
                mSlideLayout = null;
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if (mSlideLayout != null && mSlideLayout != layout) {
                mSlideLayout.closeMenu();
            }
        }

        @Override
        public void onOpen(SlideLayout layout) {
            mSlideLayout = layout;
        }
    }

    public static class ViewHolder {
        TextView item_content;
        TextView item_menu;
    }

}
