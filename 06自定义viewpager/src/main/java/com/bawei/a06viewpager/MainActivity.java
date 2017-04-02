package com.bawei.a06viewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private MyViewPager myViewPager;
    private RadioGroup rg_main;
    private int[] ids = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewPager = (MyViewPager) findViewById(R.id.myViewPager);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        //添加页面
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setBackgroundResource(ids[i]);
            //添加到MyViewPager的这个自定义View中
            myViewPager.addView(imageView);
        }
        //添加测试页面
        View textView = View.inflate(this, R.layout.test, null);
        myViewPager.addView(textView,3);

        //添加RadioButton
        for (int i = 0; i < myViewPager.getChildCount(); i++) {
            RadioButton radioButton = new RadioButton(MainActivity.this);
            radioButton.setId(i);//0 ~ 5
            if (i == 0) {
                radioButton.setChecked(true);
            }
            //添加到RadioGroup里面
            rg_main.addView(radioButton);
        }

        //设置RedioGroup选中状态的变化
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //根据下标位置定位到具体的某个页面
                myViewPager.scrollToPager(checkedId);
            }
        });

        //设置监听页面的改变
        myViewPager.setOnPagerChangListenter(new MyViewPager.OnPagerChangListenter() {
            @Override
            public void onScrollToPager(int position) {
                rg_main.check(position);
            }
        });


    }
}
