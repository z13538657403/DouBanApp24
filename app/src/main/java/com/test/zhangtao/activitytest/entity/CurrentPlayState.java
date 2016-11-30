package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/11/19.
 */
public class CurrentPlayState
{
    private int currentState;

    public CurrentPlayState(int currentState)
    {
        this.currentState = currentState;
    }

    public int getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(int currentState)
    {
        this.currentState = currentState;
    }
}
