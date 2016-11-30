package com.test.zhangtao.activitytest.entity;

import java.util.List;

/**
 * Created by zhangtao on 16/10/18.
 */
public class MovieEntity
{
    private int count;
    private int start;
    private int total;
    private List<Subject> subjects;
    private String title;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("title=" + title + " count=" + count + " start=" + start);
        if (subjects != null)
        {
            sb.append(" subjects" + subjects.toString());
        }
        return sb.toString();
    }
}
