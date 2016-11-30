package com.test.zhangtao.activitytest.constant;

/**
 * Created by zhangtao on 16/11/9.
 */
public class CWConstant
{
    public static final String APP_KEY = "733721385";		   // 应用的APP_KEY
    public static final String SECRET_KEY = "d558a7a1e372e180759a73aca11b17da";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页
    public static final String SCOPE = 							   // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
}
