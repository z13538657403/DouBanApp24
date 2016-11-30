package com.test.zhangtao.activitytest.entity;

/**
 * Created by zhangtao on 16/11/23.
 */
public class SecondMusicResult
{
    private int showapi_res_code;
    private String showapi_res_error;
    private SecondResultBody showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public SecondResultBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(SecondResultBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public class SecondResultBody
    {
        private int ret_code;
        private SecondPageBean pagebean;

        public int getRet_code()
        {
            return ret_code;
        }

        public void setRet_code(int ret_code)
        {
            this.ret_code = ret_code;
        }

        public SecondPageBean getPagebean()
        {
            return pagebean;
        }

        public void setPagebean(SecondPageBean pagebean)
        {
            this.pagebean = pagebean;
        }
    }
}
