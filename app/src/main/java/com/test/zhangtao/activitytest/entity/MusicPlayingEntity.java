package com.test.zhangtao.activitytest.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangtao on 16/11/19.
 */
public class MusicPlayingEntity implements Serializable
{
    private List<MusicEntity> musicPlayingList;
    private int currentPlayingPosition;

    public List<MusicEntity> getMusicPlayingList()
    {
        return musicPlayingList;
    }

    public void setMusicPlayingList(List<MusicEntity> musicPlayingList)
    {
        this.musicPlayingList = musicPlayingList;
    }

    public int getCurrentPlayingPosition()
    {
        return currentPlayingPosition;
    }

    public void setCurrentPlayingPosition(int currentPlayingPosition)
    {
        this.currentPlayingPosition = currentPlayingPosition;
    }

    public MusicEntity getCurrentMusic()
    {
        return musicPlayingList.get(currentPlayingPosition);
    }
}
