package com.samhaus.mylibrary.network;


import android.content.Context;


import com.samhaus.mylibrary.base.BaseActivity;
import com.samhaus.mylibrary.util.ToastUtil;

import java.lang.ref.SoftReference;

/**
 * Created by jiaoyu on 17/4/20.
 */
public class SimpleErrorVerify implements ErrorVerify {
    //软引用
    private SoftReference<Context> mContext;

    public SimpleErrorVerify(Context mContext) {
        this.mContext = new SoftReference<>(mContext);
    }

    @Override
    public void call(int code, String desc) {
        if (mContext != null && mContext.get() != null && mContext.get() instanceof BaseActivity) {
            ToastUtil.show(desc);
            ((BaseActivity) mContext.get()).hideWaitDialog();
        }
    }
}
