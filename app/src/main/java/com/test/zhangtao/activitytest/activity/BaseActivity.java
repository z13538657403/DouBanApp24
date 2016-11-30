package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.test.zhangtao.activitytest.R;

import java.lang.reflect.Field;

/**
 * Created by zhangtao on 16/11/9.
 */
public class BaseActivity extends AppCompatActivity
{
    @Override
    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_in_right , R.anim.anim_out_right);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.anim_in_left , R.anim.anim_out_left);
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

    protected void setStatusBar()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.linear_bar);
            if (linear_bar != null)
            {
                linear_bar.setVisibility(View.VISIBLE);
                int statusHeight = getStatusBarHeight();
                android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) linear_bar.getLayoutParams();
                params.height = statusHeight;
                linear_bar.setLayoutParams(params);
            }
        }
    }
}
