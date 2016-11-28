package mcxtzhang.itemdecorationdemo.adapter;

import android.content.Context;

import java.util.List;

import mcxtzhang.itemdecorationdemo.R;
import mcxtzhang.itemdecorationdemo.model.MeiTuanBean;
import mcxtzhang.itemdecorationdemo.utils.CommonAdapter;
import mcxtzhang.itemdecorationdemo.utils.ViewHolder;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class MeituanAdapter extends CommonAdapter<MeiTuanBean> {
    public MeituanAdapter(Context context, int layoutId, List<MeiTuanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final MeiTuanBean cityBean) {
        holder.setText(R.id.tvCity, cityBean.getCity());
    }
}