package mcxtzhang.itemdecorationdemo.meituan;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mcxtzhang.itemdecorationdemo.IndexBar.bean.BaseIndexPinyinBean;
import mcxtzhang.itemdecorationdemo.IndexBar.widget.IndexBar;
import mcxtzhang.itemdecorationdemo.R;
import mcxtzhang.itemdecorationdemo.decoration.DividerItemDecoration;
import mcxtzhang.itemdecorationdemo.decoration.TitleItemDecoration;
import mcxtzhang.itemdecorationdemo.meituan.bean.CityHeaderBean;
import mcxtzhang.itemdecorationdemo.utils.CommonAdapter;
import mcxtzhang.itemdecorationdemo.utils.HeaderRecyclerAndFooterWrapperAdapter;
import mcxtzhang.itemdecorationdemo.utils.ViewHolder;
import mcxtzhang.itemdecorationdemo.weixin.CityBean;

/**
 * 仿美团 选择城市页面
 */
public class MeiTuanActivity extends Activity {
    private RecyclerView mRv;
    private LinearLayoutManager mLM;
    private CommonAdapter<CityBean> mInnerAdapter;//内部适配器和数据
    private List<CityBean> mInnerDatas;

    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;//头部适配器

    private List<BaseIndexPinyinBean> mTitleDatas = new ArrayList<>();//分类titledatas
    private TitleItemDecoration mTitleItemDecoration;


    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvIndexBarHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mei_tuan);
        initDatas();

        mIndexBar = (IndexBar) findViewById(R.id.indexBar);
        mTvIndexBarHint = (TextView) findViewById(R.id.tvIndexBarHint);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mInnerAdapter = new CommonAdapter<CityBean>(this, R.layout.ayc_item_select_city, mInnerDatas) {
            @Override
            public void convert(ViewHolder holder, CityBean cityBean) {
                holder.setText(R.id.tvCity, cityBean.getCity());
            }
        };
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mInnerAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, final int headerPos, int layoutId, Object o) {
                switch (layoutId) {
                    case R.layout.ayc_header_item_select_city:
                        final CityHeaderBean cityHeaderBean = (CityHeaderBean) o;
                        //Tag 由ItemDecoration实现
                        //holder.setText(R.id.tvTag, cityHeaderBean.getBaseIndexTag());
                        //网格
                        ((RecyclerView) holder.getView(R.id.rvCity)).setAdapter(
                                new CommonAdapter<CityBean>(MeiTuanActivity.this, R.layout.ayc_header_item_item_select_city, cityHeaderBean.getCityBeanList()) {
                                    @Override
                                    public void convert(ViewHolder holder, final CityBean cityBean) {
                                        holder.setText(R.id.tvName, cityBean.getCity());
                                    }
                                });

                        ((RecyclerView) holder.getView(R.id.rvCity)).setLayoutManager(new GridLayoutManager(MeiTuanActivity.this, 3));
                        break;
                    default:
                        break;
                }
            }
        };

        mRv.setAdapter(mHeaderAdapter);
        mRv.setLayoutManager(mLM = new LinearLayoutManager(this));
        mRv.addItemDecoration(mTitleItemDecoration = new TitleItemDecoration(this, mTitleDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 35, getResources().getDisplayMetrics()))
                .setColorTitleBg(Color.WHITE)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()))
                .setColorTitleFont(Color.BLACK));
        //分割线
        mRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        //为HeaderView设置数据
        mHeaderAdapter.setHeaderView(0, R.layout.ayc_header_item_select_city, mTitleDatas.get(0));
        mHeaderAdapter.setHeaderView(1, R.layout.ayc_header_item_select_city, mTitleDatas.get(1));
        mHeaderAdapter.setHeaderView(2, R.layout.ayc_header_item_select_city, mTitleDatas.get(2));



        mIndexBar.setmPressedShowTextView(mTvIndexBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mLM)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mInnerDatas);//设置数据源 仅仅为了排序
    }


    /**
     * 组织数据源
     *
     * @return
     */
    private void initDatas() {
        String[] data = (getResources().getStringArray(R.array.provinces));
        mInnerDatas = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            CityBean cityBean = new CityBean();
            cityBean.setCity(data[i]);//设置城市名称
            mInnerDatas.add(cityBean);
        }


        //构建headerView
        List<CityBean> locationCitys = new ArrayList<>();
        locationCitys.add(new CityBean("定位中"));
        mTitleDatas.add(new CityHeaderBean("定位城市", locationCitys));

        List<CityBean> localCityNameList = new ArrayList<>();
        localCityNameList.add(new CityBean("上海"));
        localCityNameList.add(new CityBean("北京"));
        localCityNameList.add(new CityBean("武汉"));
        mTitleDatas.add(new CityHeaderBean("最近访问城市", localCityNameList));

        List<CityBean> hotCitys = new ArrayList<>();
        hotCitys.add(new CityBean("上海"));
        hotCitys.add(new CityBean("北京"));
        hotCitys.add(new CityBean("武汉"));
        hotCitys.add(new CityBean("蚌埠"));
        hotCitys.add(new CityBean("合肥"));
        hotCitys.add(new CityBean("广州"));
        hotCitys.add(new CityBean("深圳"));
        hotCitys.add(new CityBean("南京"));
        hotCitys.add(new CityBean("芜湖"));
        mTitleDatas.add(new CityHeaderBean("热门城市", hotCitys));
        mTitleDatas.addAll(mInnerDatas);//将里面的数据也塞进来

    }
}
