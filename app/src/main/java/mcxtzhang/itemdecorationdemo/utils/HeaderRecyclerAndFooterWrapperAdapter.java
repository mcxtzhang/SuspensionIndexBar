package mcxtzhang.itemdecorationdemo.utils;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 介绍：一个给RecyclerView添加HeaderView FooterView的装饰Adapter类
 * 重点哦~ RecyclerView的HeaderView将可以被系统回收，不像老版的HeaderView是一个强引用在内存里
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/8/2.
 */
public abstract class HeaderRecyclerAndFooterWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 1000000;//headerview的viewtype基准值
    private static final int BASE_ITEM_TYPE_FOOTER = 2000000;//footerView的ViewType基准值

    //存放HeaderViews的layoudID和data,key是viewType，value 是 layoudID和data，
    // 在createViewHOlder里根据layoutId创建UI,
    // 在onbindViewHOlder里依据这个data渲染UI，同时也将layoutId回传出去用于判断何种Header
    private SparseArrayCompat<SparseArrayCompat> mHeaderDatas = new SparseArrayCompat<SparseArrayCompat>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();//存放FooterViews,key是viewType

    protected RecyclerView.Adapter mInnerAdapter;//内部的的普通Adapter

    public HeaderRecyclerAndFooterWrapperAdapter(RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    public int getHeaderViewCount() {
        return mHeaderDatas.size();
    }

    public int getFooterViewCount() {
        return mFooterViews.size();
    }

    private int getInnerItemCount() {
        return mInnerAdapter != null ? mInnerAdapter.getItemCount() : 0;
    }

    /**
     * 传入position 判断是否是headerview
     *
     * @param position
     * @return
     */
    public boolean isHeaderViewPos(int position) {// 举例， 2 个头，pos 0 1，true， 2+ false
        return getHeaderViewCount() > position;
    }

    /**
     * 传入postion判断是否是footerview
     *
     * @param position
     * @return
     */
    public boolean isFooterViewPos(int position) {//举例， 2个头，2个inner，pos 0 1 2 3 ,false,4+true
        return position >= getHeaderViewCount() + getInnerItemCount();
    }

    /**
     * 添加HeaderView
     *
     * @param layoutId headerView 的LayoutId
     * @param data     headerView 的data(可能多种不同类型的header 只能用Object了)
     */
    public void addHeaderView(int layoutId, Object data) {
        //mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, v);
        SparseArrayCompat headerContainer = new SparseArrayCompat();
        headerContainer.put(layoutId, data);
        mHeaderDatas.put(mHeaderDatas.size() + BASE_ITEM_TYPE_HEADER, headerContainer);
    }

    /**
     * 设置(更新)某个layoutId的HeaderView的数据
     *
     * @param layoutId
     * @param data
     */
    public void setHeaderView(int layoutId, Object data) {
        boolean isFinded = false;
        for (int i = 0; i < mHeaderDatas.size(); i++) {
            SparseArrayCompat sparse = mHeaderDatas.valueAt(i);
            if (layoutId == sparse.keyAt(0)) {
                sparse.setValueAt(0, data);
                isFinded = true;
            }
        }
        if (!isFinded) {//没发现 说明是addHeaderView
            addHeaderView(layoutId, data);
        }
    }


    /**
     * 设置某个位置的HeaderView
     *
     * @param headerPos 从0开始，如果pos过大 就是addHeaderview
     * @param layoutId
     * @param data
     */
    public void setHeaderView(int headerPos, int layoutId, Object data) {
        if (mHeaderDatas.size() > headerPos) {
            SparseArrayCompat headerContainer = new SparseArrayCompat();
            headerContainer.put(layoutId, data);
            mHeaderDatas.setValueAt(headerPos, headerContainer);
        } else if (mHeaderDatas.size() == headerPos) {//调用addHeaderView
            addHeaderView(layoutId, data);
        } else {
            //
            addHeaderView(layoutId, data);
        }
    }

    /**
     * 添加FooterView
     *
     * @param v
     */
    public void addFooterView(View v) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, v);
    }

    /**
     * 清空HeaderView数据
     */
    public void clearHeaderView() {
        mHeaderDatas.clear();
    }

    public void clearFooterView() {
        mFooterViews.clear();
    }


    public void setFooterView(View v) {
        clearFooterView();
        addFooterView(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderDatas.keyAt(position);
        } else if (isFooterViewPos(position)) {//举例：header 2， innter 2， 0123都不是，4才是，4-2-2 = 0，ok。
            return mFooterViews.keyAt(position - getHeaderViewCount() - getInnerItemCount());
        }
        return super.getItemViewType(position - getHeaderViewCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHeaderDatas.get(viewType) != null) {//不为空，说明是headerview
            //return new ViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            //return createHeader(parent, mHeaderViews.indexOfKey(viewType)); 第一种方法是让子类实现这个方法 构建ViewHolder
            return ViewHolder.get(parent.getContext(), null, parent, mHeaderDatas.get(viewType).keyAt(0), -1);
        } else if (mFooterViews.get(viewType) != null) {//不为空，说明是footerview
            return new ViewHolder(parent.getContext(), mFooterViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    //protected abstract RecyclerView.ViewHolder createHeader(ViewGroup parent, int headerPos);

    protected abstract void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o);//多回传一个layoutId出去，用于判断是第几个headerview

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            int layoutId = mHeaderDatas.get(getItemViewType(position)).keyAt(0);
            onBindHeaderHolder((ViewHolder) holder, position, layoutId, mHeaderDatas.get(getItemViewType(position)).get(layoutId));
            return;
        } else if (isFooterViewPos(position)) {
            return;
        }
        //举例子，2个header，0 1是头，2是开始，2-2 = 0
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
    }


    @Override
    public int getItemCount() {
        return getInnerItemCount() + getHeaderViewCount() + getFooterViewCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        //为了兼容GridLayout
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderDatas.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }
}
