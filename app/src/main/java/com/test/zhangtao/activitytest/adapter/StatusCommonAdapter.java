package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.PicUrlEntity;
import com.test.zhangtao.activitytest.entity.StatusEntity;
import com.test.zhangtao.activitytest.util.RichTextUtils;
import com.test.zhangtao.activitytest.util.TimeFormatUtils;
import com.test.zhangtao.activitytest.widget.DrawCenterTextView;

import java.util.List;

/**
 * Created by zhangtao on 16/11/11.
 */
public class StatusCommonAdapter extends SimpleAdapter<StatusEntity>
{
    private StatusCommonListener listener;

    public StatusCommonAdapter(Context context , List<StatusEntity> datas , StatusCommonListener listener)
    {
        super(context, R.layout.item_weibo_content , datas);
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHodler viewHolder, final StatusEntity item)
    {
        LinearLayout llRe = (LinearLayout) viewHolder.getView(R.id.llRe);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.ivHeader);
        SimpleDraweeView simpleDraweeView1 = (SimpleDraweeView) viewHolder.getView(R.id.ivContent);
        SimpleDraweeView simpleDraweeView2 = (SimpleDraweeView) viewHolder.getView(R.id.ivReContent);

        ImageView imageView = viewHolder.getImageView(R.id.ivOperator);

        viewHolder.getTextView(R.id.tvUserName).setText(item.user.screen_name);
        viewHolder.getTextView(R.id.tvTime).setText(TimeFormatUtils.parseToYYMMDD(item.created_at));
        viewHolder.getTextView(R.id.tvSource).setText(Html.fromHtml(item.source).toString());
        viewHolder.getTextView(R.id.tvContent).setText(RichTextUtils.getRichText(context , item.text));
        viewHolder.getTextView(R.id.tvContent).setMovementMethod(LinkMovementMethod.getInstance());

        DrawCenterTextView tvRetweet = (DrawCenterTextView) viewHolder.getView(R.id.tvRetweet);
        DrawCenterTextView tvComment = (DrawCenterTextView) viewHolder.getView(R.id.tvComment);
        DrawCenterTextView tvLike = (DrawCenterTextView) viewHolder.getView(R.id.tvLike);
        tvRetweet.setText(String.valueOf(item.reposts_count));
        tvComment.setText(String.valueOf(item.comments_count));
        tvLike.setText(String.valueOf(item.attitudes_count));

        tvRetweet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.toRePost(item.id);
                }
            }
        });

        tvComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.toComment(item.id);
                }
            }
        });

        tvLike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.toGiveLike(item.id);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.toFavorites(item.id);
                }
            }
        });

        if (item.user.profile_image_url != null)
            simpleDraweeView.setImageURI(item.user.profile_image_url);

        List<PicUrlEntity> contentUrls = item.pic_urls;
        if (contentUrls != null && contentUrls.size() > 0)
        {
            PicUrlEntity pic = contentUrls.get(0);
            pic.original_pic = pic.thumbnail_pic.replace("thumbnail" , "large");
            pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail" , "bmiddle");

            simpleDraweeView1.setVisibility(View.VISIBLE);
            simpleDraweeView1.setImageURI(pic.bmiddle_pic);
        }
        else
        {
            simpleDraweeView1.setVisibility(View.GONE);
        }

        if (item.retweeted_status != null)
        {
            String reContent = "@" + item.retweeted_status.user.screen_name + "：" + item.retweeted_status.text;
            llRe.setVisibility(View.VISIBLE);
            viewHolder.getTextView(R.id.tvReContent).setText(RichTextUtils.getRichText(context , reContent));
            viewHolder.getTextView(R.id.tvReContent).setMovementMethod(LinkMovementMethod.getInstance());

            List<PicUrlEntity> reContentUrls = item.retweeted_status.pic_urls;
            if (reContentUrls != null && reContentUrls.size() > 0)
            {
                simpleDraweeView2.setVisibility(View.VISIBLE);
                simpleDraweeView2.setImageURI(reContentUrls.get(0).thumbnail_pic);
            }
            else
            {
                simpleDraweeView2.setVisibility(View.GONE);
            }
        }
        else
        {
            llRe.setVisibility(View.GONE);
        }
    }

    public interface StatusCommonListener
    {
        void toRePost(long id);
        void toComment(long id);
        void toGiveLike(long id);
        void toFavorites(long id);
    }
}
