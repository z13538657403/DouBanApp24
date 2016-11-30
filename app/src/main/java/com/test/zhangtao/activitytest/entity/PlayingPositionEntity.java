package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/11/22.
 */
public class PlayingPositionEntity
{
    private int currentPlayingPosition;

    public PlayingPositionEntity(int currentPlayingPosition)
    {
        this.currentPlayingPosition = currentPlayingPosition;
    }

    public int getCurrentPlayingPosition()
    {
        return currentPlayingPosition;
    }

    public void setCurrentPlayingPosition(int currentPlayingPosition)
    {
        this.currentPlayingPosition = currentPlayingPosition;
    }
}
