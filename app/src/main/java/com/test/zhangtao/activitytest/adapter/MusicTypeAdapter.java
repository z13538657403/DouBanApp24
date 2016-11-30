package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.widget.TextView;

import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.MusicTypeEntity;

import java.util.List;

/**
 * Created by zhangtao on 16/11/22.
 */
public class MusicTypeAdapter extends SimpleAdapter<MusicTypeEntity>
{
    public MusicTypeAdapter(Context context , List<MusicTypeEntity> datas)
    {
        super(context, R.layout.type_tv_item , datas);
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, MusicTypeEntity item)
    {
        TextView typeTv = viewHolder.getTextView(R.id.music_type_tv);
        typeTv.setText(item.getTypeName());
        typeTv.setBackgroundColor(context.getResources().getColor(item.getBgColorId()));
    }
}
