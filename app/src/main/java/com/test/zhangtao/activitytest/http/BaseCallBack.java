package com.test.zhangtao.activitytest.http;

import com.google.gson.internal.$Gson$Types;
import com.sina.weibo.sdk.exception.WeiboException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhangtao on 16/10/25.
 */
public abstract class BaseCallBack<T>
{
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subClass)
    {
        Type superclass = subClass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallBack()
    {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void onRequestBefore();

    public abstract void onResponse();

    public abstract void onSuccess(String response , T t);

    public abstract void onError(String error , int errorCode);

    public abstract void onFailure(WeiboException e);
}
