package com.henry.mediaaction.base.recycle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Created by henry on 2019/6/13.
 * recycleView的BaseHolder
 * 可以在里面提取一些常用的设置
 */
public class BaseRecycleViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;
    public View convertView;
    public BaseRecycleViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        convertView = itemView;
    }
    /**
     * 返回一个具体的view对象
     * @param viewId
     * @param <T>
     * @return
     */
    public  <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本
     * @param resId
     * @param text
     */
    public void setText(int resId, String text) {
        try {
            TextView view = getView(resId);
            view.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置显示隐藏
     * @param resId
     * @param visibility
     */
    public void setVisible(int resId, boolean visibility) {
        try {
            View view = getView(resId);
            view.setVisibility(visibility?View.VISIBLE:View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置显示隐藏
     * @param resId
     * @param visibility
     */
    public void setVisibility(int resId, int visibility) {
        try {
            View view = getView(resId);
            view.setVisibility(visibility);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
