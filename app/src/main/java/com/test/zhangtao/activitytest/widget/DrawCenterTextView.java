package com.test.zhangtao.activitytest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by zhangtao on 16/11/12.
 */
public class DrawCenterTextView extends TextView
{
    public DrawCenterTextView(Context context)
    {
        super(context);
    }

    public DrawCenterTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawCenterTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        if (null != drawableLeft)
        {
            setGravity(Gravity.START);
            setGravity(Gravity.CENTER_VERTICAL);
            int drawWidth = drawableLeft.getIntrinsicWidth();
            int drawPadding = getCompoundDrawablePadding();
            int textWidth = (int) getPaint().measureText(getText().toString());
            int bodyWidth = drawWidth + textWidth + drawPadding;
            canvas.translate((getWidth() - bodyWidth)/2 , 0);
        }

        Drawable drawableRight = drawables[2];
        if (null != drawableRight)
        {
            setGravity(Gravity.END);
            setGravity(Gravity.CENTER_VERTICAL);
            int drawWidth = drawableRight.getIntrinsicWidth();
            int drawPadding = getCompoundDrawablePadding();
            int textWidth = (int) getPaint().measureText(getText().toString());
            int bodyWidth = drawWidth + textWidth + drawPadding;
            canvas.translate(-(getWidth() - bodyWidth)/2 , 0);
        }
        super.onDraw(canvas);
    }
}
