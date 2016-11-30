package com.test.zhangtao.activitytest.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by zhangtao on 16/10/20.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T , BaseViewHodler>
{
    public SimpleAdapter(Context context, int layoutResId, List<T> datas)
    {
        super(context, layoutResId, datas);
    }
}
