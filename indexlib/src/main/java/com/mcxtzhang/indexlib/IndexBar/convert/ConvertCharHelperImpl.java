package com.mcxtzhang.indexlib.IndexBar.convert;

import com.github.promeg.pinyinhelper.Pinyin;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.util.List;


/**
 * 介绍：拼音转化实现类
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/11/27.
 */

public class ConvertCharHelperImpl implements IConvertCharHelper {
    @Override
    public void convert(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return;
        }
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = datas.get(i);
            StringBuilder pySb = new StringBuilder();
            //add by zhangxutong 2016 11 10 如果不是top 才转拼音，否则不用转了
            if (indexPinyinBean.isNeedToPinyin()) {
                String target = indexPinyinBean.getTarget();//取出需要被拼音化的字段
                //遍历target的每个char得到它的全拼音
                for (int i1 = 0; i1 < target.length(); i1++) {
                    //利用TinyPinyin将char转成拼音
                    //查看源码，方法内 如果char为汉字，则返回大写拼音
                    //如果c不是汉字，则返回String.valueOf(c)
                    pySb.append(Pinyin.toPinyin(target.charAt(i1)).toUpperCase());
                }
                indexPinyinBean.setBaseIndexPinyin(pySb.toString());//设置城市名全拼音
            } else {
                //pySb.append(indexPinyinBean.getBaseIndexPinyin());
            }
        }

    }

    @Override
    public void fillInexTag(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return;
        }
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = datas.get(i);
            if (indexPinyinBean.isNeedToPinyin()) {
                //以下代码设置城市拼音首字母
                String tagString = indexPinyinBean.getBaseIndexPinyin().toString().substring(0, 1);
                if (tagString.matches("[A-Z]")) {//如果是A-Z字母开头
                    indexPinyinBean.setBaseIndexTag(tagString);
                } else {//特殊字母这里统一用#处理
                    indexPinyinBean.setBaseIndexTag("#");
                }
            }
        }
    }
}
