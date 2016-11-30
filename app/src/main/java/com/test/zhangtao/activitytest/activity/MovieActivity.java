package com.test.zhangtao.activitytest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.CommentAdapter;
import com.test.zhangtao.activitytest.entity.CommentEntity;
import com.test.zhangtao.activitytest.entity.MovieDescription;
import com.test.zhangtao.activitytest.http.ContentParse;
import com.test.zhangtao.activitytest.widget.DBToolBar;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by zhangtao on 16/10/25.
 */
public class MovieActivity extends BaseActivity
{
    @ViewInject(R.id.toolbar_movie)
    private DBToolBar dbToolBar;
    @ViewInject(R.id.film_title)
    private TextView filmTitle;
    @ViewInject(R.id.film_info)
    private TextView filmInfo;
    @ViewInject(R.id.movie_pic)
    private SimpleDraweeView moviePic;
    @ViewInject(R.id.rating_sum)
    private TextView ratingSum;
    @ViewInject(R.id.expand_text_view)
    private ExpandableTextView expTv;
    @ViewInject(R.id.comment_list)
    private RecyclerView commentList;
    @ViewInject(R.id.more_comment)
    private LinearLayout moreComment;
    private ProgressDialog progressDialog;

    private MovieDescription movieDescription;
    private List<CommentEntity> commentEntityList;

    private String url1;
    private String url2;
    private String movieName;
    private static final int REQUEST_CONTENT = 0;
    private static final int REQUEST_COMMENT = 1;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.movie_activity);
        ViewUtils.inject(this);

        initToolBar();
        initView();
        initEvent();
    }

    private void initToolBar()
    {
        dbToolBar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        dbToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showShare();
            }
        });
    }

    private void initView()
    {
        Intent intent = getIntent();
        url1 = intent.getStringExtra("url");
        url2 = url1 + "comments?start=0&limit=3&sort=new_score";
        movieName = intent.getStringExtra("title");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载.....");
        progressDialog.show();

        getData(url1 , REQUEST_CONTENT);
        getData(url2 , REQUEST_COMMENT);
    }

    private void getData(String url , final int type)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                parseData(s, type);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
            }
        });
        requestQueue.add(request);
    }

    private void parseData(String s, int type)
    {
        switch (type)
        {
            case REQUEST_CONTENT:
                movieDescription = ContentParse.parseContent(s);
                if (movieDescription != null)
                    setData();
                break;
            case REQUEST_COMMENT:
                commentEntityList = ContentParse.parseComment(s);
                if (commentEntityList != null)
                    setCommentData();
                break;
        }
    }

    private void setData()
    {
        filmTitle.setText(movieName);
        filmInfo.setText(movieDescription.toString());
        moviePic.setImageURI(movieDescription.getMainPicUrl());
        ratingSum.setText(movieDescription.getRatingSum());
        expTv.setText(movieDescription.getContent());
    }

    private void setCommentData()
    {
        commentAdapter = new CommentAdapter(this , R.layout.comment_item , commentEntityList);
        commentList.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        commentList.setAdapter(commentAdapter);
        progressDialog.dismiss();
    }


    private void initEvent()
    {
        moreComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MovieActivity.this , CommentActivity.class);
                intent.putExtra("url" , url1);
                startActivity(intent);
            }
        });
    }

    private void showShare()
    {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(movieName);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url1);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(movieDescription.getContent());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(movieDescription.getMainPicUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url1);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }
}
