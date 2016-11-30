package com.test.zhangtao.activitytest.entity;

import java.util.List;

/**
 * Created by zhangtao on 16/10/21.
 */
public class PageBean
{
    private String w;
    private int allPages;
    private int ret_code;
    private List<MusicEntity> contentlist;
    private int currentPage;
    private String notice;
    private int allNum;
    private int maxResult;

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public List<MusicEntity> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<MusicEntity> contentlist) {
        this.contentlist = contentlist;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }
}
