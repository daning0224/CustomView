package com.bawei.a01;

import android.animation.ObjectAnimator;
import android.view.ViewGroup;

/**
 * 作    者：云凯文
 * 时    间：2017/2/4
 * 描    述：
 * 修改时间：
 */
public class Tools {

    public static void hideView(ViewGroup view) {

        hideView(view, 0);
    }

    public static void showView(ViewGroup view) {

        showView(view, 0);
    }

    public static void hideView(ViewGroup view, int startOffset) {
        /**
         * 补间动画的写法
         * */
//        //旋转动画
//        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, view.getWidth() / 2, view.getHeight());
//        //动画播放持续的时间
//        rotateAnimation.setDuration(500);
//        //动画停留在播放完成后的状态
//        rotateAnimation.setFillAfter(true);
//        //延迟多久后播放动画
//        rotateAnimation.setStartOffset(startOffset);
//        //启动
//        view.startAnimation(rotateAnimation);
//
//        for (int i = 0; i < view.getChildCount(); i++) {
//            View children = view.getChildAt(i);
//            children.setEnabled(false);
//        }

        /**
         * 属性动画的写法
         * */
//        view.setRotation();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 180);
        animator.setDuration(500);
        animator.setStartDelay(startOffset);
        animator.start();

        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight());
    }

    public static void showView(ViewGroup view, int startOffset) {

        /**
         * 补间动画
         * */
//        //旋转动画
//        RotateAnimation rotateAnimation = new RotateAnimation(180, 360, view.getWidth() / 2, view.getHeight());
//        //动画播放持续的时间
//        rotateAnimation.setDuration(500);
//        //动画停留在播放完成后的状态
//        rotateAnimation.setFillAfter(true);
//        //延迟多久后播放动画
//        rotateAnimation.setStartOffset(startOffset);
//        //启动
//        view.startAnimation(rotateAnimation);
//
//        //操作每个孩子
//        for (int i = 0; i < view.getChildCount(); i++) {
//            View children = view.getChildAt(i);
//            children.setEnabled(true);
//        }

        /**
         * 属性动画
         * */
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 180, 360);
        animator.setDuration(500);
        animator.setStartDelay(startOffset);
        animator.start();

        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight());

    }
}
