package com.test.zhangtao.activitytest.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.FriendAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.FriendEntity;
import com.test.zhangtao.activitytest.entity.UserEntity;
import com.test.zhangtao.activitytest.entity.Userentities;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.util.SPUtils;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * Created by zhangtao on 16/11/13.
 */
public class FriendListActivity extends BaseActivity
{
    private List<FriendEntity> mDatas = new ArrayList<>();
    private FriendAdapter adapter;
    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();
    private SPUtils mSpUtils;

    private SearchFragment mSearchFragment;
    @ViewInject(R.id.pick_friend_toolBar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.searchView)
    private SearchView mSearchView;
    @ViewInject(R.id.indexableLayout)
    private IndexableLayout indexableLayout;
    @ViewInject(R.id.progress)
    private FrameLayout mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pick_friend);
        ViewUtils.inject(this);
        mSpUtils = SPUtils.getInstance(this);

        initToolBar();
        getData();
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

    private void getData()
    {
        Map<String , Object> map = new HashMap<>(2);
        map.put("uid" , mSpUtils.getToken().getUid());
        map.put("count" , 100 + "");
        blogHttpHelper.request(NetUrlContacts.MY_FRIENDS_URL, NetUrlContacts.METHOD_GET, map ,
                new SimpleCallBack<Userentities>(this)
        {
            @Override
            public void onSuccess(String response, Userentities userentities)
            {
                userToFriend(userentities);
                initView();
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });
    }

    private void userToFriend(Userentities userentities)
    {
        for (UserEntity userEntity : userentities.users)
        {
            FriendEntity friendEntity = new FriendEntity();
            friendEntity.setName(userEntity.screen_name);
            friendEntity.setUserIcon(userEntity.profile_image_url);
            mDatas.add(friendEntity);
        }
    }

    private void initView()
    {
        mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        adapter = new FriendAdapter(this);
        indexableLayout.setAdapter(adapter);
        adapter.setDatas(mDatas, new IndexableAdapter.IndexCallback<FriendEntity>()
        {
            @Override
            public void onFinished(List<FriendEntity> datas)
            {
                //数据处理完成后回调
                mSearchFragment.bindDatas(mDatas);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        //set Listener
        adapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<FriendEntity>()
        {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, FriendEntity entity)
            {
                Intent intent = new Intent();
                intent.putExtra(NetUrlContacts.FRIEND_NAME , "@" + entity.getName() + "：");
                setResult(RESULT_OK , intent);
                finish();
            }
        });

        adapter.setOnItemTitleClickListener(new IndexableAdapter.OnItemTitleClickListener()
        {
            @Override
            public void onItemClick(View v, int currentPosition, String indexTitle)
            {
                Toast.makeText(FriendListActivity.this , "选中：" + indexTitle + " 当前位置：" +
                        currentPosition , Toast.LENGTH_SHORT).show();
            }
        });

        //搜索功能初始化
        initSearch();
    }

    private void initSearch()
    {
        getSupportFragmentManager().beginTransaction().hide(mSearchFragment).commit();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (newText.trim().length() > 0)
                {
                    if (mSearchFragment.isHidden())
                    {
                        getSupportFragmentManager().beginTransaction().show(mSearchFragment).commit();
                    }
                }
                else
                {
                    if (!mSearchFragment.isHidden())
                    {
                        getSupportFragmentManager().beginTransaction().hide(mSearchFragment).commit();
                    }
                }
                mSearchFragment.bindQueryText(newText);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (!mSearchFragment.isHidden())
        {
            //隐藏搜索
            mSearchView.setQuery(null , false);
            getSupportFragmentManager().beginTransaction().hide(mSearchFragment).commit();
            return;
        }
        super.onBackPressed();
    }
}
