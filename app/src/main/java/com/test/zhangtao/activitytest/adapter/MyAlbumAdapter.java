package com.test.zhangtao.activitytest.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;

import java.util.List;

/**
 * Created by zhangtao on 16/10/21.
 */
public class MyAlbumAdapter extends SimpleAdapter<AlbumEntity>
{

    public MyAlbumAdapter(Context context, int layoutResId, List<AlbumEntity> datas)
    {
        super(context, layoutResId , datas);
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, AlbumEntity item)
    {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(item.getAlbumUrl());

        viewHolder.getTextView(R.id.album_name).setText(item.getAlbumName());
        viewHolder.getTextView(R.id.song_count).setText(item.getSongCount() + "首");
    }
}
