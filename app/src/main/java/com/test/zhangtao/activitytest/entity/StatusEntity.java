package com.test.zhangtao.activitytest.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangtao on 16/11/11.
 */
public class StatusEntity implements Serializable
{
    public String created_at;
    public long id;
    public String mid;
    public String idstr;
    public String text;
    public int source_allowclick;
    public int source_type;
    public String source;
    public boolean favorited;
    public boolean truncated;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    public List<PicUrlEntity> pic_urls;
    public String thumbnail_pic;
    public String bmiddle_pic;
    public String original_pic;
    public Object geo;
    public UserEntity user;
    public StatusEntity retweeted_status;
    public int reposts_count;
    public int comments_count;
    public int attitudes_count;
    public boolean isLongText;
    public int mlevel;
}
