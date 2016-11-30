package com.test.zhangtao.activitytest.http;

import com.test.zhangtao.activitytest.entity.SecondMusicResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhangtao on 16/11/23.
 */
public interface SecondMusicService
{
    @GET("/213-4")
    Observable<SecondMusicResult> getMusicData(@Query("showapi_appid") String showapi_appid ,
                                               @Query("topid") String topid,
                                               @Query("showapi_sign") String showapi_sign);
}
