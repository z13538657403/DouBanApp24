package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.constant.CWConstant;
import com.test.zhangtao.activitytest.util.SPUtils;

/**
 * Created by zhangtao on 16/11/9.
 */
public class LandingPageActivity extends BaseActivity
{
    private SsoHandler mSsoHandler;
    private AuthInfo mAutoInfo;
    private SPUtils mSpUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lauch_activity);

        mAutoInfo = new AuthInfo(getApplicationContext() , CWConstant.APP_KEY ,
                CWConstant.REDIRECT_URL , CWConstant.SCOPE);
        mSsoHandler = new SsoHandler(this , mAutoInfo);
        mSpUtils = SPUtils.getInstance(getApplicationContext());

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                checkLogin();
            }
        } , 500);
    }

    private void checkLogin()
    {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        if (mSpUtils.isLogin())
        {
            startActivity(new Intent(LandingPageActivity.this , MainActivity.class));
            finish();
        }
        else
        {
            mSsoHandler.authorize(new WeiboAuthListener()
            {
                @Override
                public void onComplete(Bundle bundle)
                {
                    Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(bundle);
                    mSpUtils.saveToken(token);
                    startActivity(new Intent(LandingPageActivity.this , MainActivity.class));
                    finish();
                }

                @Override
                public void onWeiboException(WeiboException e)
                {
                }

                @Override
                public void onCancel()
                {
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null)
        {
            mSsoHandler.authorizeCallBack(requestCode , resultCode , data);
        }
    }
}
