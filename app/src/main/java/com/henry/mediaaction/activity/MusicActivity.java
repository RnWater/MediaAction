package com.henry.mediaaction.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.henry.mediaaction.R;
import com.henry.mediaaction.adapter.MyMusicAdapter;
import com.henry.mediaaction.arouter.ARouterPath;
import com.henry.mediaaction.base.BasePresenter;
import com.henry.mediaaction.base.MvpBaseActivity;
import com.henry.mediaaction.base.recycle.BaseRecycleItemClick;
import com.henry.mediaaction.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Route(path = ARouterPath.ONE_APP_MAIN.MusicActivity)
public class MusicActivity extends MvpBaseActivity {
    @BindView(R.id.music_recycle)
    RecyclerView musicRecycle;
    private MyMusicAdapter adapter;
    private List<MusicBean> musicBeans = new ArrayList<>();

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_music;
    }

    @Override
    protected void initView() {
        setTitle("音乐列表");
    }

    @Override
    protected void initData() {
        adapter = new MyMusicAdapter(musicBeans, R.layout.item_music_activity, new BaseRecycleItemClick() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        musicRecycle.setLayoutManager(manager);
        musicRecycle.setAdapter(adapter);
    }
}