package com.test.zhangtao.activitytest.http;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by zhangtao on 16/11/11.
 */
public abstract class DialogCallBack<T> extends SimpleCallBack<T>
{
    private ProgressDialog progressDialog;
    public DialogCallBack(Context Context)
    {
        super(Context);
        initDialog();
    }

    @Override
    public void onRequestBefore()
    {
        showDialog();
    }

    @Override
    public void onResponse()
    {
        dismissDialog();
    }

    private void initDialog()
    {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在加载中");
    }

    public void showDialog()
    {
        progressDialog.show();
    }

    public void dismissDialog()
    {
        progressDialog.dismiss();
    }
}
