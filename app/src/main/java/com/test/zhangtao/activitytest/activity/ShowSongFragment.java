package com.test.zhangtao.activitytest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.test.zhangtao.activitytest.entity.SecondMusicEntity;
import com.test.zhangtao.activitytest.entity.SecondPageBean;
import com.test.zhangtao.activitytest.http.SecondMusicMethod;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Subscriber;

/**
 * Created by zhangtao on 16/11/23.
 */
public class ShowSongFragment extends BaseFragment
{
    @ViewInject(R.id.show_song_recyclerView)
    private RecyclerView mRecyclerView;

    private Subscriber<SecondPageBean> pageBeanSubscriber;
    private ProgressDialog progressDialog;
    private List<SecondMusicEntity> mSecondMusicEntities;
    private List<MusicEntity> mMusicEntities;
    private MusicInfoAdapter mAdpater;
    private MusicPlayingEntity musicPlayingEntity;
    private boolean isFirstClick = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_show_song , container , false);
        ViewUtils.inject(this , view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.....");
        musicPlayingEntity = new MusicPlayingEntity();
        return view;
    }

    public void getListData(String topId)
    {
        getListData(topId , 0);
    }

    public void getListData(String topId , final int dataSize)
    {
        if (dataSize == 0)
            progressDialog.show();

        pageBeanSubscriber = new Subscriber<SecondPageBean>()
        {
            @Override
            public void onCompleted()
            {
                if (dataSize == 0)
                    progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e)
            {
                if (dataSize == 0)
                    progressDialog.dismiss();
                Log.d("RequestError" , e.getMessage());
            }

            @Override
            public void onNext(SecondPageBean secondPageBean)
            {
                convertToMusicEntity(secondPageBean , dataSize);
                if (mMusicEntities != null)
                {
                    initView();
                }
            }
        };
        SecondMusicMethod.getInstance().getSecondMusicPageBean(pageBeanSubscriber , topId);
    }

    private void initView()
    {
        musicPlayingEntity.setMusicPlayingList(mMusicEntities);
        mAdpater = new MusicInfoAdapter(getActivity() , mMusicEntities);
        mAdpater.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (isFirstClick)
                {
                    MyApplication.getInstance().setMusicPlayingList(mMusicEntities);
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
        mRecyclerView.setAdapter(mAdpater);
    }

    private void convertToMusicEntity(SecondPageBean secondPageBean, int dataSize)
    {
        List<MusicEntity> tempMusicEntities = new ArrayList<>();
        mSecondMusicEntities = secondPageBean.getSonglist();
        if (mSecondMusicEntities == null)
        {
            Toast.makeText(getActivity() , "没有获取到数据" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (dataSize != 0 && mSecondMusicEntities.size() > dataSize)
        {
            for (int i = 0 ; i < dataSize ; i++)
            {
                MusicEntity musicEntity = convertData(mSecondMusicEntities.get(i));
                tempMusicEntities.add(musicEntity);
            }
        }
        else if (dataSize == 0)
        {
            for (int i = 0 ; i < mSecondMusicEntities.size() ; i++)
            {
                if (i > 30)
                {
                    break;
                }
                MusicEntity musicEntity = convertData(mSecondMusicEntities.get(i));
                tempMusicEntities.add(musicEntity);
            }
        }
        mMusicEntities = tempMusicEntities;
    }

    private MusicEntity convertData(SecondMusicEntity secondMusicEntity)
    {
        MusicEntity musicEntity = new MusicEntity();
        musicEntity.setM4a(secondMusicEntity.getUrl());
        musicEntity.setDownUrl(secondMusicEntity.getDownUrl());
        musicEntity.setMedia_mid(secondMusicEntity.getAlbummid());
        musicEntity.setSongid(secondMusicEntity.getSongid());
        musicEntity.setSingerid(secondMusicEntity.getSingerid());
        musicEntity.setAlbumpic_big(secondMusicEntity.getAlbumpic_big());
        musicEntity.setAlbumpic_small(secondMusicEntity.getAlbumpic_small());
        musicEntity.setSongname(secondMusicEntity.getSongname());
        musicEntity.setSingername(secondMusicEntity.getSingername());

        return musicEntity;
    }
}
