package com.mcxtzhang.indexlib.IndexBar.convert;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.util.List;

/**
 * 介绍：
 * 1 将汉语转成拼音
 * 2 填充indexTag
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/11/27.
 */

public interface IConvertCharHelper {

    //汉语-》拼音
    void convert(List<? extends BaseIndexPinyinBean> data);

    //拼音->tag
    void fillInexTag(List<? extends BaseIndexPinyinBean> data);
}
