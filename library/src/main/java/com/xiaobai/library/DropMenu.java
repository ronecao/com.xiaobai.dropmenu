package com.xiaobai.library;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.PopupWindowCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by tianqing on 2017/5/4.
 */
public class DropMenu extends CmbPopWindow{

    private WeakReference<View> anchorView;
    private WeakReference<Context> activity;
    private RecyclerView mRecyclerView;
    private MenuAdapter mAdapter;
    private List<MenuItem> menuItemList = new ArrayList<>();
    private boolean showIcon = false;
    private OnMenuItemClickListener onMenuItemClickListener;
    private int offsetx;

    @Override
    protected int getResId() {
        return R.layout.popwindow_drop;
    }

    @Override
    protected void bindView(View view) {
        findView(view);
    }

    protected DropMenu(final View view, Context activity, List<MenuItem> menuItemList, boolean showIcon, OnMenuItemClickListener onMenuItemClickListener, int offsetx) {
        super(activity);
        this.anchorView = new WeakReference<>(view);
        this.activity = new WeakReference<>(activity);
        this.menuItemList = menuItemList;
        this.showIcon = showIcon;
        this.onMenuItemClickListener = onMenuItemClickListener;
        this.offsetx = offsetx;
    }


    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }


    protected void findView(final View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.trm_recyclerview);
    }

    public void dismiss(){
       super.dismiss();
    }

    public boolean isShow() {
        return isShowing();
    }

    public interface OnMenuItemClickListener{
        void onMenuItemClick(int position);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static class Builder {
        private WeakReference<Context> context;
        private WeakReference<View> anchorView;
        private boolean showIcon;
        private List<MenuItem> menuItems = new ArrayList<>();
        private OnMenuItemClickListener onMenuItemClickListener;
        private int offsetX;

        public Builder context(Context context) {
            this.context = new WeakReference<>(context);
            return this;
        }

        public Builder anchorView(View anchorView) {
            this.anchorView = new WeakReference<>(anchorView);
            return this;
        }

        public Builder showIcon(boolean showIcon) {
            this.showIcon = showIcon;
            return this;
        }

        public Builder menuItems(List<MenuItem> menuItems) {
            this.menuItems = menuItems;
            return this;
        }

        public Builder onMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
            this.onMenuItemClickListener = onMenuItemClickListener;
            return this;
        }

        public Builder offsetx(int offsetX) {
            this.offsetX = offsetX;
            return  this;
        }

        public DropMenu createOffset() {
            final  DropMenu dropMenu = new DropMenu(anchorView.get(), context.get(), menuItems, showIcon, onMenuItemClickListener, offsetX);
            dropMenu.mAdapter = new MenuAdapter(dropMenu.activity.get(), dropMenu, dropMenu.menuItemList, dropMenu.showIcon);
            dropMenu.mAdapter.setOnMenuItemClickListener(dropMenu.onMenuItemClickListener);
            dropMenu.mRecyclerView.setLayoutManager(new LinearLayoutManager(dropMenu.activity.get(), LinearLayoutManager.VERTICAL, false));
            dropMenu.mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            dropMenu. mAdapter.setData(dropMenu.menuItemList);
            dropMenu.mAdapter.setShowIcon(dropMenu.showIcon);
            dropMenu.mRecyclerView.setAdapter(dropMenu.mAdapter);
            View contentView = dropMenu.getContentView();
            contentView.measure(makeDropDownMeasureSpec(dropMenu.getWidth()),
                    makeDropDownMeasureSpec(dropMenu.getHeight()));
            final int measuredW =  dropMenu.getContentView().getMeasuredWidth();
            dropMenu.getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // 自动调整箭头的位置
                    autoAdjustArrowPos(dropMenu, dropMenu.getContentView(), dropMenu.anchorView.get());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        dropMenu.getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        dropMenu.getContentView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
            PopupWindowCompat.showAsDropDown(dropMenu, dropMenu.anchorView.get(), -measuredW + dropMenu.anchorView.get().getWidth() + dropMenu.offsetx, 0, Gravity.START);
            return  dropMenu;
        }


        public DropMenu createNoOffset() {
            final  DropMenu dropMenu = new DropMenu(anchorView.get(), context.get(), menuItems, showIcon, onMenuItemClickListener, offsetX);
            dropMenu.mAdapter = new MenuAdapter(dropMenu.activity.get(), dropMenu, dropMenu.menuItemList, dropMenu.showIcon);
            dropMenu.mAdapter.setOnMenuItemClickListener(dropMenu.onMenuItemClickListener);
            dropMenu.mRecyclerView.setLayoutManager(new LinearLayoutManager(dropMenu.activity.get(), LinearLayoutManager.VERTICAL, false));
            dropMenu.mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            dropMenu. mAdapter.setData(dropMenu.menuItemList);
            dropMenu.mAdapter.setShowIcon(dropMenu.showIcon);
            dropMenu.mRecyclerView.setAdapter(dropMenu.mAdapter);
            dropMenu.getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // 自动调整箭头的位置
                    autoAdjustArrowPos(dropMenu, dropMenu.getContentView(), dropMenu.anchorView.get());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        dropMenu.getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        dropMenu.getContentView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
            PopupWindowCompat.showAsDropDown(dropMenu, dropMenu.anchorView.get(), 0, 0, Gravity.START);
            return  dropMenu;
        }


        protected  void autoAdjustArrowPos(PopupWindow popupWindow, View contentView, View anchorView) {
            View upArrow = contentView.findViewById(R.id.up_arrow);
            View downArrow = contentView.findViewById(R.id.down_arrow);
            int pos[] = new int[2];
            contentView.getLocationOnScreen(pos);
            int popLeftPos = pos[0];
            anchorView.getLocationOnScreen(pos);
            int anchorLeftPos = pos[0];
            int arrowLeftMargin = anchorLeftPos - popLeftPos + anchorView.getWidth() / 2 - upArrow.getWidth() / 2;
            upArrow.setVisibility(popupWindow.isAboveAnchor() ? View.INVISIBLE : View.VISIBLE);
            downArrow.setVisibility(popupWindow.isAboveAnchor() ? View.VISIBLE : View.INVISIBLE);

            RelativeLayout.LayoutParams upArrowParams = (RelativeLayout.LayoutParams) upArrow.getLayoutParams();
            upArrowParams.leftMargin = arrowLeftMargin;
            RelativeLayout.LayoutParams downArrowParams = (RelativeLayout.LayoutParams) downArrow.getLayoutParams();
            downArrowParams.leftMargin = arrowLeftMargin;
        }
    }
}


