package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sqk.emojirelease.FaceFragment;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.BlogCommentEntity;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/12.
 */
public class WriteCommentActivity extends BaseWriteActivity
{
    @ViewInject(R.id.blog_comment_toolBar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.blog_comment_content)
    private EditText mCommentContent;

    private boolean isShow = false;
    private Intent intent;
    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();
    private FaceFragment faceFragment = FaceFragment.Instance();
    private String actionType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.blog_com_activity);
        ViewUtils.inject(this);

        getSupportFragmentManager().beginTransaction().add(R.id.Container , faceFragment).hide(faceFragment).commit();
        intent = getIntent();
        initToolBar();
    }

    private void initToolBar()
    {
        actionType = intent.getStringExtra(NetUrlContacts.COMMENT_OR_REPOST);
        if (!TextUtils.isEmpty(actionType) && actionType.equals(NetUrlContacts.IS_COMMENT))
        {
            mToolBar.setTitle(R.string.boolBar_title_comment);
            mCommentContent.setHint(R.string.write_something);
        }
        else if (!TextUtils.isEmpty(actionType) && actionType.equals(NetUrlContacts.IS_REPOST))
        {
            mToolBar.setTitle(R.string.boolBar_title_rePost);
            mCommentContent.setHint(R.string.repost_status);
        }

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
                createComment();
            }
        });
    }

    @OnClick(R.id.at_someOne)
    public void atSomeOne(View view)
    {
        Intent intent = new Intent(this , FriendListActivity.class);
        startActivityForResult(intent , NetUrlContacts.REQUEST_CODE);
    }

    @OnClick(R.id.show_face_selector)
    public void showFaceSelector(View view)
    {
        if (isShow)
        {
            getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();
            isShow = false;
        }
        else
        {
            getSupportFragmentManager().beginTransaction().show(faceFragment).commit();
            isShow = true;
        }
    }

    private void createComment()
    {
        if (TextUtils.isEmpty(mCommentContent.getText().toString().trim()))
        {
            Toast.makeText(WriteCommentActivity.this , "请填写评论！！！" , Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String , Object> map = new HashMap<>(3);
        map.put("id" , intent.getLongExtra(NetUrlContacts.TO_COMMENT , 0));
        String url = "";

        if (actionType.equals(NetUrlContacts.IS_COMMENT))
        {
            map.put("comment" , mCommentContent.getText().toString());
            url = url + NetUrlContacts.CREATE_COMMENT_URL;
        }
        else
        {
            map.put("status" , mCommentContent.getText().toString() + "");
            url = url + NetUrlContacts.STATUS_REPOST_URL;
        }

        blogHttpHelper.request(url , NetUrlContacts.METHOD_POST,
                map, new SimpleCallBack<BlogCommentEntity>(this)
        {
            @Override
            public void onSuccess(String response, BlogCommentEntity blogCommentEntity)
            {
                Toast.makeText(WriteCommentActivity.this , "发送成功..." , Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null)
        {
            String resultData = data.getStringExtra(NetUrlContacts.FRIEND_NAME);
            String beforeData = mCommentContent.getText().toString();
            mCommentContent.setText(beforeData + resultData);
        }
    }

    @Override
    public EditText setEditContent()
    {
        return mCommentContent;
    }
}
