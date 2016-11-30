package com.test.zhangtao.activitytest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.cjj.MaterialRefreshLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.MusicInfoAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.entity.MusicPlayingEntity;
import com.test.zhangtao.activitytest.entity.PageBean;
import com.test.zhangtao.activitytest.http.MusicMethods;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.Collections;
import java.util.List;
import de.greenrobot.event.EventBus;
import rx.Subscriber;

/**
 * Created by zhangtao on 16/11/18.
 */
public class SearchSongFragment extends BaseFragment
{
    @ViewInject(R.id.refresh_search_song)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.search_song_recyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.tv_no_song)
    private TextView showNoSong;

    private Subscriber<PageBean> subscriber;
    private ProgressDialog progressDialog;
    private List<MusicEntity> musicInfoList;
    private MusicInfoAdapter mAdapter;
    private MusicPlayingEntity musicPlayingEntity;
    private boolean isFirstClick = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search_song , container , false);
        ViewUtils.inject(this , view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.....");
        musicPlayingEntity = new MusicPlayingEntity();
        return view;
    }

    public void doSearchSong(String newText)
    {
        if (TextUtils.isEmpty(newText))
        {
            Toast.makeText(getActivity() , "请输入搜索关键字！！！" , Toast.LENGTH_SHORT).show();
            return;
        }

        getSearchData(newText);
    }

    private void getSearchData(String newText)
    {
        progressDialog.show();
        subscriber = new Subscriber<PageBean>()
        {
            @Override
            public void onCompleted()
            {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e)
            {
                progressDialog.dismiss();
                Log.d("SearchError" , e.getMessage());
            }

            @Override
            public void onNext(PageBean pageBean)
            {
                musicInfoList = pageBean.getContentlist();
                initView();
            }
        };
        MusicMethods.getInstance().getMusicPageBean(subscriber , 1 , newText);
    }

    private void initView()
    {
        if (musicInfoList != null && musicInfoList.size() > 0)
        {
            showNoSong.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
            initRecyclerView();
        }
        else
        {
            showNoSong.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView()
    {
        musicPlayingEntity.setMusicPlayingList(musicInfoList);
        mAdapter = new MusicInfoAdapter(getActivity() , musicInfoList);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (isFirstClick)
                {
                    MyApplication.getInstance().setMusicPlayingList(musicInfoList);
                    isFirstClick = false;
                }
                Toast.makeText(getActivity() , position + "" , Toast.LENGTH_SHORT).show();
                musicPlayingEntity.setCurrentPlayingPosition(position);
                EventBus.getDefault().post(musicPlayingEntity);

                Intent intent = new Intent(getActivity() , PlayingSongActivity.class);
                intent.putExtra(NetUrlContacts.TO_PLAYING_ACTIVITY , musicPlayingEntity.getCurrentMusic());
                intent.putExtra(NetUrlContacts.CURRENT_SEEK_POSITION , 0);
                intent.putExtra(NetUrlContacts.CURRENT_TIME_REMAIN , 0);
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration1(getActivity() , DividerItemDecoration1.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }
}
