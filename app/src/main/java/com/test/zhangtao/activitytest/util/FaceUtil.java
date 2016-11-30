package com.test.zhangtao.activitytest.util;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.sqk.emojirelease.Emoji;
import com.sqk.emojirelease.FaceFragment;

/**
 * Created by zhangtao on 16/11/15.
 */
public class FaceUtil implements FaceFragment.OnEmojiClickListener
{
    private EditText mEditContent;
    public FaceUtil(EditText editContent)
    {
        this.mEditContent = editContent;
    }
    
    @Override
    public void onEmojiDelete()
    {
        String text = mEditContent.getText().toString();
        if (TextUtils.isEmpty(text))
        {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1 , text.length())))
        {
            int index = text.lastIndexOf("[");
            if (index == -1)
            {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action , code);
                mEditContent.onKeyDown(KeyEvent.KEYCODE_DEL , event);
                return;
            }
            mEditContent.getText().delete(index , text.length());
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action , code);
        mEditContent.onKeyDown(KeyEvent.KEYCODE_DEL , event);
    }

    @Override
    public void onEmojiClick(Emoji emoji)
    {

    }
}
