package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/11/27.
 */

public class VersionEntity
{
    private String url;
    private int versionCode;
    private String updateMessage;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
