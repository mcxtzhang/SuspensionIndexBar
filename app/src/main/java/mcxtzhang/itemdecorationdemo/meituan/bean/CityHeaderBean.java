package mcxtzhang.itemdecorationdemo.meituan.bean;

import java.util.List;

import mcxtzhang.itemdecorationdemo.IndexBar.bean.BaseIndexPinyinBean;
import mcxtzhang.itemdecorationdemo.weixin.CityBean;

/**
 * 介绍：选择城市头部的Bean
 * 定位城市  最近访问的城市  热门城市
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/9/13.
 */

public class CityHeaderBean extends BaseIndexPinyinBean {
    private List<CityBean> cityBeanList;

    public List<CityBean> getCityBeanList() {
        return cityBeanList;
    }

    public void setCityBeanList(List<CityBean> cityBeanList) {
        this.cityBeanList = cityBeanList;
    }

    public CityHeaderBean(String tag, List<CityBean> cityBeanList) {
        this.cityBeanList = cityBeanList;
        setBaseIndexTag(tag);
    }

    @Override
    public String getTarget() {
        return getBaseIndexTag();
    }
}
