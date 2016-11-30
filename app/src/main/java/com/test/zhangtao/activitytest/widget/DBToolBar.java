package com.test.zhangtao.activitytest.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.test.zhangtao.activitytest.R;

/**
 * Created by zhangtao on 16/10/20.
 */
public class DBToolBar extends Toolbar
{
    private View mView;
    private LayoutInflater mLayoutInflater;
    private Button mLeftButton;
    private Button mRightButton;
    private SegmentTabLayout mSegmentTabLayout;
    private TextView mTextTitle;

    public DBToolBar(Context context)
    {
        this(context , null);
    }

    public DBToolBar(Context context, @Nullable AttributeSet attrs)
    {
        this(context , attrs , 0);
    }

    public DBToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
        setContentInsetsRelative(10 , 10);

        if (attrs != null)
        {
            final TintTypedArray array = TintTypedArray.obtainStyledAttributes(getContext() ,
                    attrs , R.styleable.DNToolBar , defStyleAttr , 0);

            final Drawable leftButtonIcon = array.getDrawable(R.styleable.DNToolBar_leftButtonIcon);
            if (leftButtonIcon != null)
            {
                setLeftButtonIcon(leftButtonIcon);
            }
            final String leftButtonText = array.getString(R.styleable.DNToolBar_leftButtonText);
            if (!TextUtils.isEmpty(leftButtonText))
            {
                setLeftButtonText(leftButtonText);
            }

            final Drawable rightButtonIcon = array.getDrawable(R.styleable.DNToolBar_rightButtonIcon);
            if (rightButtonIcon != null)
            {
                setRightButtonIcon(rightButtonIcon);
            }
            final String rightButtonText = array.getString(R.styleable.DNToolBar_rightButtonText);
            if (!TextUtils.isEmpty(rightButtonText))
            {
                setRightButtonText(rightButtonText);
            }

            boolean isShowTabLayout = array.getBoolean(R.styleable.DNToolBar_isShowTabLayout , false);
            if (isShowTabLayout)
            {
                showTabLayout();
                hideTitleView();
            }

            array.recycle();
        }
    }

    private void initView()
    {
        if (mView == null)
        {
            mLayoutInflater = LayoutInflater.from(getContext());
            mView = mLayoutInflater.inflate(R.layout.toolbar_layout , null);

            mLeftButton = (Button) mView.findViewById(R.id.tool_leftButton);
            mRightButton = (Button) mView.findViewById(R.id.tool_rightButton);
            mSegmentTabLayout = (SegmentTabLayout) mView.findViewById(R.id.segment_tabLayout);
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,
                    ViewGroup.LayoutParams.WRAP_CONTENT , Gravity.CENTER_HORIZONTAL);
            addView(mView , lp);
        }
    }

    //设置Title的属性
    @Override
    public void setTitle(CharSequence title)
    {
        initView();
        if (mTextTitle != null)
        {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    @Override
    public void setTitle(int resId)
    {
        setTitle(getContext().getText(resId));
    }

    public void showTitleView()
    {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView()
    {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);
    }

    //设置LeftButton的属性
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setLeftButtonIcon(Drawable leftButtonIcon)
    {
        if (mLeftButton != null)
        {
            mLeftButton.setBackground(leftButtonIcon);
        }
    }

    public void setLeftButtonIcon(int icon)
    {
        setLeftButtonIcon(getResources().getDrawable(icon));
    }

    private void setLeftButtonText(String leftButtonText)
    {
        mLeftButton.setText(leftButtonText);
    }

    public void setLeftButtonOnClickListener(OnClickListener li)
    {
        mLeftButton.setOnClickListener(li);
    }

    public Button getLeftButton()
    {
        return this.mLeftButton;
    }

    //设置RightButton的属性
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable rightButtonIcon)
    {
        if (mRightButton != null)
        {
            mRightButton.setBackground(rightButtonIcon);
        }
    }

    public void setRightButtonIcon(int icon)
    {
        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightButtonText(String rightButtonText)
    {
        if (!TextUtils.isEmpty(rightButtonText))
        {
            mRightButton.setText(rightButtonText);
        }
    }

    public void setRightButtonOnClickListener(OnClickListener li)
    {
        mRightButton.setOnClickListener(li);
    }

    public Button getRightButton()
    {
        return this.mRightButton;
    }

    //设置TabLayout的属性
    public void showTabLayout()
    {
        if (mSegmentTabLayout != null)
            mSegmentTabLayout.setVisibility(VISIBLE);
    }

    public void hideTabLayout()
    {
        if (mSegmentTabLayout != null)
            mSegmentTabLayout.setVisibility(GONE);
    }

    public void setOnTabItemSelectListener(OnTabSelectListener li)
    {
        mSegmentTabLayout.setOnTabSelectListener(li);
    }

    public SegmentTabLayout getTabLayout()
    {
        return mSegmentTabLayout;
    }
}
