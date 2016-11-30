package com.test.zhangtao.activitytest.entity;

import java.util.List;

/**
 * Created by zhangtao on 16/11/23.
 */
public class SecondPageBean
{
    private List<SecondMusicEntity> songlist;
    private int total_song_num;
    private int ret_code;
    private long color;
    private int cur_song_num;
    private int currentPage;
    private int song_begin;
    private int totalpage;

    public List<SecondMusicEntity> getSonglist() {
        return songlist;
    }

    public void setSonglist(List<SecondMusicEntity> songlist) {
        this.songlist = songlist;
    }

    public int getTotal_song_num() {
        return total_song_num;
    }

    public void setTotal_song_num(int total_song_num) {
        this.total_song_num = total_song_num;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public int getCur_song_num() {
        return cur_song_num;
    }

    public void setCur_song_num(int cur_song_num) {
        this.cur_song_num = cur_song_num;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSong_begin() {
        return song_begin;
    }

    public void setSong_begin(int song_begin) {
        this.song_begin = song_begin;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }
}
