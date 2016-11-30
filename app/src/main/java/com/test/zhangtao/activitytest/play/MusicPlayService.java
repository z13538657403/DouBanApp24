package com.test.zhangtao.activitytest.play;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.zhangtao.activitytest.contacts.TabContact;
import com.test.zhangtao.activitytest.entity.CurrentPlayState;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.entity.MusicPlayingEntity;
import com.test.zhangtao.activitytest.entity.PlayingPositionEntity;
import com.test.zhangtao.activitytest.entity.ProgressPosition;
import com.test.zhangtao.activitytest.util.AppContextUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangtao on 16/10/22.
 */
public class MusicPlayService extends Service
{
    private List<MusicEntity> mMusicEntity;
    private MusicReceiver musicReceiver;
    private int status = 0x11;
    private int currentSong = 0;
    private MusicPlayManager musicPlayManager;
    private boolean isFirst = true;
    @Override
    public void onCreate()
    {
        super.onCreate();
        EventBus.getDefault().register(this);
        AppContextUtil.init(MusicPlayService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        musicPlayManager = MusicPlayManager.getInstance();
        if (MusicPlayManager.mediaPlayer != null)
        {
            MusicPlayManager.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer)
                {
                    playNextMusic();
                }
            });
        }
        progressTimer();
        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        filter.addAction(TabContact.PLAY_ACTION);
        musicReceiver = new MusicReceiver();
        registerReceiver(musicReceiver, filter);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onEventMainThread(MusicPlayingEntity musicPlayingEntity)
    {
        if (musicPlayingEntity != null)
        {
            this.mMusicEntity = musicPlayingEntity.getMusicPlayingList();
            if (isFirst)
            {
                currentSong = musicPlayingEntity.getCurrentPlayingPosition();
                isFirst = false;
            }
            else
            {
                currentSong = musicPlayingEntity.getCurrentPlayingPosition() - 1;
                playNextMusic();
            }
        }
    }

    public void onEventMainThread(PlayingPositionEntity positionEntity)
    {
        if (positionEntity != null)
        {
            currentSong = positionEntity.getCurrentPlayingPosition() - 1;
            playNextMusic();
        }
    }

    public class MusicReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int control = intent.getIntExtra("control" , -1);
            switch (control)
            {
                //播放或暂停
                case 1:
                    //如果原来是没有播放的状态
                    if (status == 0x11)
                    {
                        //准备并播放音乐
                        musicPlayManager.playMusic(mMusicEntity.get(currentSong));
//                        progressTimer();
                        status = 0x12;
                    }
                    //原来处于播放状态
                    else if (status == 0x12)
                    {
                        //暂停
                        musicPlayManager.pause();
                        //改变为暂停状态
                        status = 0x13;
                    }
                    //原来处于暂停状态
                    else if (status == 0x13)
                    {
                        //播放
                        musicPlayManager.start();
                        //改变状态
                        status = 0x12;
                    }
                    break;
                //下一首
                case 2:
                    playNextMusic();
                    break;
                //上一首
                case 3:
                    playBeforeMusic();
                    break;
            }
        }
    }

    private void playNextMusic()
    {
        status = 0x12;
        currentSong ++;
        if (currentSong >= mMusicEntity.size())
        {
            currentSong = 0;
        }
        EventBus.getDefault().post(mMusicEntity.get(currentSong));
        musicPlayManager.playMusic(mMusicEntity.get(currentSong));
    }

    private void playBeforeMusic()
    {
        status =  0x12;
        currentSong--;
        if (currentSong < 0)
        {
            currentSong = mMusicEntity.size() - 1;
        }
        EventBus.getDefault().post(mMusicEntity.get(currentSong));
        musicPlayManager.playMusic(mMusicEntity.get(currentSong));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void progressTimer()
    {
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (MusicPlayManager.mediaPlayer != null)
                {
                    EventBus.getDefault().post(new CurrentPlayState(status));
                    if (MusicPlayManager.mediaPlayer.isPlaying()) {
                        int currentPosition = MusicPlayManager.mediaPlayer.getCurrentPosition() / 1000;
                        int duration = MusicPlayManager.mediaPlayer.getDuration() / 1000;
                        EventBus.getDefault().post(new ProgressPosition(duration, currentPosition));
                    }
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask , 0 , 1000);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(musicReceiver);
        musicPlayManager.release();
    }
}
