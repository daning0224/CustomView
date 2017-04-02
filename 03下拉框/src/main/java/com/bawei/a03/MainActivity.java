package com.bawei.a03;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_input;
    private ImageView iv_down_arrow;
    private PopupWindow popupWindow;
    private ListView listView;
    private ArrayList<String> msgs;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        et_input = (EditText) findViewById(R.id.et_input);
        iv_down_arrow = (ImageView) findViewById(R.id.iv_down_arrow);

        //实例化ListView
        listView = new ListView(MainActivity.this);
        //ListView的背景图片
        listView.setBackgroundResource(R.drawable.listview_background);
        //初始化数据
        initView();
        adapter = new MyAdapter(MainActivity.this, msgs);
        listView.setAdapter(adapter);
        //ListView条目的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.得到数据
                String msg = msgs.get(position);
                //2.设置到输入框
                et_input.setText(msg);
                //3.然后让PopupWindow的窗口消失
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });

        et_input.setOnClickListener(this);
        iv_down_arrow.setOnClickListener(this);

    }

    private void initView() {
        msgs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            msgs.add(i + "--aaaaaaaa--" + i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_input:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    showView();
                }
                break;

            case R.id.iv_down_arrow:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    showView();
                }
                break;
        }
    }

    private void showView() {
        if (popupWindow == null) {
            //创建PopupWindow
            popupWindow = new PopupWindow(MainActivity.this);
            //给PopupWindow设置宽和高
            popupWindow.setWidth(et_input.getWidth());
            //转换类px--->dp
            int height = DensityUtil.px2dip(MainActivity.this, 200);//dp
//            Toast.makeText(this, "" + height, Toast.LENGTH_SHORT).show();
            popupWindow.setHeight(height);
            //设置PopupWindow的内容为ListView
            popupWindow.setContentView(listView);
            //设置PopupWindow的焦点
            popupWindow.setFocusable(true);
        }
        //设置PopupWindow在EditText的下面
        popupWindow.showAsDropDown(et_input, 0, 0);
    }
}
