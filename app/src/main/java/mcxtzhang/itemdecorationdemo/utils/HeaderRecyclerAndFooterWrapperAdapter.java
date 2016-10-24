package mcxtzhang.itemdecorationdemo.utils;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 介绍：一个给RecyclerView添加HeaderView FooterView的装饰Adapter类
 * 重点哦~ RecyclerView的HeaderView将可以被系统回收，不像老版的HeaderView是一个强引用在内存里
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/8/2.
 */
public abstract class HeaderRecyclerAndFooterWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private class HeaderData {
        private final int DEFAULT_HEADER_VIEW_CACHE_SIZE = 5;//默认是5 和RecyclerViewPool的默认值一样
        private int layoutId;//viewType当做layoutId
        private Object data;//该viewType(LayoutId)对应的数据
        private int cacheSize;//该种viewType的HeaderView 在RecyclerViewPool的缓存池内的缓存数量

        public HeaderData(int layoutId, Object data, int cacheSize) {
            this.layoutId = layoutId;
            this.data = data;
            this.cacheSize = cacheSize;
        }

        public HeaderData(int layoutId, Object data) {
            this.layoutId = layoutId;
            this.data = data;
            this.cacheSize = DEFAULT_HEADER_VIEW_CACHE_SIZE;
        }

        public int getLayoutId() {
            return layoutId;
        }

        public void setLayoutId(int layoutId) {
            this.layoutId = layoutId;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public int getCacheSize() {
            return cacheSize;
        }

        public void setCacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
        }

    }

    private static final int BASE_ITEM_TYPE_FOOTER = 2000000;//footerView的ViewType基准值

    //存放HeaderViews的layoudID和data,key是viewType，value 是 layoudID和data，在createViewHOlder里根据layoutId创建UI,在onbindViewHOlder里依据这个data渲染UI
    private ArrayList<HeaderData> mHeaderDatas = new ArrayList<HeaderData>();
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

/*        SparseArrayCompat headerContainer = new SparseArrayCompat();
        headerContainer.put(layoutId, data);
        mHeaderDatas.put(mHeaderDatas.size() + BASE_ITEM_TYPE_HEADER, headerContainer);*/

        mHeaderDatas.add(new HeaderData(layoutId, data));
    }

    /**
     * 添加HeaderView
     *
     * @param layoutId  headerView 的LayoutId
     * @param data      headerView 的data(可能多种不同类型的header 只能用Object了)
     * @param cacheSize 该种headerView在缓存池中的缓存个数
     */
    public void addHeaderView(int layoutId, Object data, int cacheSize) {
        //mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, v);

/*        SparseArrayCompat headerContainer = new SparseArrayCompat();
        headerContainer.put(layoutId, data);
        mHeaderDatas.put(mHeaderDatas.size() + BASE_ITEM_TYPE_HEADER, headerContainer);*/

        mHeaderDatas.add(new HeaderData(layoutId, data, cacheSize));
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
/*            SparseArrayCompat headerContainer = new SparseArrayCompat();
            headerContainer.put(layoutId, data);
            mHeaderDatas.setValueAt(headerPos, headerContainer);*/
            mHeaderDatas.get(headerPos).setLayoutId(layoutId);
            mHeaderDatas.get(headerPos).setData(data);
        } else if (mHeaderDatas.size() == headerPos) {//调用addHeaderView
            addHeaderView(layoutId, data);
        } else {
            //
            addHeaderView(layoutId, data);
        }
    }

    /**
     * 设置某个位置的HeaderView
     *
     * @param headerPos 从0开始，如果pos过大 就是addHeaderview
     * @param layoutId
     * @param data
     * @param cacheSize 该种headerView在缓存池中的缓存个数
     */
    public void setHeaderView(int headerPos, int layoutId, Object data, int cacheSize) {
        if (mHeaderDatas.size() > headerPos) {
/*            SparseArrayCompat headerContainer = new SparseArrayCompat();
            headerContainer.put(layoutId, data);
            mHeaderDatas.setValueAt(headerPos, headerContainer);*/
            mHeaderDatas.get(headerPos).setLayoutId(layoutId);
            mHeaderDatas.get(headerPos).setData(data);
            mHeaderDatas.get(headerPos).setCacheSize(cacheSize);
        } else if (mHeaderDatas.size() == headerPos) {//调用addHeaderView
            addHeaderView(layoutId, data, cacheSize);
        } else {
            //
            addHeaderView(layoutId, data, cacheSize);
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
            return mHeaderDatas.get(position).getLayoutId();
        } else if (isFooterViewPos(position)) {//举例：header 2， innter 2， 0123都不是，4才是，4-2-2 = 0，ok。
            return mFooterViews.keyAt(position - getHeaderViewCount() - getInnerItemCount());
        }
        return super.getItemViewType(position - getHeaderViewCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHeaderDatas != null && !mHeaderDatas.isEmpty()) {//不为空，说明有headerview
            for (HeaderData headerData : mHeaderDatas) {
                if (headerData.getLayoutId() == viewType) {//匹配上了说明是headerView
                    return ViewHolder.get(parent.getContext(), null, parent, viewType, -1);
                }
            }
        }
        if (mFooterViews.get(viewType) != null) {//不为空，说明是footerview
            return new ViewHolder(parent.getContext(), mFooterViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    //protected abstract RecyclerView.ViewHolder createHeader(ViewGroup parent, int headerPos);

    protected abstract void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o);//多回传一个layoutId出去，用于判断是第几个headerview

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            onBindHeaderHolder((ViewHolder) holder, position, mHeaderDatas.get(position).getLayoutId(), mHeaderDatas.get(position).getData());
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
        //设置HeaderView的ViewHolder的缓存数量
        if (null != mHeaderDatas && !mHeaderDatas.isEmpty()) {
            for (HeaderData headerData : mHeaderDatas) {
                recyclerView.getRecycledViewPool().setMaxRecycledViews(headerData.getLayoutId(), headerData.getCacheSize());
            }
        }
        //为了兼容GridLayout
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (isHeaderViewPos(position)) {
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

