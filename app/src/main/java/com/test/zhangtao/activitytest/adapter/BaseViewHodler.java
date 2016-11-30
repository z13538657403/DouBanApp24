package com.test.zhangtao.activitytest.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangtao on 16/10/20.
 */
public class BaseViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private SparseArray<View> views;
    private BaseAdapter.OnItemClickListener mOnItemClickListener;

    public BaseViewHodler(View itemView , BaseAdapter.OnItemClickListener listener)
    {
        super(itemView);
        this.mOnItemClickListener = listener;
        itemView.setOnClickListener(this);
        this.views = new SparseArray<>();
    }

    public View getView(int viewId)
    {
        return retrieveView(viewId);
    }

    public TextView getTextView(int viewId)
    {
        return retrieveView(viewId);
    }

    public Button getButton(int viewId)
    {
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId)
    {
        return retrieveView(viewId);
    }

    protected <T extends View> T retrieveView(int viewId)
    {
        View view = views.get(viewId);
        if (view == null)
        {
            view = itemView.findViewById(viewId);
            views.put(viewId , view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View view)
    {
        if (mOnItemClickListener != null)
        {
            mOnItemClickListener.onItemClick(view , getLayoutPosition());
        }
    }
}
