package com.samhaus.mylibrary.network;


import com.samhaus.mylibrary.R;
import com.samhaus.mylibrary.base.AppContext;
import com.samhaus.mylibrary.util.NetUtil;
import com.samhaus.mylibrary.util.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by samhaus on 2017/9/12.
 * IO线程进行请求，在主线程进行回调
 */

public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> compose() {
    return new ObservableTransformer<T, T>() {
        @Override
        public ObservableSource<T> apply(Observable<T> observable) {
            return observable
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            if (!NetUtil.isConnected(AppContext.mAppContext)) {
                                ToastUtil.show(AppContext.mAppContext.getResources().getString(R.string.net_not_connect));
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
}

}
