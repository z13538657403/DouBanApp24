package com.test.zhangtao.activitytest.contacts;

import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.AppInfoEntity;
import com.test.zhangtao.activitytest.entity.MusicTypeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 16/10/20.
 */
public class TabContact
{
    public static final String[] TAB_TITLES = {"电影" , "音乐" , "微博"};
    public static final String[] PAGER_TITLE = {"主页" , "播放列表"};
    public static final String PLAY_ACTION = "com.test.zhangtao.activitytest.contacts.PLAY_ACTION";
    public static final String[] HEADERS = {"热门"};
    public static final String[] TITLES = {"按评论热度（从冷到热）" , "按评论时间（从新到旧）"};
    public static final String[] HEAD_TITLES = {"热门" , "最新"};
    public static final List<MusicTypeEntity> musicTypeList = new ArrayList<>();
    public static final List<AppInfoEntity> appInfoEntities = new ArrayList<>();

    static
    {
        MusicTypeEntity musicTypeEntity1 = new MusicTypeEntity(5 , R.color.grade_color5 , "内地");
        musicTypeList.add(musicTypeEntity1);
        MusicTypeEntity musicTypeEntity2 = new MusicTypeEntity(6 , R.color.grade_color4 , "港台");
        musicTypeList.add(musicTypeEntity2);
        MusicTypeEntity musicTypeEntity3 = new MusicTypeEntity(3 , R.color.grade_color3 , "欧美");
        musicTypeList.add(musicTypeEntity3);
        MusicTypeEntity musicTypeEntity4 = new MusicTypeEntity(16 , R.color.grade_color1 , "韩国");
        musicTypeList.add(musicTypeEntity4);
        MusicTypeEntity musicTypeEntity5 = new MusicTypeEntity(17 , R.color.grade_color2 , "日本");
        musicTypeList.add(musicTypeEntity5);
        MusicTypeEntity musicTypeEntity6 = new MusicTypeEntity(26 , R.color.grade_color6 , "热歌");
        musicTypeList.add(musicTypeEntity6);

        AppInfoEntity appInfoEntity1 = new AppInfoEntity(1 , "账号信息" , R.mipmap.ic_my_detail);
        appInfoEntities.add(appInfoEntity1);
        AppInfoEntity appInfoEntity2 = new AppInfoEntity(2 , "我的关注" , R.mipmap.ic_my_focus);
        appInfoEntities.add(appInfoEntity2);
        AppInfoEntity appInfoEntity3 = new AppInfoEntity(3 , "我的收藏" , R.mipmap.ic_star_like);
        appInfoEntities.add(appInfoEntity3);
        AppInfoEntity appInfoEntity4 = new AppInfoEntity(4 , "意见反馈" , R.mipmap.ic_mail_outlin);
        appInfoEntities.add(appInfoEntity4);
        AppInfoEntity appInfoEntity5 = new AppInfoEntity(5 , "设置" , R.mipmap.ic_settings_app);
        appInfoEntities.add(appInfoEntity5);
    }
}
