package com.test.zhangtao.activitytest.play;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.util.AppContextUtil;

import java.io.IOException;

/**
 * Created by zhangtao on 16/10/22.
 */
public class MusicPlayManager
{
    private static MusicPlayManager musicPlayManager;
    public static MediaPlayer mediaPlayer;
    private static boolean isPause;

    private MusicPlayManager()
    {
        mediaPlayer = new MediaPlayer();
    }

    public static MusicPlayManager getInstance()
    {
        if (musicPlayManager == null)
        {
            synchronized (MusicPlayManager.class)
            {
                musicPlayManager = new MusicPlayManager();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
        }
        return musicPlayManager;
    }

    public void playMusic(MusicEntity musicEntity)
    {
        if (mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
        }

        try
        {
            mediaPlayer.reset();
//            Uri uri = Uri.parse(musicEntity.getDownUrl());
            Uri uri = Uri.parse(musicEntity.getM4a());
            Log.d("url" , musicEntity.getM4a());
            mediaPlayer.setDataSource(AppContextUtil.getInstance() , uri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer)
                {
                    mediaPlayer.start();
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        if (mediaPlayer != null && isPause)
        {
            mediaPlayer.start();
            isPause = false;
        }
    }

    public void pause()
    {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    public void release()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
