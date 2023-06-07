package com.henry.mediaaction.base.recycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by henry on 2019/6/13.
 * recycleView的基础adapter
 * 适合单view样式的adapter
 * 需要设置点击事件的可以在实现类中添加回调处理
 * 如需特殊处理可以重写需要的方法
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleViewHolder> {
    protected List<T> mList;
    //布局id
    private int layoutId;
    protected View view;
    protected WeakReference<BaseRecycleItemClick> weakReference;
    protected BaseRecycleItemClick click;
    public BaseRecycleAdapter(List<T> mList, int layoutId) {
        this.mList = mList;
        this.layoutId = layoutId;
    }
    public BaseRecycleAdapter(List<T> mList, int layoutId,BaseRecycleItemClick click) {
        this.mList = mList;
        this.layoutId = layoutId;
        weakReference = new WeakReference<>(click);
        this.click=click;
    }
    public BaseRecycleAdapter(int layoutId,BaseRecycleItemClick click){
        this.layoutId = layoutId;
        weakReference = new WeakReference<>(click);
        this.click=click;
    }
    /**
     *加载数据
     */
    public void setAdapterList(List<T> mList){
        this.mList = mList;
    }


    /**
     * 获取数据源
     * @return
     */
    public List<T> getAdapterList(){
        return mList;
    }
    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(layoutId, parent,false);
        return new BaseRecycleViewHolder(view);
    }
    /**
     * 获取当前的view
     * @return
     */
    public View getLayoutView(){
        return view;
    }
    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        convert(holder, mList.get(position),mList,position);
    }

    protected abstract void convert(BaseRecycleViewHolder holder, T t, List<T> mList, int position);

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }
}
