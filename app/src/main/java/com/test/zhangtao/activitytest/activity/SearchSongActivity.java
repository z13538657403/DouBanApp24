package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * Created by zhangtao on 16/11/18.
 */
public class SearchSongActivity extends BaseActivity
{
    private String[] searchTagTitles = new String[]{"莫西子诗" , "许巍" , "九月" , "陈壁" , "性空山" ,
            "赵雷" , "故乡" , "斑马", "我想和你虚度光阴" , "宋冬野"};
    private LayoutInflater mInflater;
    private SearchSongFragment searchSongFragment;

    @ViewInject(R.id.flow_layout)
    private TagFlowLayout mFlowLayout;
    @ViewInject(R.id.search_song_toolBar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.search_song_view)
    private SearchView mSearchView;
    @ViewInject(R.id.hot_search_layout)
    private LinearLayout hotSearchLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_song_activity);
        ViewUtils.inject(this);

        mInflater = LayoutInflater.from(this);
        searchSongFragment = (SearchSongFragment) getSupportFragmentManager().findFragmentById(R.id.song_result_fragment);

        initToolBar();
        initFlowLayout();
        initSearchView();
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

    private void initFlowLayout()
    {
        mFlowLayout.setAdapter(new TagAdapter<String>(searchTagTitles)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv , mFlowLayout , false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Intent intent = new Intent(SearchSongActivity.this , TagSearchActivity.class);
                intent.putExtra(NetUrlContacts.TAG_HOT_STRING , searchTagTitles[position]);
                startActivity(intent);
                return true;
            }
        });
    }

    private void initSearchView()
    {
        getSupportFragmentManager().beginTransaction().hide(searchSongFragment).commit();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchSongFragment.doSearchSong(query);
                if (searchSongFragment.isHidden())
                {
                    getSupportFragmentManager().beginTransaction().show(searchSongFragment).commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (newText.trim().length() > 0)
                {
                    hotSearchLayout.setVisibility(View.GONE);
                }
                else
                {
                    getSupportFragmentManager().beginTransaction().hide(searchSongFragment).commit();
                    hotSearchLayout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }
}
