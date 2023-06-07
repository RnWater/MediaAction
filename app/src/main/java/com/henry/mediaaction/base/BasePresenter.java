package com.henry.mediaaction.base;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<T extends BaseViewInter> {
    private WeakReference<T> weakReference;
    public void attach(T t) {
        weakReference = new WeakReference<>(t);
    }

    public void deAttach() {
        if (weakReference != null) {
            weakReference.clear();
            weakReference = null;
        }
    }
    public boolean isViewAttached() {
        return weakReference != null && weakReference.get() != null;
    }

    public T getView() {
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }
}
