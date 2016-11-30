package com.test.zhangtao.activitytest.activity;

import android.app.Application;
import android.support.v4.content.ContextCompat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.MusicEntity;
import com.test.zhangtao.activitytest.util.AppContextUtil;
import com.test.zhangtao.activitytest.util.FrescoImageLoader;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by zhangtao on 16/10/20.
 */
public class MyApplication extends Application
{
    private static MyApplication mInstance;
    private FunctionConfig functionConfig;
    private List<MusicEntity> musicPlayingList;
    
    public static MyApplication getInstance()
    {
        return mInstance;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        initConfig();
        Fresco.initialize(this);
        AppContextUtil.init(this);
    }

    public List<MusicEntity> getMusicPlayingList()
    {
        return musicPlayingList;
    }

    public void setMusicPlayingList(List<MusicEntity> musicPlayingList)
    {
        this.musicPlayingList = musicPlayingList;
    }

    private void initConfig() 
    {
        ThemeConfig themeConfig = new ThemeConfig.Builder()
                .setTitleBarBgColor(ContextCompat.getColor(this , R.color.heightqing))
                .setTitleBarTextColor(ContextCompat.getColor(this , R.color.white))
                .setTitleBarIconColor(ContextCompat.getColor(this , R.color.white))
                .setFabNornalColor(ContextCompat.getColor(this , R.color.heightqing))
                .setFabPressedColor(ContextCompat.getColor(this , R.color.press_fab))
                .setCheckNornalColor(ContextCompat.getColor(this , R.color.default_indexBar_textColor))
                .setCheckSelectedColor(ContextCompat.getColor(this , R.color.heightqing))
                .setIconBack(R.mipmap.back)
                .setIconRotate(R.mipmap.ic_crop_rotate)
                .setIconCrop(R.mipmap.ic_crop_free)
                .setIconCamera(R.mipmap.ic_monochrome_photos)
                .build();

        functionConfig= new FunctionConfig.Builder()
                .setMutiSelectMaxSize(4)
                .setEnableEdit(true)
                .setRotateReplaceSource(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setEnableCamera(true)
                .setEnablePreview(true).build();

        ImageLoader imageLoader = new FrescoImageLoader(this);
        CoreConfig coreConfig = new CoreConfig.Builder(this , imageLoader , themeConfig)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(false)
                .build();
        GalleryFinal.init(coreConfig);
    }

    public FunctionConfig getFunctionConfig()
    {
        return functionConfig;
    }
}
