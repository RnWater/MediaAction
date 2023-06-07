package com.henry.mediaaction.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class RxEvent {
    private final PublishSubject<Object> bus = PublishSubject.create();

    private static final RxEvent EVENT = new RxEvent();

    public static RxEvent singleton() {
        return EVENT;
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType).observeOn(AndroidSchedulers.mainThread());
    }
}
