package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.AppInfoEntity;
import java.util.List;

/**
 * Created by zhangtao on 16/11/26.
 */

public class AppInfoAdapter extends SimpleAdapter<AppInfoEntity>
{
    public AppInfoAdapter(Context context , List<AppInfoEntity> datas)
    {
        super(context, R.layout.app_info_item , datas);
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, AppInfoEntity item)
    {
        viewHolder.getImageView(R.id.app_info_img).setImageResource(item.getItemIcon());
        viewHolder.getTextView(R.id.app_info_textView).setText(item.getItemName());
    }
}
