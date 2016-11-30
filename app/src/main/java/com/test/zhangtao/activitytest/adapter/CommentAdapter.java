package com.test.zhangtao.activitytest.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.CommentEntity;

import java.util.List;

/**
 * Created by zhangtao on 16/10/26.
 */
public class CommentAdapter extends SimpleAdapter<CommentEntity>
{
    public CommentAdapter(Context context, int layoutResId, List<CommentEntity> datas)
    {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, CommentEntity item)
    {
        SimpleDraweeView commentIcon = (SimpleDraweeView) viewHolder.getView(R.id.user_image);
        commentIcon.setImageURI(item.getUserIconUrl());

        viewHolder.getTextView(R.id.user_name).setText(item.getUserName());
        viewHolder.getTextView(R.id.publish_date).setText(item.getPublishDate());
        viewHolder.getTextView(R.id.comment_count).setText(item.getCommentCount());
        viewHolder.getTextView(R.id.comment_content).setText(item.getCommentContent());
    }
}
