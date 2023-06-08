package com.henry.mediaaction.adapter;
import com.henry.mediaaction.R;
import com.henry.mediaaction.base.recycle.BaseRecycleAdapter;
import com.henry.mediaaction.base.recycle.BaseRecycleItemClick;
import com.henry.mediaaction.base.recycle.BaseRecycleViewHolder;
import com.henry.mediaaction.bean.MusicBean;
import java.util.List;
public class MyMusicAdapter extends BaseRecycleAdapter<MusicBean> {
    public MyMusicAdapter(List<MusicBean> mList, int layoutId, BaseRecycleItemClick click) {
        super(mList, layoutId, click);
    }
    @Override
    protected void convert(BaseRecycleViewHolder holder, MusicBean musicBean, List<MusicBean> mList, int position) {
        holder.setText(R.id.music_name,musicBean.getMusicName());
        holder.setText(R.id.music_song,musicBean.getSpeaker());
        holder.getView(R.id.music_item).setOnClickListener(view -> {
            if (click != null) {
                click.onItemClick(view,position);
            }
        });
    }
}
