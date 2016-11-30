package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/10/18.
 */
public class HttpResult<T>
{
    //用来模仿resultCode和resultMessage
    private int count;
    private int start;
    private int total;
    private T subjects;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getSubjects() {
        return subjects;
    }

    public void setSubjects(T subjects) {
        this.subjects = subjects;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
