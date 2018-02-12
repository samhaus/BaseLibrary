package com.samhaus.baselibrary.presenter;

import android.content.Context;

import com.samhaus.baselibrary.network.ApiFactory;
import com.samhaus.baselibrary.network.Apis;
import com.samhaus.mylibrary.network.BaseNetWork;
import com.samhaus.mylibrary.network.BaseObserver;
import com.samhaus.mylibrary.network.BasePresenter;
import com.samhaus.mylibrary.network.RxTransformerHelper;
import com.samhaus.mylibrary.util.LogUtil;

import io.reactivex.Observable;

/**
 * Created by samhaus on 2018/2/7.
 */

public class MainPresenter extends BasePresenter<BaseNetWork> {

    private Apis apis;
    private Context mContext;
    private BaseNetWork baseInterface;
    private Observable observable;

    public MainPresenter(Context mContext, BaseNetWork baseInterface) {
        this.mContext = mContext;
        this.baseInterface = baseInterface;
        attachView(baseInterface);
        apis = ApiFactory.build();
    }

    public void getTest() {
        observable = apis.getTest();
        getResponse();
    }

    public void getTest2() {
        observable = apis.getTest2();
        getResponse();
    }

    private void getResponse() {
        baseInterface.loadding();
        observable
                .compose(RxTransformerHelper.applySchedulerAndAllFilter(mContext))
                .subscribe(new BaseObserver() {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        baseInterface.requestSuccess(o);
                    }

                    @Override
                    protected void onHandleError(String msg) {
                        baseInterface.requestError(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("error " + e.getMessage());
                        baseInterface.requestError(e.getMessage());
                    }

                });
    }

}
