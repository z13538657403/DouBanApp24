package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangtao on 16/10/20.
 */
public abstract class BaseAdapter<T , H extends BaseViewHodler> extends RecyclerView.Adapter<BaseViewHodler>
{
    protected Context context;
    protected List<T> datas;
    protected int layoutResId;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
    }

    public BaseAdapter(Context context , int layoutResId , List<T> datas)
    {
        this.context = context;
        this.layoutResId = layoutResId;
        this.datas = datas == null ? new ArrayList<T>() : datas;
    }

    @Override
    public BaseViewHodler onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(layoutResId , parent , false);
        BaseViewHodler vh = new BaseViewHodler(view , mOnItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHodler holder, int position)
    {
        T item = getItem(position);
        convert((H) holder, item);
    }

    @Override
    public int getItemCount()
    {
        return datas.size();
    }

    public T getItem(int position)
    {
        if (position >= datas.size()) return null;
        return datas.get(position);
    }

    public void clearData()
    {
        if (datas == null || datas.size() <= 0)
            return;
        datas.clear();
        notifyItemRangeRemoved(0 , datas.size());
    }

    public List<T> getDatas()
    {
        return datas;
    }

    public void addData(List<T> data)
    {
        addData(0 , data);
    }

    public void addData(int position , List<T> data)
    {
        if (data != null && data.size() > 0)
        {
            datas.addAll(data);
            notifyItemRangeChanged(position , datas.size());
        }
    }

    public List<T> getData()
    {
        return datas;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view , int position);
    }

    protected abstract void convert(H viewHolder , T item);
}
