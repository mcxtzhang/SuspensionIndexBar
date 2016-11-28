package mcxtzhang.itemdecorationdemo.model;

/**
 * 介绍：美团最顶部Header
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/11/28.
 */

public class MeituanTopHeaderBean {
    private String txt;

    public MeituanTopHeaderBean(String txt) {
        this.txt = txt;
    }

    public String getTxt() {
        return txt;
    }

    public MeituanTopHeaderBean setTxt(String txt) {
        this.txt = txt;
        return this;
    }

}
