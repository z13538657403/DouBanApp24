package com.test.zhangtao.activitytest.http;

import com.test.zhangtao.activitytest.entity.HttpResult;
import com.test.zhangtao.activitytest.entity.Subject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangtao on 16/10/18.
 */
public class HttpMethods {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private MovieService movieService;

    //构造器私有化
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    /**
     *   用于获取豆瓣电影Top250的数据
     *   由调用者传过来的观察者对象
     *   起始位置
     *   获取长度
     */

    public void getTopMovie(Subscriber<List<Subject>> subscriber , int start , int count)
    {
        movieService.getTopMovie(start , count)
                .map(new HttpResultFunc<List<Subject>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     *   用来统一处理的resultCode，并将HttpResult的Data部分分离出来返回给subscriber
     *
     *   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T> , T>
    {
        @Override
        public T call(HttpResult<T> httpResult)
        {
            if (httpResult.getCount() == 0)
            {
                throw new ApiException(100);
            }
            return httpResult.getSubjects();
        }
    }
}
