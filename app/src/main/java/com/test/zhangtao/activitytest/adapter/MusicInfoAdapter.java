package com.test.zhangtao.activitytest.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.MusicEntity;

import java.util.List;

/**
 * Created by zhangtao on 16/11/18.
 */
public class MusicInfoAdapter extends SimpleAdapter<MusicEntity>
{
    public MusicInfoAdapter(Context context , List<MusicEntity> datas)
    {
        super(context, R.layout.temp_song_item , datas);
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, MusicEntity item)
    {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.image_of_song);
        simpleDraweeView.setImageURI(item.getAlbumpic_small());
        viewHolder.getTextView(R.id.name_of_song).setText(item.getSongname());
        viewHolder.getTextView(R.id.name_of_singer).setText(item.getSingername());
    }
}
