package com.xiaobai.library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Author：whh
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MenuItem> menuItemList;
    private boolean showIcon;
    private DropMenu mTopRightMenu;
    private DropMenu.OnMenuItemClickListener onMenuItemClickListener;

    public MenuAdapter(Context context, DropMenu topRightMenu, List<MenuItem> menuItemList, boolean show) {
        this.mContext = context;
        this.mTopRightMenu = topRightMenu;
        this.menuItemList = menuItemList;
        this.showIcon = show;
    }

    public void setData(List<MenuItem> data){
        menuItemList = data;
        notifyDataSetChanged();
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trm_item_popup_menu_list, parent, false);
        return new TRMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MenuItem menuItem = menuItemList.get(position);
        if (holder instanceof  TRMViewHolder) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((TRMViewHolder) holder).text.getLayoutParams();
            if (showIcon){
                ((TRMViewHolder) holder).relativeLayout_icon.setVisibility(View.VISIBLE);
                int resId = menuItem.getIcon();
                ((TRMViewHolder) holder).icon.setImageResource(resId < 0 ? 0 : resId);
                layoutParams.setMargins(0, 0, 0, 0);
                ((TRMViewHolder) holder).text.setLayoutParams(layoutParams);
            }else{
                ((TRMViewHolder) holder).relativeLayout_icon.setVisibility(View.GONE);
                layoutParams.setMargins(32, 0, 32, 0);
                ((TRMViewHolder) holder).text.setLayoutParams(layoutParams);
            }
            ((TRMViewHolder) holder).text.setText(menuItem.getText());

            if (position == 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ((TRMViewHolder) holder).container.setBackground(addStateDrawable(mContext, -1, R.drawable.trm_popup_top_pressed));
                } else {
                    ((TRMViewHolder) holder).container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_top_pressed));
                }
            }else if (position == menuItemList.size() - 1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ((TRMViewHolder) holder).container.setBackground(addStateDrawable(mContext, -1, R.drawable.trm_popup_bottom_pressed));
                } else {
                    ((TRMViewHolder) holder).container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_bottom_pressed));
                }

            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ((TRMViewHolder) holder).container.setBackground(addStateDrawable(mContext, -1, R.drawable.trm_popup_middle_pressed));
                } else {
                    ((TRMViewHolder) holder).container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_middle_pressed));

                }
            }
            final int pos = holder.getAdapterPosition();
            ((TRMViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null) {
                        mTopRightMenu.dismiss();
                        onMenuItemClickListener.onMenuItemClick(pos);
                    }
                }
            });
            if (menuItemList.size() != 0 && position == menuItemList.size() - 1){
                ((TRMViewHolder) holder).view_line.setVisibility(View.GONE);
            } else {
                ((TRMViewHolder) holder).view_line.setVisibility(View.VISIBLE);

            }
        }

    }

    private StateListDrawable addStateDrawable(Context context, int normalId, int pressedId){
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = normalId == -1 ? null : context.getResources().getDrawable(normalId);
        Drawable pressed = pressedId == -1 ? null : context.getResources().getDrawable(pressedId);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{}, normal);
        return sd;
    }


    @Override
    public int getItemCount() {
        return menuItemList == null ? 0 : menuItemList.size();
    }

    class TRMViewHolder extends RecyclerView.ViewHolder{
        ViewGroup container;
        ImageView icon;
        RelativeLayout relativeLayout_icon;
        TextView text;
        View view_line;
        TRMViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView;
            icon = (ImageView) itemView.findViewById(R.id.trm_menu_item_icon);
            text = (TextView) itemView.findViewById(R.id.trm_menu_item_text);
            relativeLayout_icon = (RelativeLayout) itemView.findViewById(R.id.layout_icon);
            view_line =  itemView.findViewById(R.id.view_line);
        }
    }

    class GradeHolder extends RecyclerView.ViewHolder {

        ViewGroup container;
        ImageView icon;
        TextView text;

        public GradeHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView;
            icon = (ImageView) itemView.findViewById(R.id.trm_menu_item_icon);
            text = (TextView) itemView.findViewById(R.id.trm_menu_item_text);
        }
    }

    public void setOnMenuItemClickListener(DropMenu.OnMenuItemClickListener listener){
        this.onMenuItemClickListener = listener;
    }
}
