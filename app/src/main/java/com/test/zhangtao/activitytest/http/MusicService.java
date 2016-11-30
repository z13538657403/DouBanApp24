package com.test.zhangtao.activitytest.http;

import com.test.zhangtao.activitytest.entity.MusicResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhangtao on 16/10/21.
 */
public interface MusicService
{
    @GET("/213-1")
    Observable<MusicResult> getMusicData(@Query("keyword") String keyWord ,
                                         @Query("page") int page ,
                                         @Query("showapi_appid") String showapi_appid ,
                                         @Query("showapi_sign") String showapi_sign);
}
