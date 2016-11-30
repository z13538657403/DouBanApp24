package com.test.zhangtao.activitytest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.FriendEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 16/11/13.
 */
public class SearchFragment extends BaseFragment
{
    @ViewInject(R.id.search_result_recyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.tv_no_result)
    private TextView mTvNoResult;

    private SearchAdapter mAdapter;
    private List<FriendEntity> mDatas;
    private String mQueryText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search_friend , container , false);
        ViewUtils.inject(this , view);
        return view;
    }

    public void bindDatas(List<FriendEntity> datas)
    {
        this.mDatas = datas;
        mAdapter = new SearchAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        if (mQueryText != null)
        {
            mAdapter.getFilter().filter(mQueryText);
        }
    }

    //根据nextText进行查找，显示
    public void bindQueryText(String newText)
    {
        if (mDatas == null)
        {
            mQueryText = newText.toLowerCase();
        }
        else if (!TextUtils.isEmpty(newText))
        {
            mAdapter.getFilter().filter(newText.toLowerCase());
        }
    }


    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VH> implements Filterable
    {
        private List<FriendEntity> items = new ArrayList<>();

        public SearchAdapter()
        {
            items.clear();
            items.addAll(mDatas);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType)
        {
            final VH holder = new VH(LayoutInflater.from(getActivity()).inflate(R.layout.item_friend , parent , false));
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position = holder.getAdapterPosition();
                    Intent intent = new Intent();
                    intent.putExtra(NetUrlContacts.FRIEND_NAME , "@" + items.get(position).getName() + "：");
                    getActivity().setResult(Activity.RESULT_OK , intent);
                    getActivity().finish();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(VH holder, int position)
        {
            holder.userName.setText(items.get(position).getName());
            holder.userIcon.setImageURI(items.get(position).getUserIcon());
        }

        @Override
        public int getItemCount()
        {
            return items.size();
        }

        @Override
        public Filter getFilter()
        {
            return new Filter()
            {
                @Override
                protected FilterResults performFiltering(CharSequence constraint)
                {
                    ArrayList<FriendEntity> list = new ArrayList<>();
                    for (FriendEntity item : mDatas)
                    {
                        if (item.getPinyin().startsWith(constraint.toString()) || item.getName().contains(constraint))
                        {
                            list.add(item);
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.count = list.size();
                    results.values = list;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results)
                {
                    ArrayList<FriendEntity> list = (ArrayList<FriendEntity>) results.values;
                    items.clear();
                    items.addAll(list);
                    if (results.count == 0)
                    {
                        mTvNoResult.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mTvNoResult.setVisibility(View.INVISIBLE);
                    }
                    notifyDataSetChanged();
                }
            };
        }

        class VH extends RecyclerView.ViewHolder
        {
            private SimpleDraweeView userIcon;
            private TextView userName;

            public VH(View itemView)
            {
                super(itemView);
                userIcon = (SimpleDraweeView) itemView.findViewById(R.id.friend_icon);
                userName = (TextView) itemView.findViewById(R.id.friend_name);
            }
        }
    }
}
