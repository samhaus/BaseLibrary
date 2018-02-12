package com.samhaus.baselibrary.network;

import com.samhaus.baselibrary.constant.AppConstant;
import com.samhaus.mylibrary.network.RetrofitManager;

/**
 * Created by samhaus on 2018/2/7.
 */

public class ApiFactory {
    protected static final Object monitor = new Object();
    static Apis apis = null;

    /**
     * singleton配置创建api
     *
     * @return ApiServer
     */
    public static Apis build() {
        synchronized (monitor) {
            if (apis == null) {
                apis = RetrofitManager.getRetrofit(AppConstant.URL_MAIN).create(Apis.class);
            }
            return apis;
        }
    }
}
