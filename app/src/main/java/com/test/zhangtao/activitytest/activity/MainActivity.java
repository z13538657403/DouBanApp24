package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.AppInfoAdapter;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.MyPagerAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.contacts.TabContact;
import com.test.zhangtao.activitytest.entity.CurrentPlayState;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.entity.ProgressPosition;
import com.test.zhangtao.activitytest.entity.UserEntity;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.play.MusicPlayService;
import com.test.zhangtao.activitytest.util.SPUtils;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import com.test.zhangtao.activitytest.widget.DragLayout;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhangtao on 16/10/18.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener
{
    private String[] mTitles = TabContact.TAB_TITLES;
    private ArrayList<Fragment> mFragments;

    @ViewInject(R.id.drag_layout)
    private DragLayout dragLayout;
    @ViewInject(R.id.toolbar)
    private DBToolBar dbToolBar;
    @ViewInject(R.id.seekBar)
    private SeekBar seekBar;
    @ViewInject(R.id.play_pause)
    private ImageButton playPause;
    @ViewInject(R.id.next_song)
    private ImageButton nextSong;
    @ViewInject(R.id.music_icon)
    private SimpleDraweeView musicIcon;
    @ViewInject(R.id.songName)
    private TextView songName;
    @ViewInject(R.id.singerName)
    private TextView singerName;
    @ViewInject(R.id.time_remain)
    private TextView timeRemain;
    @ViewInject(R.id.f1_change)
    private ViewPager viewPager;
    @ViewInject(R.id.lv)
    private RecyclerView mAppInfoRecyclerView;
    @ViewInject(R.id.img_head)
    private CircleImageView userHeadIcon;
    @ViewInject(R.id.user_screen_name)
    private TextView userName;
    @ViewInject(R.id.user_detail_info)
    private TextView userDetailInfo;

    private SegmentTabLayout tabLayout;
    private Intent intent;
    private int currentSeekPosition = 0;
    private int currentTimeRemain = 0;
    private AppInfoAdapter mAdapter;
    private UserEntity userInfo;
    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();
    private SPUtils mSpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        setStatusBar();
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        mSpUtils = SPUtils.getInstance(this);

        initToolBar();
        initView();
        initAppInfoView();
        initUserInfo();
        initEvent();
    }

    private void initToolBar()
    {
        if (dbToolBar != null)
        {
            tabLayout = dbToolBar.getTabLayout();
            tabLayout.setTabData(mTitles);
        }

        dbToolBar.setLeftButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dragLayout.open();
            }
        });

        dbToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int action = Integer.parseInt(dbToolBar.getTag().toString());
                switch (action)
                {
                    case 0:
                        Toast.makeText(MainActivity.this , "MovieFragment" , Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Intent intent2 = new Intent(MainActivity.this , SearchSongActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(MainActivity.this , WriteBlogActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initView()
    {
        intent = new Intent(MainActivity.this , MusicPlayService.class);
        startService(intent);

        mFragments  = new ArrayList<>();
        mFragments.add(MovieFragment.getInstance());
        mFragments.add(MusicFragment.getInstance());
        mFragments.add(BlogFragment.getInstance());

        playPause.setOnClickListener(this);
        nextSong.setOnClickListener(this);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager() , mFragments , mTitles));
    }

    private void initAppInfoView()
    {
        mAdapter = new AppInfoAdapter(this , TabContact.appInfoEntities);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                switch (position)
                {
                    case 0:
                        Intent intent1 = new Intent(MainActivity.this , UserDetailActivity.class);
                        intent1.putExtra(NetUrlContacts.USER_INFO_ACTIVITY , userInfo);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(MainActivity.this , FriendListActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(MainActivity.this , FavoriteMusicActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        Intent intent4 = new Intent(MainActivity.this , WriteAppComActivity.class);
                        startActivity(intent4);
                        break;
                    case 4:
                        Intent intent5 = new Intent(MainActivity.this , LoginOutActivity.class);
                        startActivityForResult(intent5 , NetUrlContacts.REQUEST_CODE);
                        break;
                }
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dragLayout.close();
                    }
                } , 300);
            }
        });
        mAppInfoRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        mAppInfoRecyclerView.setAdapter(mAdapter);
    }

    private void initUserInfo()
    {
        Map<String , Object> map = new HashMap<>(2);
        map.put("uid" , mSpUtils.getToken().getUid());
        blogHttpHelper.request(NetUrlContacts.CURRENT_USER_URL, NetUrlContacts.METHOD_GET,
                map, new SimpleCallBack<UserEntity>(this)
        {
            @Override
            public void onSuccess(String response, UserEntity userEntity)
            {
                userInfo = userEntity;
                if (userInfo != null)
                {
                    setDataToUser();
                }
            }

            @Override
            public void onError(String error, int errorCode)
            {
            }
        });
    }

    private void setDataToUser()
    {
        Picasso.with(this).load(userInfo.profile_image_url).into(userHeadIcon);
        userName.setText(userInfo.screen_name);
        userDetailInfo.setText(userInfo.description);
    }

    private void initEvent()
    {
        dragLayout.setDragListener(new DragLayout.DragListener()
        {
            @Override
            public void onOpen()
            {
            }

            @Override
            public void onClose()
            {
            }

            @Override
            public void onDrag(float percent)
            {
            }
        });

        tabLayout.setOnTabSelectListener(new OnTabSelectListener()
        {
            @Override
            public void onTabSelect(int position)
            {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position)
            {
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                tabLayout.setCurrentTab(position);
                switch (position)
                {
                    case 0:
                        dbToolBar.setRightButtonIcon(R.mipmap.refresh_movie);
                        dbToolBar.setTag(0);
                        break;
                    case 1:
                        dbToolBar.setRightButtonIcon(R.mipmap.ic_search);
                        dbToolBar.setTag(1);
                        break;
                    case 2:
                        dbToolBar.setRightButtonIcon(R.mipmap.write_blog);
                        dbToolBar.setTag(2);
                        break;
                    default:
                        break;
                }

                if (position == 0)
                {
                    dragLayout.setIsDrag(true);
                }
                else
                {
                    dragLayout.setIsDrag(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        //创建Intent
        Intent intent = new Intent(TabContact.PLAY_ACTION);
        switch (view.getId())
        {
            case R.id.play_pause:
                intent.putExtra("control" , 1);
                break;
            case R.id.next_song:
                intent.putExtra("control" , 2);
                break;
        }
        //发送广播
        sendBroadcast(intent);
    }

    public void onEventMainThread(final MusicEntity musicEntity)
    {
        if (musicEntity != null)
        {
            musicIcon.setImageURI(musicEntity.getAlbumpic_small());
            songName.setText(musicEntity.getSongname());
            singerName.setText("-" + musicEntity.getSingername());

            musicIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startPlayActivity(musicEntity);
                }
            });
        }
    }

    private void startPlayActivity(MusicEntity musicEntity)
    {
        Intent intent = new Intent(this , PlayingSongActivity.class);
        intent.putExtra(NetUrlContacts.TO_PLAYING_ACTIVITY , musicEntity);
        intent.putExtra(NetUrlContacts.CURRENT_SEEK_POSITION , currentSeekPosition);
        intent.putExtra(NetUrlContacts.CURRENT_TIME_REMAIN , currentTimeRemain);
        startActivity(intent);
    }

    public void onEventMainThread(ProgressPosition progressPosition)
    {
        if (progressPosition != null)
        {
            double currentProgress = ((double)progressPosition.getCurrentPosition()) /
                    ((double)(progressPosition.getDuration()));
            currentSeekPosition = (int) (currentProgress * 100);
            seekBar.setProgress(currentSeekPosition);
            currentTimeRemain = progressPosition.getDuration() - progressPosition.getCurrentPosition();
            timeRemain.setText("-" + (currentTimeRemain/60) + ":" + (currentTimeRemain%60));
        }
    }

    public void onEventMainThread(CurrentPlayState currentPlayState)
    {
        if (currentPlayState != null)
        {
            if (currentPlayState.getCurrentState() == 0x11 || currentPlayState.getCurrentState() == 0x13)
            {
                playPause.setImageResource(R.drawable.start_btn);
            }
            else if (currentPlayState.getCurrentState() == 0x12)
            {
                playPause.setImageResource(R.drawable.pause_btn);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null)
        {
            startActivity(new Intent(MainActivity.this , LandingPageActivity.class));
            finish();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopService(intent);
    }
}

