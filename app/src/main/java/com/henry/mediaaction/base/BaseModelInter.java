package com.henry.mediaaction.base;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henry.mediaaction.net.HttpApiService;
import com.henry.mediaaction.utils.NetUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class BaseModelInter {
     private static final String BASE_URL = "http://www.chatgptbus.cn:8233/bus/";
     /**
     * 连接超时时长x秒
     */
    private static final int CONNECT_TIME_OUT = 10;
    /**
     * 读数据超时时长x秒
     */
    private static final int READ_TIME_OUT = 30;
    /**
     * 写数据接超时时长x秒
     */
    private static final int WRITE_TIME_OUT = 30;

    protected Retrofit mRetrofit;

    public HttpApiService mApis;
    protected BaseModelInter() {
        this(BASE_URL);
    }
    protected BaseModelInter(String baseUrl) {
        File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "http-cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            Request request=original.newBuilder().build();
            return chain.proceed(request);
        }).retryOnConnectionFailure(false).
           connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS).
           writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).
           readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).
           cache(cache).
           build();
        mRetrofit = new Retrofit.Builder().
                client(client).
                addConverterFactory(GsonConverterFactory.create(gson)).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                baseUrl(baseUrl).
                build();
        mApis = mRetrofit.create(HttpApiService.class);
    }

    protected void toSubscribe(Observable observable, Observer observer) {
        if (NetUtils.isNetAvailable()) {
            filterStatus(observable)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    protected void toJsonSubscribe(Observable observable, Observer observer){
        if (NetUtils.isNetAvailable()) {
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }
    private Observable filterStatus(Observable observable) {
        return observable.map(new ResultFilter());
    }
    /**
     * 请求code的拦截
     * @param <T>
     */
    private class ResultFilter<T> implements Function<BaseResponse<T>, BaseResponse<T>> {
        @Override
        public BaseResponse<T> apply(BaseResponse<T> input) {
            return input;
        }
    }
}
