package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.StatusCommonAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.decoration.CardViewItemDecortion;
import com.test.zhangtao.activitytest.entity.FavoriteEntities;
import com.test.zhangtao.activitytest.entity.StatusEntity;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.util.MyActionSheet;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 16/10/20.
 */
public class BlogFragment extends BaseFragment
{
    @ViewInject(R.id.refresh_status)
    private MaterialRefreshLayout refreshLayout;
    @ViewInject(R.id.content_recyclerView)
    private RecyclerView mBlogCommentRecyclerView;

    private static final int LOAD_COUNT = 10;
    private int currentPage = 1;
    private int totalCount;
    private int totalPage;

    private static final int STATE_NORMOL = 1;
    private static final int STATE_LOADMORE = 2;
    private static final int STATE_REFRESH = 3;
    private int loadState = STATE_NORMOL;

    private StatusCommonAdapter mAdapter;
    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();
    private List<StatusEntity> data;

    public static BlogFragment getInstance()
    {
        BlogFragment bf = new BlogFragment();
        return bf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.book_fragment , container , false);
        ViewUtils.inject(this , v);
        getActivity().setTheme(R.style.ActionSheetStyleiOS7);
        getData();
        initEvent();

        return v;
    }

    private void initEvent()
    {
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener()
        {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout)
            {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout)
            {
                if (currentPage <= totalPage)
                {
                    loadMore();
                }
                else
                {
                    Toast.makeText(getContext() , "没有下一页数据了" , Toast.LENGTH_SHORT).show();
                    refreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData()
    {
        currentPage = 1;
        loadState = STATE_REFRESH;
        getData();
    }

    private void loadMore()
    {
        currentPage = currentPage + 1;
        loadState = STATE_LOADMORE;
        getData();
    }

    private void getData()
    {
        Map<String , Object> map = new HashMap<>(3);
        map.put("count" , LOAD_COUNT);
        map.put("page" , currentPage);

        blogHttpHelper.request(NetUrlContacts.BASE_URL_FAVORITE , NetUrlContacts.METHOD_GET ,
                map , new SimpleCallBack<FavoriteEntities>(getActivity())
                {
                    @Override
                    public void onSuccess(String response, FavoriteEntities favoriteEntities)
                    {
                        if (favoriteEntities != null)
                        {
                            totalCount = favoriteEntities.total_number;
                            totalPage = totalCount/10 + 1;
                            List<StatusEntity> statusEntities = new ArrayList<>();
                            for (int i = 0 ; i < favoriteEntities.favorites.size() ; i++)
                            {
                                statusEntities.add(favoriteEntities.favorites.get(i).status);
                            }
                            data = statusEntities;
                            initView();
                        }
                    }

                    @Override
                    public void onError(String error, int errorCode)
                    {
                    }
                });
    }

    private void initView()
    {
        switch (loadState)
        {
            case STATE_NORMOL:
                mAdapter = new StatusCommonAdapter(getActivity(), data, new StatusCommonAdapter.StatusCommonListener()
                {
                    @Override
                    public void toRePost(long id)
                    {
                        Intent intent = new Intent(getActivity() , WriteCommentActivity.class);
                        intent.putExtra(NetUrlContacts.TO_COMMENT , id);
                        intent.putExtra(NetUrlContacts.COMMENT_OR_REPOST , NetUrlContacts.IS_REPOST);
                        startActivityForResult(intent , NetUrlContacts.REQUEST_CODE);
                    }

                    @Override
                    public void toComment(long id)
                    {
                        Intent intent = new Intent(getActivity() , WriteCommentActivity.class);
                        intent.putExtra(NetUrlContacts.TO_COMMENT , id);
                        intent.putExtra(NetUrlContacts.COMMENT_OR_REPOST , NetUrlContacts.IS_COMMENT);
                        startActivityForResult(intent , NetUrlContacts.REQUEST_CODE);
                    }

                    @Override
                    public void toGiveLike(long id)
                    {
                        giveLikeOrNot(id);
                    }

                    @Override
                    public void toFavorites(long id)
                    {
                        showActionSheet(id);
                    }
                });

                mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        StatusEntity statusEntity = mAdapter.getItem(position);
                        Intent intent = new Intent(getActivity() , StatusContentActivity.class);
                        intent.putExtra(NetUrlContacts.STATUS_CONTENT , statusEntity);
                        startActivity(intent);
                    }
                });

                mBlogCommentRecyclerView.setAdapter(mAdapter);
                mBlogCommentRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity() ,
                        LinearLayoutManager.VERTICAL , false));
                mBlogCommentRecyclerView.addItemDecoration(new CardViewItemDecortion());
                break;
            case STATE_REFRESH:
                mAdapter.clearData();
                mAdapter.addData(data);
                mBlogCommentRecyclerView.scrollToPosition(0);
                refreshLayout.finishRefresh();
                break;
            case STATE_LOADMORE:
                mAdapter.addData(mAdapter.getData().size() , data);
                mBlogCommentRecyclerView.scrollToPosition(mAdapter.getData().size());
                refreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void showActionSheet(final long id)
    {
        new MyActionSheet(getActivity() , getActivity().getSupportFragmentManager(), NetUrlContacts.STORE_LIST)
        {
            @Override
            protected void actionDetail(int index)
            {
                switch (index)
                {
                    case 0:
                        delFavorite(id);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void delFavorite(long id)
    {
        Map<String , Object> map = new HashMap<>(2);
        map.put("id" , id);
        blogHttpHelper.request(NetUrlContacts.DELETE_FAVORITE, NetUrlContacts.METHOD_POST, map,
                new SimpleCallBack<String>(getActivity())
        {
            @Override
            public void onSuccess(String response, String s)
            {
                Toast.makeText(getActivity() , "删除收藏成功！！！" , Toast.LENGTH_SHORT).show();
                currentPage = 1;
                getData();
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });
    }

    private void giveLikeOrNot(long id)
    {
        Map<String , Object> map = new HashMap<>(3);
        map.put("id" , id);
        blogHttpHelper.request(NetUrlContacts.ATTITUDES_CREATE_URL, NetUrlContacts.METHOD_POST, map,
                new SimpleCallBack<String>(getActivity())
        {
            @Override
            public void onSuccess(String response, String s)
            {
                Log.d("ISkooo" , s);
            }

            @Override
            public void onError(String error, int errorCode)
            {
                Toast.makeText(getActivity() , error + "--" + errorCode , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("Back" , "I am Back");
        getData();
    }
}
