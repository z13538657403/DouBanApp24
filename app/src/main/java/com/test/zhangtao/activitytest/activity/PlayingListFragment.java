package com.test.zhangtao.activitytest.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.MusicInfoAdapter;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.entity.PlayingPositionEntity;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangtao on 16/11/21.
 */
public class PlayingListFragment extends BaseFragment
{
    @ViewInject(R.id.playing_song_list)
    private RecyclerView mPlayingRecyclerView;
    @ViewInject(R.id.playing_list_toolBar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.linear_bar)
    private LinearLayout mLinearBar;

    private List<MusicEntity> mPlayingList;
    private MusicInfoAdapter mAdapter;

    public static PlayingListFragment getInstance()
    {
        PlayingListFragment playingListFragment = new PlayingListFragment();
        return playingListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.play_list_fragment , null);
        ViewUtils.inject(this , view);

        setStatusBar();
        initToolBar();
        initView();
        return view;
    }

    private void initToolBar()
    {
        mToolBar.setLeftButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });
    }

    private void initView()
    {
        mPlayingList = MyApplication.getInstance().getMusicPlayingList();
        if (mPlayingList != null)
        {
            mAdapter = new MusicInfoAdapter(getActivity() , mPlayingList);
            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    EventBus.getDefault().post(new PlayingPositionEntity(position));
                }
            });

            mPlayingRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));
            mPlayingRecyclerView.addItemDecoration(new DividerItemDecoration1(getActivity() , DividerItemDecoration1.VERTICAL_LIST));
            mPlayingRecyclerView.setAdapter(mAdapter);
        }
    }

    protected void setStatusBar()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (mLinearBar != null)
            {
                mLinearBar.setVisibility(View.VISIBLE);
                int statusHeight = getStatusBarHeight();
                android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mLinearBar.getLayoutParams();
                params.height = statusHeight;
                mLinearBar.setLayoutParams(params);
            }
        }
    }
}
