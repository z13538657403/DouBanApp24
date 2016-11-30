package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.test.zhangtao.activitytest.R;
import java.util.List;

/**
 * Created by zhangtao on 16/10/29.
 */
public class ListDropDownAdapter extends BaseAdapter
{
    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;
    private LayoutInflater mflater;

    public void setCheckItem(int position)
    {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ListDropDownAdapter(Context context , List<String> list)
    {
        this.context = context;
        mflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;

        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mflater.inflate(R.layout.item_default_drop_down , parent , false);
            viewHolder.mText = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        fillValue(position , viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder)
    {
        viewHolder.mText.setText(list.get(position));
        if (checkItemPosition != -1)
        {
            if (checkItemPosition == position)
            {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.heightqing));
                viewHolder.mText.setBackgroundResource(R.color.check_bg);
            }
            else
            {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.result_view));
                viewHolder.mText.setBackgroundResource(R.color.white);
            }
        }
    }

    static class ViewHolder
    {
        TextView mText;
    }
}
