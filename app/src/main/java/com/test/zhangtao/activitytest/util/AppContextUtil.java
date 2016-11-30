package com.test.zhangtao.activitytest.util;

import android.content.Context;

/**
 * Created by zhangtao on 16/10/22.
 */
public class AppContextUtil
{
    private static Context sContext;
    private AppContextUtil()
    {
    }

    public static void init(Context context)
    {
        sContext = context;
    }

    public static Context getInstance()
    {
        if (sContext == null)
        {
            throw new NullPointerException("the Context is null");
        }
        return sContext;
    }
}
