package com.test.zhangtao.activitytest.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by zhangtao on 16/11/10.
 */
public class SPUtils
{
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SPUtils instance;
    private static final String SP_NAME = "CWEIBO";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String IS_LOGIN = "IS_LOGIN";

    public static SPUtils getInstance(Context context)
    {
        if (instance == null)
        {
            synchronized (SPUtils.class)
            {
                instance = new SPUtils();
                mSharedPreferences = context.getSharedPreferences(SP_NAME , Activity.MODE_PRIVATE);
                mEditor = mSharedPreferences.edit();
            }
        }
        return instance;
    }

    public void saveToken(Oauth2AccessToken accessToken)
    {
        mEditor.putString(ACCESS_TOKEN , new Gson().toJson(accessToken)).commit();
        mEditor.putBoolean(IS_LOGIN , true).commit();
    }

    public void clearToken()
    {
        mEditor.clear().commit();
    }

    public Oauth2AccessToken getToken()
    {
        String json = mSharedPreferences.getString(ACCESS_TOKEN , "");
        if (TextUtils.isEmpty(json))
        {
            return null;
        }
        return new Gson().fromJson(json , Oauth2AccessToken.class);
    }

    public boolean isLogin()
    {
        return mSharedPreferences.getBoolean(IS_LOGIN , false);
    }
}
