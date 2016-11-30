package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import com.test.zhangtao.activitytest.adapter.RePostCommentEntity;
import com.test.zhangtao.activitytest.adapter.RepostStatusAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.BlogCommentEntities;
import com.test.zhangtao.activitytest.entity.BlogCommentEntity;
import com.test.zhangtao.activitytest.entity.PicUrlEntity;
import com.test.zhangtao.activitytest.entity.RePostEntity;
import com.test.zhangtao.activitytest.entity.StatusEntity;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.util.RichTextUtils;
import com.test.zhangtao.activitytest.util.TimeFormatUtils;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import com.test.zhangtao.activitytest.widget.DrawCenterTextView;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/16.
 */
public class StatusContentActivity extends BaseActivity implements TabLayout.OnTabSelectedListener
{
    @ViewInject(R.id.status_content_toolbar)
    private DBToolBar mToolBar;
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
    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;
    @ViewInject(R.id.comment_recyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.result_is_null)
    private TextView showNull;
    
    private StatusEntity statusEntity;
    private int currentTab = NetUrlContacts.STATUS_REPOST_TAB;
    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();
    private RepostStatusAdapter mAdapter;
    private List<RePostCommentEntity> rePostCommentEntities = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_status_content);
        ViewUtils.inject(this);

        statusEntity = (StatusEntity) getIntent().getSerializableExtra(NetUrlContacts.STATUS_CONTENT);
        
        initToolBar();
        setViewData();
        initTab();
    }

    private void initTab()
    {
        if (statusEntity == null)
        {
            Toast.makeText(this , "数据为空，请重新加载数据！！！" , Toast.LENGTH_SHORT).show();
            return;
        }
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("转发 " + statusEntity.reposts_count);
        tab.setTag(NetUrlContacts.STATUS_REPOST_TAB);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("评论 " + statusEntity.comments_count);
        tab.setTag(NetUrlContacts.STATUS_COMMENT_TAB);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("赞 " + statusEntity.attitudes_count);
        tab.setTag(NetUrlContacts.STATUS_LIKE_TAB);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);

        getRePostTabData();
    }

    private void setViewData() 
    {
        if (statusEntity == null)
        {
            Toast.makeText(this , "数据为空，请重新加载数据！！！" , Toast.LENGTH_SHORT).show();
            return;
        }
        userName.setText(statusEntity.user.screen_name);
        createTime.setText(TimeFormatUtils.parseToYYMMDD(statusEntity.created_at));
        statusSource.setText(Html.fromHtml(statusEntity.source).toString());
        statusContentText.setText(RichTextUtils.getRichText(this , statusEntity.text));
        statusContentText.setMovementMethod(LinkMovementMethod.getInstance());

        doRePostStatus.setText(String.valueOf(statusEntity.reposts_count));
        doCommentStatus.setText(String.valueOf(statusEntity.comments_count));
        doLikeStatus.setText(String.valueOf(statusEntity.attitudes_count));

        if (statusEntity.user.profile_image_url != null)
            userIcon.setImageURI(statusEntity.user.profile_image_url);

        List<PicUrlEntity> contentUrls = statusEntity.pic_urls;
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

        if (statusEntity.retweeted_status != null)
        {
            String reContent = "@" + statusEntity.retweeted_status.user.screen_name + "：" + statusEntity.retweeted_status.text;
            rePostLayout.setVisibility(View.VISIBLE);
            rePostContent.setText(RichTextUtils.getRichText(this , reContent));
            rePostContent.setMovementMethod(LinkMovementMethod.getInstance());

            List<PicUrlEntity> reContentUrls = statusEntity.retweeted_status.pic_urls;
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

    @OnClick(R.id.do_rePost)
    public void doRePost(View view)
    {
        Intent intent = new Intent(this , WriteCommentActivity.class);
        intent.putExtra(NetUrlContacts.TO_COMMENT , statusEntity.id);
        intent.putExtra(NetUrlContacts.COMMENT_OR_REPOST , NetUrlContacts.IS_REPOST);
        startActivity(intent);
    }

    @OnClick(R.id.do_comment)
    public void doComment(View view)
    {
        Intent intent = new Intent(this , WriteCommentActivity.class);
        intent.putExtra(NetUrlContacts.TO_COMMENT , statusEntity.id);
        intent.putExtra(NetUrlContacts.COMMENT_OR_REPOST , NetUrlContacts.IS_COMMENT);
        startActivity(intent);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        currentTab = (int) tab.getTag();
        getData();
    }

    private void getData()
    {
        switch (currentTab)
        {
            case NetUrlContacts.STATUS_REPOST_TAB:
                getRePostTabData();
                break;
            case NetUrlContacts.STATUS_COMMENT_TAB:
                getCommentTabData();
                break;
            case NetUrlContacts.STATUS_LIKE_TAB:
                mRecyclerView.setVisibility(View.GONE);
                showNull.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void getRePostTabData()
    {
        Map<String , Object> map = new HashMap<>(3);
        map.put("id" , statusEntity.id);
        blogHttpHelper.request(NetUrlContacts.LIST_OF_REPOST , NetUrlContacts.METHOD_GET,
                map, new SimpleCallBack<RePostEntity>(this)
        {
            @Override
            public void onSuccess(String response, RePostEntity rePostEntity)
            {
                if (rePostEntity != null)
                {
                    convertToRePost(rePostEntity.reposts);
                    initRePostView();
                }
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });
    }

    private void getCommentTabData()
    {
        Map<String , Object> map = new HashMap<>(3);
        map.put("id" , statusEntity.id);
        map.put("count" , 10);
        blogHttpHelper.request(NetUrlContacts.LIST_OF_COMMENT, NetUrlContacts.METHOD_GET, map,
                new SimpleCallBack<BlogCommentEntities>(this)
        {
            @Override
            public void onSuccess(String response, BlogCommentEntities blogCommentEntities)
            {
                if (blogCommentEntities != null)
                {
                    convertToComment(blogCommentEntities.comments);
                    initRePostView();
                }
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {
    }

    private void initRePostView()
    {
        if (rePostCommentEntities == null || rePostCommentEntities.size() <= 0)
        {
            mRecyclerView.setVisibility(View.GONE);
            showNull.setVisibility(View.VISIBLE);
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        showNull.setVisibility(View.GONE);
        mAdapter = new RepostStatusAdapter(this, rePostCommentEntities);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new WrapContentLinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void convertToRePost(List<StatusEntity> reposts)
    {
        rePostCommentEntities.clear();
        for (StatusEntity statusEntity : reposts)
        {
            RePostCommentEntity rePostCommentEntity = new RePostCommentEntity();
            rePostCommentEntity.iconImageUrl = statusEntity.user.profile_image_url;
            rePostCommentEntity.userName = statusEntity.user.screen_name;
            rePostCommentEntity.createTime = statusEntity.created_at;
            rePostCommentEntity.statusContent = statusEntity.text;
            rePostCommentEntities.add(rePostCommentEntity);
        }
    }

    private void convertToComment(List<BlogCommentEntity> comments)
    {
        rePostCommentEntities.clear();
        for (BlogCommentEntity blogCommentEntity : comments)
        {
            RePostCommentEntity rePostCommentEntity = new RePostCommentEntity();
            rePostCommentEntity.iconImageUrl = blogCommentEntity.user.profile_image_url;
            rePostCommentEntity.userName = blogCommentEntity.user.screen_name;
            rePostCommentEntity.createTime = blogCommentEntity.created_at;
            rePostCommentEntity.statusContent = blogCommentEntity.text;
            rePostCommentEntities.add(rePostCommentEntity);
        }
    }
}
