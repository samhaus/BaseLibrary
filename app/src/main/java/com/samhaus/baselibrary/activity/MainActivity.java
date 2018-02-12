package com.samhaus.baselibrary.activity;

import android.os.Bundle;

import com.samhaus.baselibrary.R;
import com.samhaus.baselibrary.bean.TestBean2;
import com.samhaus.baselibrary.presenter.MainPresenter;
import com.samhaus.mylibrary.base.BaseActivity;
import com.samhaus.baselibrary.bean.TestBean;
import com.samhaus.mylibrary.network.BaseNetWork;
import com.samhaus.mylibrary.network.BasePresenter;
import com.samhaus.mylibrary.util.LogUtil;

/**
 * Created by samhaus on 2018/2/6.
 */

public class MainActivity extends BaseActivity implements BaseNetWork {
    private MainPresenter presenter;


    @Override
    public int generateLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter createPresenter() {
        return new MainPresenter(this, this);
    }

    @Override
    public void initData(Bundle bundle) {
        presenter = new MainPresenter(this, this);
        presenter.getTest();
        presenter.getTest2();
    }

    @Override
    public void initView() {

    }

    @Override
    public void loadding() {

    }

    @Override
    public void loaddingFinish() {

    }

    @Override
    public void requestSuccess(Object object) {
        if (object instanceof TestBean) {
            TestBean bean = (TestBean) object;
            LogUtil.e("bean1" + bean.getText().toString());
        } else if (object instanceof TestBean2) {
            TestBean2 bean = (TestBean2) object;
            LogUtil.e("bean2" + bean.getText().toString());
        }
    }

    @Override
    public void requestError(String object) {

    }

}
