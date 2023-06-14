package com.henry.mediaaction.activity;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.henry.mediaaction.R;
import com.henry.mediaaction.adapter.MyMusicAdapter;
import com.henry.mediaaction.arouter.ARouterPath;
import com.henry.mediaaction.base.BaseResponse;
import com.henry.mediaaction.base.BaseViewInter;
import com.henry.mediaaction.base.MvpBaseActivity;
import com.henry.mediaaction.base.recycle.BaseRecycleItemClick;
import com.henry.mediaaction.bean.MusicBean;
import com.henry.mediaaction.present.MusicPresenter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
@Route(path = ARouterPath.ONE_APP_MAIN.MusicActivity)
public class MusicActivity extends MvpBaseActivity<MusicPresenter,BaseViewInter> implements BaseViewInter<BaseResponse<List<MusicBean>>>{
    @BindView(R.id.music_recycle)
    RecyclerView musicRecycle;
    private MyMusicAdapter adapter;
    private List<MusicBean> musicBeans = new ArrayList<>();

    @Override
    protected MusicPresenter getPresenter() {
        return new MusicPresenter();
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
        adapter = new MyMusicAdapter(musicBeans, R.layout.item_music_activity, (v, position) -> ARouter.getInstance().build(ARouterPath.ONE_APP_MAIN.MusicPlayerActivity).withSerializable("musicItem",musicBeans.get(position)).navigation());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        musicRecycle.setLayoutManager(manager);
        musicRecycle.setAdapter(adapter);
        showLoading();
        presenter.getMusics();
    }

    @Override
    public void actionSuccess(String type, BaseResponse<List<MusicBean>>... responses) {
        endLoading();
        if ("getMusics".equals(type)) {
            musicBeans.clear();
            musicBeans.addAll(responses[0].getData());
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void doFailure(String type, BaseResponse<List<MusicBean>>... responses) {

    }
}