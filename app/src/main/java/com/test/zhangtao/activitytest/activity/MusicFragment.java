package com.test.zhangtao.activitytest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.AlbumEntity;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.MusicTypeAdapter;
import com.test.zhangtao.activitytest.adapter.MyAlbumAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.contacts.TabContact;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.entity.MusicPlayingEntity;
import com.test.zhangtao.activitytest.entity.MusicTypeEntity;
import com.test.zhangtao.activitytest.entity.PageBean;
import com.test.zhangtao.activitytest.http.MusicMethods;
import com.test.zhangtao.activitytest.widget.DrawCenterTextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Subscriber;

/**
 * Created by zhangtao on 16/10/21.
 */
public class MusicFragment extends BaseFragment
{
    @ViewInject(R.id.list_image)
    private RecyclerView imageList;
    @ViewInject(R.id.music_type_recyclerView)
    private RecyclerView musicTypeList;

    private ProgressDialog progressDialog;
    private Subscriber<PageBean> subscriber;
    private SparseArray<AlbumEntity> albums;
    private MyAlbumAdapter adapter;
    private List<MusicTypeEntity> musicTypeData = TabContact.musicTypeList;
    private MusicTypeAdapter musicTypeAdapter;
    private ShowSongFragment rollMusicFragment;
    private ShowSongFragment folkMusicFragment;

    public static MusicFragment getInstance()
    {
        MusicFragment sf = new MusicFragment();
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.get_more_roll , R.id.get_more_folk})
    public void getMoreMusic(View view)
    {
        Intent intent = new Intent(getActivity() , ShowSongActivity.class);
        switch (view.getId())
        {
            case R.id.get_more_roll:
                intent.putExtra(NetUrlContacts.CURRENT_MUISC_TYPE , 19 + "");
                break;
            case R.id.get_more_folk:
                intent.putExtra(NetUrlContacts.CURRENT_MUISC_TYPE , 18 + "");
                break;
        }
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.music_fragment , null);
        ViewUtils.inject(this , v);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.....");
        albums = new SparseArray<>();

        setMusicTypeView();
        getData();
        rollMusicFragment = new ShowSongFragment();
        folkMusicFragment = new ShowSongFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.roll_music_container , rollMusicFragment).commit();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.folk_music_container , folkMusicFragment).commit();
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showFragmentData();
    }

    private void showFragmentData()
    {
        rollMusicFragment.getListData("" + 19 , 3);
        folkMusicFragment.getListData("" + 18 , 3);
    }

    private void setMusicTypeView()
    {
        musicTypeAdapter = new MusicTypeAdapter(getActivity() , musicTypeData);
        musicTypeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(getActivity() , ShowSongActivity.class);
                intent.putExtra(NetUrlContacts.CURRENT_MUISC_TYPE , musicTypeAdapter.getItem(position).getTopId() + "");
                startActivity(intent);
            }
        });

        musicTypeList.setLayoutManager(new GridLayoutManager(getActivity() , 3));
        musicTypeList.setAdapter(musicTypeAdapter);
    }

    private void getData()
    {
        progressDialog.show();

        subscriber = new Subscriber<PageBean>()
        {
            @Override
            public void onCompleted()
            {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e)
            {
                progressDialog.dismiss();
            }

            @Override
            public void onNext(PageBean pageBean)
            {
                initImageList(pageBean);
                postMusic(pageBean);
            }
        };
        MusicMethods.getInstance().getMusicPageBean(subscriber , 1 , "陈壁");
    }

    private void postMusic(PageBean pageBean)
    {
        List<MusicEntity> tempList = new ArrayList<>();
        for (MusicEntity musicEntity : pageBean.getContentlist())
        {
            if (!musicEntity.getAlbumname().equals(""))
            {
                tempList.add(musicEntity);
            }
        }
        MyApplication.getInstance().setMusicPlayingList(tempList);
        MusicPlayingEntity musicPlayingEntity = new MusicPlayingEntity();
        musicPlayingEntity.setMusicPlayingList(tempList);
        musicPlayingEntity.setCurrentPlayingPosition(0);
        EventBus.getDefault().post(musicPlayingEntity);
    }

    private void initImageList(PageBean pageBean)
    {
        for (MusicEntity musicEntity : pageBean.getContentlist())
        {
            if (!musicEntity.getAlbumname().equals(""))
            {
                if (albums.get(musicEntity.getAlbumid()) == null)
                {
                    List<MusicEntity> tempList = new ArrayList<>();
                    tempList.add(musicEntity);
                    AlbumEntity albumEntity = new AlbumEntity();
                    albumEntity.setSongCount(1);
                    albumEntity.setAlbumName(musicEntity.getAlbumname());
                    albumEntity.setAlbumUrl(musicEntity.getAlbumpic_big());
                    albumEntity.setMusicEntities(tempList);
                    albums.put(musicEntity.getAlbumid(), albumEntity);
                }
                else
                {
                    AlbumEntity albumEntity = albums.get(musicEntity.getAlbumid());
                    albumEntity.setSongCount(albumEntity.getSongCount() + 1);
                    List<MusicEntity> tempList = albumEntity.getMusicEntities();
                    tempList.add(musicEntity);
                    albumEntity.setMusicEntities(tempList);
                }
            }
        }
        initView();
    }

    private void initView()
    {
        int size = albums.size();
        List<AlbumEntity> albumEntity = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++)
        {
            albumEntity.add(albums.valueAt(i));
        }

        adapter = new MyAlbumAdapter(getActivity() , R.layout.album_item , albumEntity);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Toast.makeText(getActivity() , "" + position , Toast.LENGTH_SHORT).show();
            }
        });
        imageList.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        imageList.setAdapter(adapter);
    }
}
