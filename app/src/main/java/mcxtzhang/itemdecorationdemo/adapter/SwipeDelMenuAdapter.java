package mcxtzhang.itemdecorationdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

import mcxtzhang.itemdecorationdemo.model.CityBean;
import mcxtzhang.itemdecorationdemo.R;

/**
 * 和CityAdapter 一模一样，只是修改了 Item的布局
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class SwipeDelMenuAdapter extends CityAdapter {

    public SwipeDelMenuAdapter(Context mContext, List<CityBean> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public SwipeDelMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_city_swipe, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.findViewById(R.id.btnDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout) holder.itemView).quickClose();
                mDatas.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }
}
