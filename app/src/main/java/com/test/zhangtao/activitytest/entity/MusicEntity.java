package com.test.zhangtao.activitytest.entity;

import java.io.Serializable;

/**
 * Created by zhangtao on 16/10/21.
 */
public class MusicEntity implements Serializable , Comparable<MusicEntity>
{
    private String m4a;
    private String media_mid;
    private long songid;
    private long singerid;
    private String albumname;
    private String downUrl;
    private String singername;
    private String songname;
    private String albummid;
    private String songmid;
    private String albumpic_big;
    private String albumpic_small;
    private int albumid;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj != null && obj.getClass() == MusicEntity.class)
        {
            MusicEntity musicEntity = (MusicEntity) obj;
            if (this.getSongid() == musicEntity.getSongid())
            {
                return true;
            }
        }
        return false;
    }

    public String toString()
    {
        return "albumname = " + albumname + " songname = " + songname + " singername = " + singername +
                " " +  "\n";
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public String getAlbummid() {
        return albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlbumpic_big() {
        return albumpic_big;
    }

    public void setAlbumpic_big(String albumpic_big) {
        this.albumpic_big = albumpic_big;
    }

    public String getAlbumpic_small() {
        return albumpic_small;
    }

    public void setAlbumpic_small(String albumpic_small) {
        this.albumpic_small = albumpic_small;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getM4a() {
        return m4a;
    }

    public void setM4a(String m4a) {
        this.m4a = m4a;
    }

    public String getMedia_mid() {
        return media_mid;
    }

    public void setMedia_mid(String media_mid) {
        this.media_mid = media_mid;
    }

    public long getSingerid() {
        return singerid;
    }

    public void setSingerid(long singerid) {
        this.singerid = singerid;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public long getSongid() {
        return songid;
    }

    public void setSongid(long songid) {
        this.songid = songid;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname)
    {
        this.songname = songname;
    }

    @Override
    public int compareTo(MusicEntity another)
    {
        return this.songid > another.songid ? 1 : this.songid < another.songid ? -1 : 0;
    }
}
