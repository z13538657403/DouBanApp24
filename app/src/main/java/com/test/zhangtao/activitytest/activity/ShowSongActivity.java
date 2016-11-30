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
 * Created by zhangtao on 16/11/24.
 */
public class ShowSongActivity extends BaseActivity
{
    @ViewInject(R.id.show_song_toolBar)
    private DBToolBar mToolBar;

    private ShowSongFragment showSongFragment;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_song_activity);
        ViewUtils.inject(this);

        intent = getIntent();
        showSongFragment = (ShowSongFragment) getSupportFragmentManager().findFragmentById(R.id.show_song_fragment);
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
    }

    private void initFragment()
    {
        String musicType = intent.getStringExtra(NetUrlContacts.CURRENT_MUISC_TYPE);
        if (!TextUtils.isEmpty(musicType))
        {
            showSongFragment.getListData(musicType);
        }
    }
}
