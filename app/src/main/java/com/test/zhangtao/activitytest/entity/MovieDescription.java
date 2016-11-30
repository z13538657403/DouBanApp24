package com.test.zhangtao.activitytest.entity;

import java.util.List;

/**
 * Created by zhangtao on 16/10/26.
 */
public class MovieDescription
{
    private String director;
    private String actor1;
    private String actor2;
    private String countryName;
    private String language;
    private String mainPicUrl;
    private String ratingSum;
    private String content;
    private String onLineTime;
    private String duration;

    public String toString()
    {
        return "导演：" + director + "\n" +
                "主演：" + actor1 + "/" + actor2 + "\n" +
                "制片国家/地区：" + countryName + "\n" +
                "语言：" + language + "\n" +
                "上映时间：" + onLineTime + "\n" +
                "片长：" + duration;
    }

    public String getOnLineTime() {
        return onLineTime;
    }

    public void setOnLineTime(String onLineTime) {
        this.onLineTime = onLineTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor1() {
        return actor1;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getActor2() {
        return actor2;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMainPicUrl() {
        return mainPicUrl;
    }

    public void setMainPicUrl(String mainPicUrl) {
        this.mainPicUrl = mainPicUrl;
    }

    public String getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(String ratingSum) {
        this.ratingSum = ratingSum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
