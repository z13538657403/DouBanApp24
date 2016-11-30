package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;

import java.lang.reflect.Field;

/**
 * Created by zhangtao on 16/11/9.
 */
public class BaseFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startActivity(Intent intent)
    {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_in_right , R.anim.anim_out_right);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode)
    {
        getActivity().startActivityForResult(intent , requestCode);
        getActivity().overridePendingTransition(R.anim.anim_in_right , R.anim.anim_out_right);
    }

    protected int getStatusBarHeight()
    {
        try
        {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public void setMargins (View v, int l, int t, int r, int b)
    {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
