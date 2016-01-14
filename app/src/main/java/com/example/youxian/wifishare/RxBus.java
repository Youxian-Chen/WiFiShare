package com.example.youxian.wifishare;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Youxian on 1/14/16.
 */
public class RxBus {
    private final PublishSubject<String> mRxBus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead
    //private final Subject<Object, Object> mRxBus = new SerializedSubject<>(PublishSubject.create());

    private static RxBus mInstance;

    public static RxBus getInstance() {
        if (mInstance == null) {
            mInstance = new RxBus();
        }
        return mInstance;
    }

    private RxBus() {

    }

    public void send(String s) {
        mRxBus.onNext(s);
    }

    public Observable<String> toObserverable() {
        return mRxBus;
    }

    public boolean hasObservers() {
        return mRxBus.hasObservers();
    }
}
