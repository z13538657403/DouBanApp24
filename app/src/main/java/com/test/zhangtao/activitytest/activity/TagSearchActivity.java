package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.widget.DBToolBar;

/**
 * Created by zhangtao on 16/11/19.
 */
public class TagSearchActivity extends BaseActivity
{
    @ViewInject(R.id.hot_search_toolbar)
    private DBToolBar mToolBar;

    private SearchSongFragment tagHotFragment;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hot_search_activity);
        ViewUtils.inject(this);

        intent = getIntent();
        initToolBar();
        initFragment();
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
        mToolBar.setTitle(intent.getStringExtra(NetUrlContacts.TAG_HOT_STRING));
    }

    private void initFragment()
    {
        tagHotFragment = (SearchSongFragment) getSupportFragmentManager().findFragmentById(R.id.hot_tag_fragment);
        if (!TextUtils.isEmpty(intent.getStringExtra(NetUrlContacts.TAG_HOT_STRING)))
        {
            tagHotFragment.doSearchSong(intent.getStringExtra(NetUrlContacts.TAG_HOT_STRING));
        }
    }
}
