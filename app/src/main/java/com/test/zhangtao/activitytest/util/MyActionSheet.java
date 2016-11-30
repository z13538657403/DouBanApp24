package com.test.zhangtao.activitytest.util;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.baoyz.actionsheet.ActionSheet;

/**
 * Created by zhangtao on 16/11/14.
 */
public abstract class MyActionSheet
{
    public MyActionSheet(Context context , FragmentManager fragmentManager , String[] strArray)
    {
        createSheet(context , fragmentManager , strArray);
    }

    public void createSheet(Context context , FragmentManager fragmentManager , String[] array)
    {
        ActionSheet.createBuilder(context , fragmentManager)
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles(array)
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener()
                {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel)
                    {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index)
                    {
                        actionDetail(index);
                    }
                }).show();
    }
    protected abstract void actionDetail(int index);
}
