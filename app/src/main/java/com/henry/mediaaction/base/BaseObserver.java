package com.henry.mediaaction.base;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
public abstract class BaseObserver<T> implements Observer<T> {
    protected Disposable disposable;
    private static final String TAG = "BaseObserver";

    /**
     * 订阅
     * @param d
     */
    @Override
    public void onSubscribe(Disposable d) {
        this.disposable=d;
    }

    /**
     * 链式调度
     * @param t
     */
    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    /**
     * 错误日志处理 网络错误 解析错误
     * @param e
     */
    @Override
    public void onError(Throwable e) {//阿里云收集错误日志
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        _onError(e);
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
    protected abstract void _onError(Throwable e);
    protected abstract void _onNext(T t);
}