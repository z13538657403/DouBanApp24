package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/10/23.
 */
public class ProgressPosition
{
    private int duration;
    private int currentPosition;

    public ProgressPosition(int duration, int currentPosition)
    {
        this.duration = duration;
        this.currentPosition = currentPosition;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
