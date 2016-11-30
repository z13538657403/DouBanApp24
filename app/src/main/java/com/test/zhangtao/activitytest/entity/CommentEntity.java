package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/10/26.
 */
public class CommentEntity
{
    private String userName;
    private String userIconUrl;
    private String commentCount;
    private String publishDate;
    private String commentContent;

    public String toString()
    {
        return  userName + "\n" +
                userIconUrl +  "\n" +
                commentCount + "\n" +
                publishDate + "\n" +
                commentContent + "\n" +
                "------------------" + "\n";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
