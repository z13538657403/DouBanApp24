package com.test.zhangtao.activitytest.http;

import com.test.zhangtao.activitytest.entity.HttpResult;
import com.test.zhangtao.activitytest.entity.MovieEntity;
import com.test.zhangtao.activitytest.entity.Subject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhangtao on 16/10/18.
 */
public interface MovieService
{
    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start , @Query("count") int count);
}
