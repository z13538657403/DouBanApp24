package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Log;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.util.RichTextUtils;
import com.test.zhangtao.activitytest.util.TimeFormatUtils;

import java.util.List;

/**
 * Created by zhangtao on 16/11/16.
 */
public class RepostStatusAdapter extends SimpleAdapter<RePostCommentEntity>
{
    public RepostStatusAdapter(Context context , List<RePostCommentEntity> datas)
    {
        super(context, R.layout.status_repost_layout , datas);
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, RePostCommentEntity item)
    {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.repost_user_icon);
        draweeView.setImageURI(item.iconImageUrl);

        viewHolder.getTextView(R.id.repost_user_name).setText(item.userName);
        viewHolder.getTextView(R.id.repost_time).setText(TimeFormatUtils.parseToYYMMDD(item.createTime));
        viewHolder.getTextView(R.id.repost_status_content).setText(RichTextUtils.getRichText(context , item.statusContent));
        viewHolder.getTextView(R.id.repost_status_content).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
