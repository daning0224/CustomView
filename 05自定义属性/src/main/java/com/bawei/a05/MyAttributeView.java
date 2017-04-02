package com.bawei.a05;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 作    者：云凯文
 * 时    间：2017/2/28
 * 描    述：
 * 修改时间：
 */

public class MyAttributeView extends View {

    private static final String TAG = MyAttributeView.class.getSimpleName();
    private int myAge;
    private String myName;
    private Bitmap bitmap;

    public MyAttributeView(Context context) {
        this(context, null);
    }

    public MyAttributeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAttributeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);

    }

    private void initView(Context context, AttributeSet attrs) {
        //获取属性有三种方式
        //1.用命名空间获取
        String my_age = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_age");
        String my_name = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_name");
        String my_bg = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_bg");
        System.out.println("..................my_age=" + my_age + "my_name=" + my_name + "my_bg=" + my_bg);

        //2.遍历我们的属性集合
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.d(TAG, attrs.getAttributeName(i) + "========================" + attrs.getAttributeValue(i));
        }

        //3.使用系统工具，获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAttributeView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.MyAttributeView_my_age:
                    myAge = typedArray.getInt(index, 0);
                    break;
                case R.styleable.MyAttributeView_my_name:
                    myName = typedArray.getString(index);
                    break;
                case R.styleable.MyAttributeView_my_bg:
                    Drawable drawable = typedArray.getDrawable(index);
                    BitmapDrawable drawable1 = (BitmapDrawable) drawable;
                    bitmap = drawable1.getBitmap();
                    break;
            }
        }
        //记得最后回收
        typedArray.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(20);
        canvas.drawText(myName+"----"+myAge, 50, 50, paint);
        canvas.drawBitmap(bitmap, 50, 50, paint);

    }
}
