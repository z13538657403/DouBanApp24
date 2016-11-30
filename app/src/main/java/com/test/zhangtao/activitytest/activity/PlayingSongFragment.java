package com.test.zhangtao.activitytest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.widget.DBToolBar;
import de.greenrobot.event.EventBus;

/**
 * Created by zhangtao on 16/11/21.
 */
public class PlayingSongFragment extends BaseFragment
{
    @ViewInject(R.id.playing_song_icon)
    private SimpleDraweeView playingMusicImg;
    @ViewInject(R.id.music_playing_toolBar)
    private DBToolBar mToolBar;
    private String bigImgUri;

    public static PlayingSongFragment getInstance(String bigImgUrl)
    {
        PlayingSongFragment playingSongFragment = new PlayingSongFragment();
        playingSongFragment.bigImgUri= bigImgUrl;
        return playingSongFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.play_song_fragment , null);
        ViewUtils.inject(this , view);
        EventBus.getDefault().register(this);

        initToolBar();
        initView();
        return view;
    }

    private void initToolBar()
    {
        mToolBar.setLeftButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });
    }

    private void initView()
    {
        playingMusicImg.setImageURI(bigImgUri);
        int marginTop = getStatusBarHeight();
        setMargins(mToolBar , 0 , marginTop , 0 , 0);
    }

    public void onEventMainThread(MusicEntity musicEntity)
    {
        if (musicEntity != null)
        {
            playingMusicImg.setImageURI(musicEntity.getAlbumpic_big());
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
