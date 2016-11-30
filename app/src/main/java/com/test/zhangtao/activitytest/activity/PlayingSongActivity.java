package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.MyPagerAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.contacts.TabContact;
import com.test.zhangtao.activitytest.entity.CurrentPlayState;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.entity.ProgressPosition;
import com.test.zhangtao.activitytest.util.MusicProvider;

import java.util.ArrayList;
import java.util.LinkedList;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangtao on 16/11/20.
 */
public class PlayingSongActivity extends BaseActivity
{
    @ViewInject(R.id.my_favorite_song)
    private ImageView mFavoriteImg;
    @ViewInject(R.id.start_and_pause)
    private ImageView imgPlayMusic;
    @ViewInject(R.id.play_time_remain)
    private TextView playTimeRemain;
    @ViewInject(R.id.playing_seekBar)
    private SeekBar mSeekBar;
    @ViewInject(R.id.play_song_name)
    private TextView playSongName;
    @ViewInject(R.id.playing_dot)
    private ImageView playingDot;
    @ViewInject(R.id.list_dot)
    private ImageView listDot;
    @ViewInject(R.id.my_viewPager)
    private ViewPager viewPager;

    private Intent intent;
    private MusicEntity currentMusicEntity;
    private int currentSeekPosition = 0;
    private int currentTimeRemain = 0;

    private ArrayList<Fragment> mFragmentList;
    private MusicProvider musicProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_playing_activity);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        musicProvider = MusicProvider.getInstance();
        musicProvider.setMyContext(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);
        }

        intent = getIntent();
        initView();
        initEvent();
    }

    private void initView()
    {
        currentMusicEntity = (MusicEntity) intent.getSerializableExtra(NetUrlContacts.TO_PLAYING_ACTIVITY);
        currentSeekPosition = intent.getIntExtra(NetUrlContacts.CURRENT_SEEK_POSITION , 0);
        currentTimeRemain = intent.getIntExtra(NetUrlContacts.CURRENT_TIME_REMAIN , 0);
        if (currentMusicEntity == null)
        {
            Toast.makeText(this , "没有任何数据！" , Toast.LENGTH_SHORT).show();
            return;
        }

        showFavoritedImg();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(PlayingSongFragment.getInstance(currentMusicEntity.getAlbumpic_big()));
        mFragmentList.add(PlayingListFragment.getInstance());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager() , mFragmentList , TabContact.PAGER_TITLE));

        playSongName.setText(currentMusicEntity.getSongname());
        mSeekBar.setProgress(currentSeekPosition);
        playTimeRemain.setText("-" + (currentTimeRemain/60) + ":" + (currentTimeRemain%60));
    }

    private void initEvent()
    {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        playingDot.setImageResource(R.mipmap.dot_green);
                        listDot.setImageResource(R.mipmap.dot_white);
                        break;
                    case 1:
                        playingDot.setImageResource(R.mipmap.dot_white);
                        listDot.setImageResource(R.mipmap.dot_green);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    @OnClick(R.id.my_favorite_song)
    public void toFavoriteList(View view)
    {
        if (musicProvider.getAll() == null)
        {
            musicProvider.put(currentMusicEntity);
            mFavoriteImg.setImageResource(R.mipmap.ic_favorited_misic);
        }
        else
        {
            if (!musicProvider.getAll().contains(currentMusicEntity))
            {
                musicProvider.put(currentMusicEntity);
                mFavoriteImg.setImageResource(R.mipmap.ic_favorited_misic);
            }
            else
            {
                musicProvider.delete(currentMusicEntity);
                mFavoriteImg.setImageResource(R.mipmap.ic_favorite_music);
            }
        }
    }

    @OnClick({R.id.start_and_pause , R.id.playing_next_song , R.id.playing_before_song})
    public void playSong(View view)
    {
        Intent intent = new Intent(TabContact.PLAY_ACTION);
        switch (view.getId())
        {
            case R.id.start_and_pause:
                intent.putExtra("control" , 1);
                break;
            case R.id.playing_next_song:
                intent.putExtra("control" , 2);
                break;
            case R.id.playing_before_song:
                intent.putExtra("control" , 3);
                break;
        }
        //发送广播
        sendBroadcast(intent);
    }

    public void onEventMainThread(CurrentPlayState currentPlayState)
    {
        if (currentPlayState != null)
        {
            if (currentPlayState.getCurrentState() == 0x11 || currentPlayState.getCurrentState() == 0x13)
            {
                imgPlayMusic.setImageResource(R.mipmap.ic_start_play);
            }
            else if (currentPlayState.getCurrentState() == 0x12)
            {
                imgPlayMusic.setImageResource(R.mipmap.ic_big_pause);
            }
        }
    }

    public void onEventMainThread(final MusicEntity musicEntity)
    {
        if (musicEntity != null)
        {
            playSongName.setText(musicEntity.getSongname());
            currentMusicEntity = musicEntity;
            showFavoritedImg();
        }
    }

    public void onEventMainThread(ProgressPosition progressPosition)
    {
        if (progressPosition != null)
        {
            double currentProgress = ((double)progressPosition.getCurrentPosition()) /
                    ((double)(progressPosition.getDuration()));
            int position = (int) (currentProgress * 100);
            mSeekBar.setProgress(position);
            int remain = progressPosition.getDuration() - progressPosition.getCurrentPosition();
            playTimeRemain.setText("-" + (remain/60) + ":" + (remain%60));
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showFavoritedImg()
    {
        LinkedList<MusicEntity> favoriteMusicList = musicProvider.getAll();
        if (favoriteMusicList != null)
        {
            if (favoriteMusicList.contains(currentMusicEntity))
            {
                mFavoriteImg.setImageResource(R.mipmap.ic_favorited_misic);
            }
            else
            {
                mFavoriteImg.setImageResource(R.mipmap.ic_favorite_music);
            }
        }
    }
}
