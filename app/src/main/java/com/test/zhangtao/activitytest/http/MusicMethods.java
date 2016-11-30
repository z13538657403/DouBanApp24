package com.test.zhangtao.activitytest.http;

import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.MusicResult;
import com.test.zhangtao.activitytest.entity.PageBean;
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
 * Created by zhangtao on 16/10/21.
 */
public class MusicMethods
{
    private Retrofit retrofit;
    private MusicService musicService;

    private MusicMethods()
    {
        //手动创建一个OkHttpClient，并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(NetUrlContacts.DEFUALT_TIMEOUT , TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(NetUrlContacts.BASE_URL)
                .build();
        musicService = retrofit.create(MusicService.class);
    }

    private static class SingleInstance
    {
        private static final MusicMethods SINGLE_INSTANCE = new MusicMethods();
    }

    public static MusicMethods getInstance()
    {
        return SingleInstance.SINGLE_INSTANCE;
    }

    public void getMusicPageBean(Subscriber<PageBean> subscriber , int page , String keyword)
    {
        musicService.getMusicData(keyword , page , NetUrlContacts.showapi_appid , NetUrlContacts.showapi_sign)
                .map(new MusicFunc1())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private class MusicFunc1 implements Func1<MusicResult , PageBean>
    {
        @Override
        public PageBean call(MusicResult musicResult)
        {
            PageBean pageBean = musicResult.getShowapi_res_body().getPagebean();
            if (pageBean == null)
            {
                throw new ApiException(100);
            }
            return pageBean;
        }
    }
}
