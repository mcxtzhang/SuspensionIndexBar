package com.mcxtzhang.indexlib.IndexBar.sort;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.util.List;

/**
 * 介绍：排序帮助类
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/11/27.
 */

public interface ISortHelper {
    //对源数据进行排序（RecyclerView）
    void sortSourceDatas(List<? extends BaseIndexPinyinBean> datas);

    //对IndexBar的数据源进行排序(右侧栏)
    void sortIndexDatas(List<? extends BaseIndexPinyinBean> sourceDatas,List<String> datas, boolean isNeedRealIndex);
}
