package com.test.zhangtao.activitytest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.MovieAdapter;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.entity.Subject;
import com.test.zhangtao.activitytest.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zhangtao on 16/10/20.
 */
public class MovieFragment extends BaseFragment
{
    private Subscriber<List<Subject>> subscriber;
    private MovieAdapter movieAdapter;
    private List<Subject> movieDatas;

    @ViewInject(R.id.movie_list)
    private RecyclerView movieList;
    @ViewInject(R.id.refresh_movie)
    private MaterialRefreshLayout refreshLayout;
    private ProgressDialog progressDialog;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_LOADMORE = 1;
    private static final int STATE_REFRESH = 2;
    private int state = STATE_NORMAL;

    private int currentDataPosition = 0;
    private int loadDataSize = 20;

    public static MovieFragment getInstance()
    {
        MovieFragment sf = new MovieFragment();
        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout , container , false);
        ViewUtils.inject(this , view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载中...");

        getData();
        initEvent();
        return view;
    }

    private void initEvent()
    {
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener()
        {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout)
            {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout)
            {
                loadMoreData();
            }
        });
    }

    private void refreshData()
    {
        currentDataPosition = 0;
        loadDataSize = 20;
        state = STATE_REFRESH;
        getData();
    }

    private void loadMoreData()
    {
        currentDataPosition = movieDatas.size() + currentDataPosition;
        loadDataSize = 10;
        state = STATE_LOADMORE;
        getData();
    }

    private void getData()
    {
        if (state == STATE_NORMAL)
        {
            progressDialog.show();
        }
        subscriber = new Subscriber<List<Subject>>()
        {
            @Override
            public void onCompleted()
            {
                if (state == STATE_NORMAL)
                {
                    progressDialog.dismiss();
                }
                Log.d("Load", "Success");
            }

            @Override
            public void onError(Throwable e)
            {
                if (state == STATE_NORMAL)
                {
                    progressDialog.dismiss();
                }
                Log.d("Message", e.getMessage());
            }

            @Override
            public void onNext(List<Subject> subjects)
            {
                movieDatas = subjects;
                showData();
            }
        };
        HttpMethods.getInstance().getTopMovie(subscriber , currentDataPosition , loadDataSize);
    }

    private void showData()
    {
        switch (state)
        {
            case STATE_NORMAL:
                movieAdapter = new MovieAdapter(getActivity() , R.layout.movie_item , movieDatas);
                movieAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        String movieUrl = movieAdapter.getItem(position).getAlt();
                        String movieName = movieAdapter.getItem(position).getTitle();
                        Intent intent = new Intent(getActivity() , MovieActivity.class);
                        intent.putExtra("url" , movieUrl);
                        intent.putExtra("title" , movieName);
                        startActivity(intent);
                    }
                });

                movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
                movieList.setItemAnimator(new DefaultItemAnimator());
                movieList.addItemDecoration(new DividerItemDecoration1(getActivity() , LinearLayoutManager.VERTICAL));
                movieList.setAdapter(movieAdapter);
                break;
            case STATE_REFRESH:
                movieAdapter.clearData();
                movieAdapter.addData(movieDatas);
                movieList.scrollToPosition(0);
                refreshLayout.finishRefresh();
                break;
            case STATE_LOADMORE:
                movieAdapter.addData(movieAdapter.getDatas().size() , movieDatas);
                movieList.scrollToPosition(movieAdapter.getDatas().size());
                refreshLayout.finishRefreshLoadMore();
                break;
        }
    }
}
