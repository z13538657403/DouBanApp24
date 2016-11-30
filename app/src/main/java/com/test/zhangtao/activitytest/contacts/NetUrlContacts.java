package com.test.zhangtao.activitytest.contacts;

/**
 * Created by zhangtao on 16/11/10.
 */
public class NetUrlContacts
{
    public static final String PUBLIC_URL_MSG = "https://api.weibo.com/2/statuses/public_timeline.json";
    public static final String BASE_URL_FRIENDS = "https://api.weibo.com/2/friendships/followers.json";
    public static final String BASE_URL_FAVORITE = "https://api.weibo.com/2/favorites.json";
    public static final String CREATE_COMMENT_URL = "https://api.weibo.com/2/comments/create.json";
    public static final String MY_FRIENDS_URL = "https://api.weibo.com/2/friendships/friends.json";
    public static final String[] BUTTON_TITLES = {"打开相册(Open Gallery)" , "拍照(Camera)" , "裁剪(Crop)" , "编辑(Edit)"};
    public static final String[] STORE_LIST = {"删除收藏" , "收藏微博" , "屏蔽微博" , "举报微博"};
    public static final String STATUS_CREATE_URL = "https://api.weibo.com/2/statuses/update.json";
    public static final String STATUS_WITH_PIC = "https://api.weibo.com/2/statuses/upload.json";
    public static final String STATUS_REPOST_URL = "https://api.weibo.com/2/statuses/repost.json";
    public static final String ATTITUDES_CREATE_URL ="https://api.weibo.com/2/attitudes/create.json";
    public static final String ATTITUDES_DESTROY_URL ="https://api.weibo.com/2/attitudes/destroy.json";
    public static final String DELETE_FAVORITE = "https://api.weibo.com/2/favorites/destroy.json";
    public static final String CURRENT_USER_URL = "https://api.weibo.com/2/users/show.json";
    public static final String CREATE_APP_COMMENT = "https://api.weibo.com/2/comments/create.json";
    public static final String LOGIN_OUT_URL = "https://api.weibo.com/oauth2/revokeoauth2";

    public static final String LIST_OF_REPOST = "https://api.weibo.com/2/statuses/repost_timeline.json";
    public static final String LIST_OF_COMMENT = "https://api.weibo.com/2/comments/show.json";

    public static final String STATUS_CONTENT = "itemStatusData";
    public static final String COMMENT_OR_REPOST = "comment_or_rePost";
    public static final String IS_COMMENT = "comment";
    public static final String IS_REPOST = "repost";
    public static final String TO_COMMENT = "id";
    public static final int REQUEST_CODE = 0;
    public static final int REQUEST_STATUS_CODE = 1;
    public static final String FRIEND_NAME = "friend_name";
    public static final String RE_LOGIN = "restart home";

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";

    public static final int STATUS_REPOST_TAB = 3;
    public static final int STATUS_COMMENT_TAB = 4;
    public static final int STATUS_LIKE_TAB = 5;

    public static final String TAG_HOT_STRING = "hotSong";
    public static final String TO_PLAYING_ACTIVITY = "playing_music";
    public static final String CURRENT_SEEK_POSITION = "seek_position";
    public static final String CURRENT_TIME_REMAIN = "remain_time";

    public static final String BASE_URL = "https://route.showapi.com";
    public static final String showapi_appid = "27332";
    public static final String showapi_sign = "f6688c370e99489bbc9243520c04f90f";
    public static final int DEFUALT_TIMEOUT = 5;

    public static final String USER_INFO_ACTIVITY = "user_info";

    public static final String CURRENT_MUISC_TYPE = "music_type";
}
