package com.test.zhangtao.activitytest.adapter;

import com.test.zhangtao.activitytest.entity.MusicEntity;

import java.util.List;

/**
 * Created by zhangtao on 16/10/21.
 */
public class AlbumEntity
{
    private int songCount;
    private String albumName;
    private String albumUrl;
    private List<MusicEntity> musicEntities;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public List<MusicEntity> getMusicEntities() {
        return musicEntities;
    }

    public void setMusicEntities(List<MusicEntity> musicEntities) {
        this.musicEntities = musicEntities;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}
