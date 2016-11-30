package com.test.zhangtao.activitytest.http;

import android.content.Context;
import android.util.Log;

import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by zhangtao on 16/11/11.
 */
public abstract class SimpleCallBack<T> extends BaseCallBack<T>
{
    protected Context mContext;

    public SimpleCallBack(Context Context)
    {
        mContext = Context;
    }


    @Override
    public void onRequestBefore()
    {

    }

    @Override
    public void onResponse()
    {

    }

    @Override
    public void onFailure(WeiboException e)
    {
        Log.d("SimpleCallBack" , e.getMessage());
    }
}
