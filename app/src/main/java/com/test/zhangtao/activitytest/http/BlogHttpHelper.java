package com.test.zhangtao.activitytest.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.test.zhangtao.activitytest.constant.CWConstant;
import com.test.zhangtao.activitytest.util.AppContextUtil;
import com.test.zhangtao.activitytest.util.SPUtils;

import java.util.Map;

/**
 * Created by zhangtao on 16/11/10.
 */
public class BlogHttpHelper
{
    private static BlogHttpHelper blogHttpHelper;
    private Context context = AppContextUtil.getInstance();
    private Gson gson;
    private SPUtils mSpUtils;
    private AsyncWeiboRunner asyncWeiboRunner;
    private WeiboParameters parameters;

    static
    {
        blogHttpHelper = new BlogHttpHelper();
    }

    private BlogHttpHelper()
    {
        gson = new Gson();
        mSpUtils = SPUtils.getInstance(context);
        asyncWeiboRunner = new AsyncWeiboRunner(context);

    }

    public static BlogHttpHelper getInstance()
    {
        return blogHttpHelper;
    }

    public void request(String url , String requestMethod , Map<String , Object> params , final BaseCallBack callBack)
    {
        setParams(params);
        callBack.onRequestBefore();
        asyncWeiboRunner.requestAsync(url, parameters, requestMethod, new RequestListener()
        {
            @Override
            public void onComplete(String response)
            {
                callBack.onResponse();

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response);
                if (element.isJsonObject())
                {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("error"))
                    {
                        callBack.onError(object.get("error").getAsString() , object.get("error_code").getAsInt());
                    }
                    else
                    {
                        if (callBack.mType == String.class)
                        {
                            callBack.onSuccess(response , response);
                        }
                        else
                        {
                            Object obj = gson.fromJson(response , callBack.mType);
                            callBack.onSuccess(response , obj);
                        }
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e)
            {
                callBack.onFailure(e);
            }
        });
    }

    private void setParams(Map<String, Object> params)
    {
        parameters = new WeiboParameters(CWConstant.APP_KEY);
        parameters.put(WBConstants.AUTH_ACCESS_TOKEN , mSpUtils.getToken().getToken());
        if (params != null)
        {
            for (Map.Entry<String , Object> entry : params.entrySet())
            {
                if (entry.getKey().equals("pic"))
                {
                    Bitmap bitmap = (Bitmap) entry.getValue();
                    parameters.put(entry.getKey() , bitmap);
                    continue;
                }
                parameters.put(entry.getKey() , entry.getValue());
            }
        }
    }
}
