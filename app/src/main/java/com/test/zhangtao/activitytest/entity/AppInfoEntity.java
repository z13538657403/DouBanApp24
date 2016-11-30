package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/11/26.
 */

public class AppInfoEntity 
{
    private int id;
    private String itemName;
    private int itemIcon;

    public AppInfoEntity(int id, String itemName, int itemIcon)
    {
        this.id = id;
        this.itemName = itemName;
        this.itemIcon = itemIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
