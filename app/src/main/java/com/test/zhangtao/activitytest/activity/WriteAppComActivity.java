package com.test.zhangtao.activitytest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.BlogCommentEntity;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.widget.DBToolBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/27.
 */

public class WriteAppComActivity extends BaseActivity
{
    @ViewInject(R.id.app_comment_toolBar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.write_comment_edit)
    private EditText mEditText;

    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_comment_activity);
        ViewUtils.inject(this);

        initToolBar();
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

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (TextUtils.isEmpty(mEditText.getText()))
                {
                    Toast.makeText(WriteAppComActivity.this , "请输入您的宝贵意见！！！" , Toast.LENGTH_LONG).show();
                }
                else
                {
                    postComment();
                }
            }
        });
    }

    private void postComment()
    {
        Map<String , Object> map = new HashMap<>(3);
        map.put("id" , "4046433168520192");
        map.put("comment" , mEditText.getText().toString());

        blogHttpHelper.request(NetUrlContacts.CREATE_APP_COMMENT, NetUrlContacts.METHOD_POST, map,
                new SimpleCallBack<BlogCommentEntity>(this)
        {
            @Override
            public void onSuccess(String response, BlogCommentEntity blogCommentEntity)
            {
                Toast.makeText(WriteAppComActivity.this , "提交成功..." , Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });

    }
}
