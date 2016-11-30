package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.LoginOutEntity;
import com.test.zhangtao.activitytest.entity.VersionEntity;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.util.SPUtils;
import com.test.zhangtao.activitytest.widget.DBToolBar;

/**
 * Created by zhangtao on 16/11/27.
 */

public class LoginOutActivity extends BaseActivity
{
    @ViewInject(R.id.login_out_button)
    private Button loginOutButton;
    @ViewInject(R.id.user_setting_toolBar)
    private DBToolBar mToolBar;

    private SPUtils mSpUtils = SPUtils.getInstance(this);
    private Oauth2AccessToken mAccessToken;
    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_activity);
        ViewUtils.inject(this);

        loginOut();
        initToolBar();
    }

    @OnClick(R.id.about_the_app)
    private void aboutApp(View view)
    {
        Intent intent = new Intent(this , AppIntroduceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.check_new_version)
    public void checkVersion(View view)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest("http://192.168.1.101/version.json", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                if (!TextUtils.isEmpty(s))
                {
                    checkNewVersion(s);
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
            }
        });
        requestQueue.add(request);
    }

    private void checkNewVersion(String version)
    {
        gson = new Gson();
        VersionEntity versionEntity = gson.fromJson(version , VersionEntity.class);
        int nowVersionCode = versionEntity.getVersionCode();
        try
        {
            int currentVersionCode = getPackageManager().getPackageInfo(getPackageName() , 0).versionCode;
            if (nowVersionCode > currentVersionCode)
            {
                Toast.makeText(this , "检查到了新的版本，是否更新？" , Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this , "已经是最新版本！！！" , Toast.LENGTH_LONG).show();
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void initToolBar()
    {
        mToolBar.setLeftButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void loginOut()
    {
        mAccessToken = mSpUtils.getToken();
        loginOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mAccessToken != null && mAccessToken.isSessionValid())
                {
                    doLoginOut();
                }
            }
        });
    }

    private void doLoginOut()
    {
        blogHttpHelper.request(NetUrlContacts.LOGIN_OUT_URL, NetUrlContacts.METHOD_GET, null,
                new SimpleCallBack<LoginOutEntity>(this)
        {
            @Override
            public void onSuccess(String response, LoginOutEntity loginOutEntity)
            {
                if (loginOutEntity.getResult().equals("true"))
                {
                    Toast.makeText(LoginOutActivity.this , "退出成功" , Toast.LENGTH_LONG).show();
                    mSpUtils.clearToken();
                    Intent intent = new Intent();
                    intent.putExtra(NetUrlContacts.RE_LOGIN , "restart");
                    setResult(RESULT_OK , intent);
                    finish();
                }
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });
    }
}
