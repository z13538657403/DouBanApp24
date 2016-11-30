package com.test.zhangtao.activitytest.adapter;

/**
 * Created by zhangtao on 16/11/16.
 */
public class RePostCommentEntity
{
    public String iconImageUrl;
    public String userName;
    public String createTime;
    public String statusContent;

    public String toString()
    {
        return iconImageUrl + "----" + userName + "----" + createTime + "----" + statusContent;
    }
}
