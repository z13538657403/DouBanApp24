package com.test.zhangtao.activitytest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.sqk.emojirelease.Emoji;
import com.sqk.emojirelease.FaceFragment;
import com.test.zhangtao.activitytest.R;

/**
 * Created by zhangtao on 16/11/9.
 */
public abstract class BaseWriteActivity extends BaseActivity implements FaceFragment.OnEmojiClickListener
{
    protected EditText mEditContent;

    @Override
    public void onEmojiDelete()
    {
        mEditContent = setEditContent();
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
        mEditContent = setEditContent();
        if (emoji != null)
        {
            int index = mEditContent.getSelectionStart();
            Editable editable = mEditContent.getEditableText();
            if (index < 0)
            {
                editable.append(emoji.getContent());
            }
            else
            {
                editable.insert(index , emoji.getContent());
            }
        }
    }

    public abstract EditText setEditContent();
}
