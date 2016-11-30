package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.Subject;

import java.util.List;

/**
 * Created by zhangtao on 16/10/20.
 */
public class MovieAdapter extends SimpleAdapter<Subject>
{

    public MovieAdapter(Context context, int layoutResId, List<Subject> datas)
    {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, Subject item)
    {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.movie_theme);
        draweeView.setImageURI(Uri.parse(item.getImages().getMedium()));

        viewHolder.getTextView(R.id.title).setText(item.getTitle());

        StringBuilder sb = new StringBuilder();
        for (String genre : item.getGenres())
        {
            sb.append(genre + "，");
        }

        viewHolder.getTextView(R.id.genres).setText(sb.substring(0 , sb.lastIndexOf("，")));
        viewHolder.getTextView(R.id.time).setText(item.getYear() + "上映");
        viewHolder.getTextView(R.id.score).setText(item.getCollect_count() + "");
    }
}
