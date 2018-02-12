package com.samhaus.baselibrary.network;


import com.samhaus.baselibrary.bean.TestBean2;
import com.samhaus.mylibrary.bean.CommonRequestBean;
import com.samhaus.baselibrary.bean.TestBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by samhaus on 2017/9/7.
 */

public interface Apis {

    //测试url
    @GET("test")
    Observable<CommonRequestBean<TestBean>> getTest();

    //测试url
    @GET("test2")
    Observable<CommonRequestBean<TestBean2>> getTest2();

}


