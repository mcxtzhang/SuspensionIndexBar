package mcxtzhang.itemdecorationdemo.IndexBar.bean;

/**
 * 介绍：索引类的标志位的实体基类
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/09/04.
 */

public class BaseIndexTagBean {
    private String tag;//所属的分类（城市的汉语拼音首字母）

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
