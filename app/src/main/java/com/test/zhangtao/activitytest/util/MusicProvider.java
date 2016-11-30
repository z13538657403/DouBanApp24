package com.test.zhangtao.activitytest.util;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.test.zhangtao.activitytest.entity.MusicEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangtao on 16/11/24.
 */
public class MusicProvider
{
    private LinkedList<MusicEntity> data = null;
    private Context mContext;
    private static final String MUSIC_JSON = "music_json";
    private static MusicProvider musicProviderInstance;

    private MusicProvider()
    {
        data = new LinkedList<>();
    }

    public static MusicProvider getInstance()
    {
        if (musicProviderInstance == null)
        {
            musicProviderInstance = new MusicProvider();
        }
        return musicProviderInstance;
    }

    public void setMyContext(Context context)
    {
        mContext = context;
        listToLink();
    }

    public void put(MusicEntity musicEntity)
    {
        if (data.contains(musicEntity))
        {
            Toast.makeText(mContext , "该歌曲已经在收藏夹里了" , Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (data.size() >= 30)
            {
                data.addLast(musicEntity);
                data.removeFirst();
            }
            else
            {
                data.addLast(musicEntity);
            }
        }
        commit();
    }

    public void delete(MusicEntity musicEntity)
    {
        data.remove(musicEntity);
        commit();
    }

    public LinkedList<MusicEntity> getAll()
    {
        return getDataFromLocal();
    }

    public void clear()
    {
        data.clear();
        commit();
    }

    private void commit()
    {
        LinkedList<MusicEntity> musics = linkToList();
        PreferencesUtils.putString(mContext , MUSIC_JSON , JsonUtil.toJson(musics));
    }

    private LinkedList<MusicEntity> linkToList()
    {
        int size = data.size();
        LinkedList<MusicEntity> list = new LinkedList<>();
        for (int i = 0 ; i < size ; i++)
        {
            list.addLast(data.get(i));
        }
        return list;
    }

    private void listToLink()
    {
        data.clear();
        LinkedList<MusicEntity> musics = getDataFromLocal();
        if (musics != null && musics.size() > 0)
        {
            for (MusicEntity musicEntity : musics)
            {
                data.addLast(musicEntity);
            }
        }
    }

    private LinkedList<MusicEntity> getDataFromLocal()
    {
        String json = PreferencesUtils.getString(mContext , MUSIC_JSON);
        LinkedList<MusicEntity> musics = null;
        if (json != null)
        {
            musics = JsonUtil.fromJson(json , new TypeToken<LinkedList<MusicEntity>>(){}.getType());
        }
        return musics;
    }
}
