package com.test.zhangtao.activitytest.http;

import android.util.Log;

import com.test.zhangtao.activitytest.entity.CommentEntity;
import com.test.zhangtao.activitytest.entity.MovieDescription;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 16/10/25.
 */
public class ContentParse
{
    public static MovieDescription parseContent(String result)
    {
        MovieDescription movieDescription = new MovieDescription();

        Document doc = Jsoup.parseBodyFragment(result);
        Element body = doc.body();

        Element info = body.getElementById("info");

        List<TextNode> country = info.textNodes();
        List<String> other = new ArrayList<>();
        for (int i = 0 ; i < country.size() ; i++)
        {
            String important = country.get(i).text().trim();
            if (!important.equals("") && !important.equals("/"))
            {
                other.add(country.get(i).text());
            }
        }
        movieDescription.setCountryName(other.get(0));
        movieDescription.setLanguage(other.get(1));

        Elements attrs = info.getElementsByClass("attrs");
        Elements time = info.getElementsByClass("pl");

        String onLineTime = time.get(6).nextElementSibling().text();
        String duration = time.get(7).nextElementSibling().text();
        movieDescription.setOnLineTime(onLineTime);
        movieDescription.setDuration(duration);

        String director = attrs.get(0).child(0).text();
        String actor1 = attrs.get(2).child(0).text();
        String actor2 = attrs.get(2).child(1).text();
        movieDescription.setDirector(director);
        movieDescription.setActor1(actor1);
        movieDescription.setActor2(actor2);

        Element mainpic = body.getElementById("mainpic");
        Elements rating = body.getElementsByClass("rating_sum");
        Element sum = rating.get(0).child(0);
        movieDescription.setRatingSum(sum.text());

        String img = mainpic.child(0).child(0).attr("src");
        movieDescription.setMainPicUrl(img);

        Element content = body.getElementById("link-report");
        String context = content.child(0).text();
        movieDescription.setContent(context);

        return movieDescription;
    }

    public static List<CommentEntity> parseComment(String result)
    {
        List<CommentEntity> commentEntityList = new ArrayList<>();
        CommentEntity commentEntity = null;
        Document doc = Jsoup.parseBodyFragment(result);
        Element body = doc.body();

        Elements avatar = body.getElementsByClass("avatar");
        Elements comments = body.getElementsByClass("comment");

        for (int i = 0 ; i < comments.size() ; i++)
        {
            commentEntity = new CommentEntity();
            String user_name = avatar.get(i).child(0).attr("title");
            String user_icon = avatar.get(i).child(0).child(0).attr("src");
            commentEntity.setUserName(user_name);
            commentEntity.setUserIconUrl(user_icon);

            Elements vote = comments.get(i).getElementsByClass("comment-vote");
            String[] arry = vote.get(0).text().split(" ");
            commentEntity.setCommentCount(arry[0]);

            Elements name = comments.get(i).getElementsByClass("comment-info");
            String[] arry2 = name.get(0).text().split(" ");
            commentEntity.setPublishDate(arry2[1]);

            Element word = comments.get(i).child(1);
            commentEntity.setCommentContent(word.text());
            commentEntityList.add(commentEntity);
        }
        return commentEntityList;
    }
}