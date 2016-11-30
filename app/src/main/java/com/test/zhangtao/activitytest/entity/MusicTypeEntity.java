package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/11/22.
 */
public class MusicTypeEntity
{
    private int topId;
    private int bgColorId;
    private String typeName;

    public MusicTypeEntity(int topId, int bgColorId, String typeName)
    {
        this.topId = topId;
        this.bgColorId = bgColorId;
        this.typeName = typeName;
    }

    public int getTopId()
    {
        return topId;
    }

    public void setTopId(int topId)
    {
        this.topId = topId;
    }

    public int getBgColorId()
    {
        return bgColorId;
    }

    public void setBgColorId(int bgColorId)
    {
        this.bgColorId = bgColorId;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
}
