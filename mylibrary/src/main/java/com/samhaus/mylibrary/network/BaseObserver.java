package com.samhaus.mylibrary.network;


import com.samhaus.mylibrary.bean.CommonRequestBean;
import com.samhaus.mylibrary.util.LogUtil;
import com.samhaus.mylibrary.util.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by samhaus on 2017/9/12.
 * RxJava Observable订阅需要传入一个观察者对象，此处封装一个BaseObserver
 */

public abstract class BaseObserver<T> implements Observer<CommonRequestBean<T>> {

    private static final String TAG = "BaseObserver";
//    private Context mContext;

    protected BaseObserver() {

    }


    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(CommonRequestBean<T> value) {
        if (value.getResult() != null) {
            T t = value.getResult();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getReason());
        }
    }

    @Override
    public void onError(Throwable e) {
        onHandleError(e.getMessage());
    }

    @Override
    public void onComplete() {
    }


    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        ToastUtil.show(msg);
        LogUtil.e("Error:" + msg);
    }

}
