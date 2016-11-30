package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.MusicInfoAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.entity.MusicPlayingEntity;
import com.test.zhangtao.activitytest.util.MusicProvider;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangtao on 16/11/24.
 */
public class FavoriteMusicActivity extends BaseActivity
{
    @ViewInject(R.id.favorite_music_toolBar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.my_favorite_recyclerView)
    private RecyclerView mRecyclerView;

    private MusicInfoAdapter mAdapter;
    private MusicProvider musicProvider;
    private List<MusicEntity> musicEntities;
    private MusicPlayingEntity musicPlayingEntity;
    private boolean isFirstClick = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.favorite_music_activity);
        ViewUtils.inject(this);

        musicProvider = MusicProvider.getInstance();
        musicProvider.setMyContext(this);
        musicPlayingEntity = new MusicPlayingEntity();

        initToolBar();
        initView();
    }

    private void initToolBar()
    {
        mToolBar.setLeftButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicProvider.clear();
            }
        });
    }

    private void initView()
    {
        LinkedList<MusicEntity> musics = musicProvider.getAll();
        musicEntities = new ArrayList<>();
        if (musics != null && musics.size() > 0)
        {
            for (MusicEntity musicEntity : musics)
            {
                musicEntities.add(musicEntity);
            }

            musicPlayingEntity.setMusicPlayingList(musicEntities);
            mAdapter = new MusicInfoAdapter(this , musicEntities);
            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    if (isFirstClick)
                    {
                        MyApplication.getInstance().setMusicPlayingList(musicEntities);
                        isFirstClick = false;
                    }
                    Toast.makeText(FavoriteMusicActivity.this , position + "" , Toast.LENGTH_SHORT).show();
                    musicPlayingEntity.setCurrentPlayingPosition(position);
                    EventBus.getDefault().post(musicPlayingEntity);

                    Intent intent = new Intent(FavoriteMusicActivity.this , PlayingSongActivity.class);
                    intent.putExtra(NetUrlContacts.TO_PLAYING_ACTIVITY , musicPlayingEntity.getCurrentMusic());
                    intent.putExtra(NetUrlContacts.CURRENT_SEEK_POSITION , 0);
                    intent.putExtra(NetUrlContacts.CURRENT_TIME_REMAIN , 0);
                    startActivity(intent);
                }
            });
            mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
            mRecyclerView.addItemDecoration(new DividerItemDecoration1(this , DividerItemDecoration1.VERTICAL_LIST));
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
