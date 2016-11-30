package com.test.zhangtao.activitytest.entity;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Created by zhangtao on 16/11/13.
 */
public class FriendEntity implements IndexableEntity
{
    private long id;
    private String name;
    private String pinyin;
    private String userIcon;

    public FriendEntity() {}

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getUserIcon()
    {
        return userIcon;
    }

    public void setUserIcon(String userIcon)
    {
        this.userIcon = userIcon;
    }

    public String getPinyin()
    {
        return pinyin;
    }

    public void setPinyin(String pinyin)
    {
        this.pinyin = pinyin;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getFieldIndexBy()
    {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexField)
    {
        this.name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin)
    {
        this.pinyin = pinyin;
    }
}
