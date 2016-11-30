package com.test.zhangtao.activitytest.http;

import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.SecondMusicResult;
import com.test.zhangtao.activitytest.entity.SecondPageBean;
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
 * Created by zhangtao on 16/11/23.
 */
public class SecondMusicMethod
{
    private Retrofit retrofit;
    private SecondMusicService secondMusicService;

    private SecondMusicMethod()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(NetUrlContacts.DEFUALT_TIMEOUT , TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(NetUrlContacts.BASE_URL)
                .build();

        secondMusicService = retrofit.create(SecondMusicService.class);
    }

    private static class SingleInstance
    {
        private static final SecondMusicMethod single_instance = new SecondMusicMethod();
    }

    public static SecondMusicMethod getInstance()
    {
        return SingleInstance.single_instance;
    }

    public void getSecondMusicPageBean(Subscriber<SecondPageBean> subscriber , String topId)
    {
        secondMusicService.getMusicData(NetUrlContacts.showapi_appid , topId , NetUrlContacts.showapi_sign)
                .map(new SecondMusicFun1())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private class SecondMusicFun1 implements Func1<SecondMusicResult , SecondPageBean>
    {
        @Override
        public SecondPageBean call(SecondMusicResult secondMusicResult)
        {
            SecondPageBean secondPageBean = secondMusicResult.getShowapi_res_body().getPagebean();
            if (secondPageBean == null)
            {
                throw new ApiException(100);
            }
            return secondPageBean;
        }
    }
}
