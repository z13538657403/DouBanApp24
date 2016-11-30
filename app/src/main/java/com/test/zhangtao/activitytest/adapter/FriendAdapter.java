package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.entity.FriendEntity;

import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * Created by zhangtao on 16/11/13.
 */
public class FriendAdapter extends IndexableAdapter<FriendEntity>
{
    private LayoutInflater mInflater;

    public FriendAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent)
    {
        View view = mInflater.inflate(R.layout.item_index_friend , parent , false);
        return new IndexVH(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent)
    {
        View view = mInflater.inflate(R.layout.item_friend, parent , false);
        return new ContentVH(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle)
    {
        IndexVH vh = (IndexVH) holder;
        vh.tvIndex.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, FriendEntity entity)
    {
        ContentVH vh = (ContentVH) holder;
        vh.userName.setText(entity.getName());
        vh.userIcon.setImageURI(entity.getUserIcon());
    }

    private class IndexVH extends RecyclerView.ViewHolder
    {
        TextView tvIndex;

        public IndexVH(View itemView)
        {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }

    private class ContentVH extends RecyclerView.ViewHolder
    {
        SimpleDraweeView userIcon;
        TextView userName;

        public ContentVH(View itemView)
        {
            super(itemView);
            userIcon = (SimpleDraweeView) itemView.findViewById(R.id.friend_icon);
            userName = (TextView) itemView.findViewById(R.id.friend_name);
        }
    }
}
