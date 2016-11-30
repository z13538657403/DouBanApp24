package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.PicUrlEntity;
import com.test.zhangtao.activitytest.entity.StatusEntity;
import com.test.zhangtao.activitytest.entity.UserEntity;
import com.test.zhangtao.activitytest.util.RichTextUtils;
import com.test.zhangtao.activitytest.util.TimeFormatUtils;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import com.test.zhangtao.activitytest.widget.DrawCenterTextView;

import java.util.List;

/**
 * Created by zhangtao on 16/11/26.
 */

public class UserDetailActivity extends BaseActivity
{
    @ViewInject(R.id.user_info_toolbar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.user_head_icon)
    private SimpleDraweeView mUserIcon;
    @ViewInject(R.id.current_user_name)
    private TextView currentUSerName;
    @ViewInject(R.id.current_user_detail)
    private TextView currentUserDetail;
    @ViewInject(R.id.user_blog_count)
    private TextView userBlogCount;
    @ViewInject(R.id.user_focus_count)
    private TextView userFocusCount;
    @ViewInject(R.id.user_friend_count)
    private TextView userFriendCount;

    @ViewInject(R.id.user_icon)
    private SimpleDraweeView userIcon;
    @ViewInject(R.id.status_user_name)
    private TextView userName;
    @ViewInject(R.id.status_create_time)
    private TextView createTime;
    @ViewInject(R.id.status_source)
    private TextView statusSource;
    @ViewInject(R.id.status_content_detail)
    private TextView statusContentText;
    @ViewInject(R.id.status_content_image)
    private SimpleDraweeView statusContentImg;
    @ViewInject(R.id.rePost_layout)
    private LinearLayout rePostLayout;
    @ViewInject(R.id.RePost_content)
    private TextView rePostContent;
    @ViewInject(R.id.RePost_content_image)
    private SimpleDraweeView rePostContentImg;
    @ViewInject(R.id.do_rePost)
    private DrawCenterTextView doRePostStatus;
    @ViewInject(R.id.do_comment)
    private DrawCenterTextView doCommentStatus;
    @ViewInject(R.id.do_like)
    private DrawCenterTextView doLikeStatus;

    private Intent intent;
    private StatusEntity lastStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_info_activity);
        ViewUtils.inject(this);

        intent = getIntent();
        initToolBar();
        initView();
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

    private void initView()
    {
        UserEntity userEntity = (UserEntity) intent.getSerializableExtra(NetUrlContacts.USER_INFO_ACTIVITY);
        if (userEntity == null)
        {
            Toast.makeText(this , "没有数据" , Toast.LENGTH_LONG).show();
            return;
        }
        mUserIcon.setImageURI(userEntity.profile_image_url);
        currentUSerName.setText(userEntity.screen_name);
        currentUserDetail.setText(userEntity.description);
        userBlogCount.setText("微博 " + userEntity.statuses_count);
        userFocusCount.setText("粉丝 " + userEntity.followers_count);
        userFriendCount.setText("关注 " + userEntity.friends_count);

        userName.setText(userEntity.screen_name);
        userIcon.setImageURI(userEntity.profile_image_url);

        lastStatus = userEntity.status;

        if (lastStatus != null)
        {
            createTime.setText(TimeFormatUtils.parseToYYMMDD(lastStatus.created_at));
            statusSource.setText(Html.fromHtml(lastStatus.source).toString());
            statusContentText.setText(RichTextUtils.getRichText(this , lastStatus.text));
            statusContentText.setMovementMethod(LinkMovementMethod.getInstance());

            doRePostStatus.setText(String.valueOf(lastStatus.reposts_count));
            doCommentStatus.setText(String.valueOf(lastStatus.comments_count));
            doLikeStatus.setText(String.valueOf(lastStatus.attitudes_count));

            List<PicUrlEntity> contentUrls = lastStatus.pic_urls;
            if (contentUrls != null && contentUrls.size() > 0)
            {
                PicUrlEntity pic = contentUrls.get(0);
                pic.original_pic = pic.thumbnail_pic.replace("thumbnail" , "large");
                pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail" , "bmiddle");

                statusContentImg.setVisibility(View.VISIBLE);
                statusContentImg.setImageURI(pic.bmiddle_pic);
            }
            else
            {
                statusContentImg.setVisibility(View.GONE);
            }

            if (lastStatus.retweeted_status != null)
            {
                String reContent = "@" + lastStatus.retweeted_status.user.screen_name + "：" + lastStatus.retweeted_status.text;
                rePostLayout.setVisibility(View.VISIBLE);
                rePostContent.setText(RichTextUtils.getRichText(this , reContent));
                rePostContent.setMovementMethod(LinkMovementMethod.getInstance());

                List<PicUrlEntity> reContentUrls = lastStatus.retweeted_status.pic_urls;
                if (reContentUrls != null && reContentUrls.size() > 0)
                {
                    rePostContentImg.setVisibility(View.VISIBLE);
                    rePostContentImg.setImageURI(reContentUrls.get(0).thumbnail_pic);
                }
                else
                {
                    rePostContentImg.setVisibility(View.GONE);
                }
            }
            else
            {
                rePostLayout.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.do_rePost)
    public void doRePost(View view)
    {
        Intent intent = new Intent(this , WriteCommentActivity.class);
        intent.putExtra(NetUrlContacts.TO_COMMENT , lastStatus.id);
        intent.putExtra(NetUrlContacts.COMMENT_OR_REPOST , NetUrlContacts.IS_REPOST);
        startActivity(intent);
    }

    @OnClick(R.id.do_comment)
    public void doComment(View view)
    {
        Intent intent = new Intent(this , WriteCommentActivity.class);
        intent.putExtra(NetUrlContacts.TO_COMMENT , lastStatus.id);
        intent.putExtra(NetUrlContacts.COMMENT_OR_REPOST , NetUrlContacts.IS_COMMENT);
        startActivity(intent);
    }
}
