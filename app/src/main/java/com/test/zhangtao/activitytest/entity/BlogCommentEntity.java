package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/11/11.
 */
public class BlogCommentEntity
{
    public String created_at;
    public long id;
    public long rootid;
    public int floor_number;
    public String text;
    public int source_allowclick;
    public int source_type;
    public String source;
    public UserEntity user;
    public String mid;
    public String idstr;
    public StatusEntity status;
    public BlogCommentEntity reply_comment;
    public String reply_original_text;
}
