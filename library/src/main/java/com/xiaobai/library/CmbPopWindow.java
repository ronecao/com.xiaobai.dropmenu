package com.xiaobai.library;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;

/**
 * Created by tianqing on 2017/5/4.
 */

public abstract class CmbPopWindow extends PopupWindow {

    private View contentView;
    private WeakReference<Context> activity;


    /**
     */
    public CmbPopWindow(Context activity) {
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.activity = new WeakReference<>(activity);
        initView();
    }



    private void initView () {
        contentView = LayoutInflater.from(activity.get()).inflate(getResId(), null);
        bindView(contentView);
        setContentView(contentView);
        setAnimationStyle(R.style.TRM_ANIM_STYLE);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setBackgroundAlpha(1f, 0.75f, 240);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(0.75f, 1f, 300);
            }
        });

    }

    private void setBackgroundAlpha(float from, float to, int duration) {
         WindowManager.LayoutParams layoutParams = null;
         Window window = null;
        if (activity.get() instanceof Activity) {
            layoutParams = ((Activity)(activity.get())).getWindow().getAttributes();
            window = ((Activity)(activity.get())).getWindow();
        }  else {
            return;
        }
        final WindowManager.LayoutParams lp = layoutParams;
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        final Window finalWindow = window;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                finalWindow.setAttributes(lp);
            }
        });
        animator.start();
    }

    protected abstract int getResId();

    protected abstract void bindView(View view);



}
