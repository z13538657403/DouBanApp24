package com.test.zhangtao.activitytest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.CommentAdapter;
import com.test.zhangtao.activitytest.adapter.ListDropDownAdapter;
import com.test.zhangtao.activitytest.contacts.TabContact;
import com.test.zhangtao.activitytest.entity.CommentEntity;
import com.test.zhangtao.activitytest.http.ContentParse;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;
import com.yyydjk.library.DropDownMenu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangtao on 16/10/28.
 */
public class CommentActivity extends BaseActivity
{
    @ViewInject(R.id.comment_toolbar)
    private DBToolBar dbToolBar;
    @ViewInject(R.id.dropdownMenu)
    private DropDownMenu mDropDownMenu;
    @ViewInject(R.id.comment2_list)
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout commentRefreshLayout;
    private ListView chooseList;
    private View contentView;

    private String url1;
    private String url2;

    private static final int STATE_HOT = 0;
    private static final int STATE_LAST = 1;
    private int commentState = 0;
    private boolean isShowed = false;
    private int currentDataPosition = 0;
    private int loadDataSize = 20;

    private static final int STATE_NORMOL = 1;
    private static final int STATE_LOADMORE = 2;
    private static final int STATE_REFRESH = 3;
    private int loadState = 1;

    private List<CommentEntity> commentEntityList;
    private List<View> popupViews = new ArrayList<>();
    private ListDropDownAdapter chooseAdapter;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment_activity);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        url1 = intent.getStringExtra("url") + "comments?start=" + currentDataPosition + "&limit=" + loadDataSize +"&sort=new_score";
        url2 = intent.getStringExtra("url") + "comments?start=" + currentDataPosition +"&limit=" + loadDataSize +"&sort=time";

        initToolBar();
        initView();
        getData(url1);
        initEvent();
    }

    private void initToolBar()
    {
        dbToolBar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (mDropDownMenu.isShowing())
                {
                    mDropDownMenu.closeMenu();
                }
                CommentActivity.this.finish();
            }
        });
    }

    private void initView()
    {
        chooseList = new ListView(this);
        chooseAdapter = new ListDropDownAdapter(this , Arrays.asList(TabContact.TITLES));
        chooseList.setDividerHeight(0);
        chooseList.setAdapter(chooseAdapter);
        popupViews.add(chooseList);
        contentView = LayoutInflater.from(this).inflate(R.layout.comment_list_layout , null);
        commentRefreshLayout = (MaterialRefreshLayout) contentView.findViewById(R.id.comment_refresh_layout);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.comment2_list);
        mDropDownMenu.setDropDownMenu(Arrays.asList(TabContact.HEADERS) , popupViews , contentView);
    }


    private void getData(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                commentEntityList = ContentParse.parseComment(s);
                if (commentEntityList != null)
                {
                    setData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
            }
        });
        requestQueue.add(request);
    }

    private void setData()
    {
        switch (loadState)
        {
            case STATE_NORMOL:
                if (!isShowed)
                {
                    commentAdapter = new CommentAdapter(this, R.layout.comment_item2, commentEntityList);
                    mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(commentAdapter);
                    isShowed = true;
                }
                else
                {
                    commentAdapter.clearData();
                    commentAdapter.addData(commentEntityList);
                    mRecyclerView.scrollToPosition(0);
                }
                break;
            case STATE_REFRESH:
                commentAdapter.clearData();
                commentAdapter.addData(commentEntityList);
                mRecyclerView.scrollToPosition(0);
                commentRefreshLayout.finishRefresh();
                break;
            case STATE_LOADMORE:
                commentAdapter.addData(commentAdapter.getDatas().size() , commentEntityList);
                mRecyclerView.scrollToPosition(commentAdapter.getDatas().size());
                commentRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void refreshData()
    {
        currentDataPosition = 0;
        loadDataSize = 20;
        loadState = STATE_REFRESH;
        if (commentState == STATE_HOT)
        {
            getData(url1);
        }
        else
        {
            getData(url2);
        }
    }

    private void loadMoreData()
    {
        currentDataPosition = commentEntityList.size() + currentDataPosition;
        loadDataSize = 10;
        loadState = STATE_LOADMORE;
        if (commentState == STATE_HOT)
        {
            getData(url1);
        }
        else
        {
            getData(url2);
        }
    }

    private void initEvent()
    {
        commentRefreshLayout.setLoadMore(true);
        commentRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener()
        {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout)
            {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout)
            {
                loadMoreData();
            }
        });

        chooseList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                chooseAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? TabContact.HEADERS[0] : TabContact.HEAD_TITLES[position]);
                mDropDownMenu.closeMenu();

                if (commentState != position && position == STATE_HOT)
                {
                    loadState = STATE_NORMOL;
                    currentDataPosition = 0;
                    loadDataSize = 20;
                    getData(url1);
                    commentState = STATE_HOT;
                }
                else if (commentState != position && position == STATE_LAST)
                {
                    loadState = STATE_NORMOL;
                    currentDataPosition = 0;
                    loadDataSize = 20;
                    getData(url2);
                    commentState = STATE_LAST;
                }
            }
        });
    }
}
