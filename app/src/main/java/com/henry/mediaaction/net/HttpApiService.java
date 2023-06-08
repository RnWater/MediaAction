package com.henry.mediaaction.net;
import com.henry.mediaaction.base.BaseResponse;
import com.henry.mediaaction.bean.MusicBean;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface HttpApiService {
    @GET("music/getAllMusics")
    Observable<BaseResponse<List<MusicBean>>> getMusics();
}
