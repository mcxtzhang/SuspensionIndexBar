package mcxtzhang.itemdecorationdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.TitleItemDecoration;

import java.util.ArrayList;
import java.util.List;

import mcxtzhang.itemdecorationdemo.CityAdapter;
import mcxtzhang.itemdecorationdemo.CityBean;
import mcxtzhang.itemdecorationdemo.R;
import mcxtzhang.itemdecorationdemo.decoration.DividerItemDecoration;

/**
 * 介绍：高仿微信通讯录界面
 * 头部不是HeaderView 因为头部也需要快速导航，"↑"
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/7.
 */

public class WeChatActivity extends AppCompatActivity {
    private static final String TAG = "zxt";
    private RecyclerView mRv;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mManager;
    private List<CityBean> mDatas;

    private TitleItemDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        initDatas(getResources().getStringArray(R.array.provinces));

        mAdapter = new CityAdapter(this, mDatas);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new TitleItemDecoration(this, mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。
        //mRv.addItemDecoration(new TitleItemDecoration2(this,mDatas));
        mRv.addItemDecoration(new DividerItemDecoration(WeChatActivity.this, DividerItemDecoration.VERTICAL_LIST));


        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mDatas);//设置数据

    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(String[] data) {
        mDatas = new ArrayList<>();
        //微信的头部 也是可以右侧IndexBar导航索引的，
        // 但是它不需要被ItemDecoration设一个标题titile
        mDatas.add(new CityBean("新的朋友").setTop(true));
        mDatas.add(new CityBean("群聊").setTop(true));
        mDatas.add(new CityBean("标签").setTop(true));
        mDatas.add(new CityBean("公众号").setTop(true));
        for (int i = 0; i < data.length; i++) {
            CityBean cityBean = new CityBean();
            cityBean.setCity(data[i]);//设置城市名称
            mDatas.add(cityBean);
        }
    }

    /**
     * 更新数据源
     *
     * @param view
     */
    public void updateDatas(View view) {
        for (int i = 0; i < 99; i++) {
            mDatas.add(new CityBean("东京"));
            mDatas.add(new CityBean("大阪"));
        }
        mAdapter.notifyDataSetChanged();
        mIndexBar.setmSourceDatas(mDatas);
    }
}
