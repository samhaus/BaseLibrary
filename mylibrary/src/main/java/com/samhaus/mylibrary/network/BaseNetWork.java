package com.samhaus.mylibrary.network;

/**
 * Created by samhaus on 2018/2/7.
 */

public interface BaseNetWork<T> {

    void loadding();

    void loaddingFinish();

    void requestSuccess(T object);

    void requestError(String object);
}
