package com.test.zhangtao.activitytest.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.test.zhangtao.activitytest.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangtao on 16/11/12.
 */
public class RichTextUtils
{
    private static String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
    private static final Pattern WEB_PATTERN = Pattern.compile(regex);
    private static final Pattern MENTION_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5a-zA-Z0-9_-]+");
    private static final Pattern FOCUS_PATTERN = Pattern.compile("#[^#]+#");

    public static SpannableString getRichText(final Context context , String text)
    {
        SpannableString spannableString = new SpannableString(text);
        if (!TextUtils.isEmpty(text))
        {
            final int link_color = ContextCompat.getColor(context, R.color.cw_blue);
            Matcher matcher = WEB_PATTERN.matcher(text);
            while (matcher.find())
            {
                final String url = matcher.group();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Utils", url);

                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri_content = Uri.parse(url);
                        intent.setData(uri_content);
                        context.startActivity(intent);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds)
                    {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(link_color);
                    }

                }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Matcher matcherAdd = MENTION_PATTERN.matcher(text);
            while (matcherAdd.find())
            {
                final String urlAdd = matcherAdd.group();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Utils", urlAdd);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(link_color);
                    }
                }, matcherAdd.start(), matcherAdd.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Matcher matcherFocus = FOCUS_PATTERN.matcher(text);
            while (matcherFocus.find())
            {
                final String focusString = matcherFocus.group();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view)
                    {
                        Log.d("Utils" , focusString);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds)
                    {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(link_color);
                    }
                } , matcherFocus.start() , matcherFocus.end() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}
