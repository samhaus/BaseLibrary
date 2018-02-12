package com.samhaus.mylibrary.network;

import android.content.Context;

import com.samhaus.mylibrary.BuildConfig;
import com.samhaus.mylibrary.base.BaseActivity;
import com.samhaus.mylibrary.bean.CommonRequestBean;
import com.samhaus.mylibrary.util.LogUtil;
import com.samhaus.mylibrary.util.NetUtil;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Retrofit帮助类 返回参数滤错
 */
public class RxTransformerHelper {

    /**
     * 服务器返回错误码 0为成功
     */
    public static final String RESULTOK = "200";   //请求后台成功
    public static final int RESULTFAIL = 1;   //请求失败

    //    public static final int REQUESTFLAG1 = 1011;  //请先登录
//
//    public static final int REQUESTFLAG2 = 1012;  //登录超时
//
//    public static final int REQUESTFLAG3 = 1013; //登录验证失败
//
    public static final int NET_ERROR = -1;     //网络错误


    /**
     * 业务错误过滤器（自定义）
     */
    private static <T> Predicate<T> verifyBusiness(ErrorVerify errorVerify) {
        return response -> {
            if (response instanceof CommonRequestBean) {
                CommonRequestBean baseResponse = (CommonRequestBean) response;
                String responseCode = baseResponse.getError_code();
                boolean isSuccess = responseCode.equals(RESULTOK);
                if (!isSuccess) {
                    if (errorVerify != null) {
                        errorVerify.call(Integer.valueOf(baseResponse.getError_code()), baseResponse.getReason());
                    }
                }
                return isSuccess;
            } else {
                return false;
            }
        };
    }

    /**
     * 非空过滤器（自定义）
     */
    private static <T> Predicate<T> verifyNotEmpty() {
        return response -> response != null;
    }

    /**
     * 错误提醒处理
     *
     * @param context
     * @param errorVerify
     * @param <T>
     * @return
     */
    private static <T> Function<Throwable, T> doError(final Context context, ErrorVerify errorVerify) {
        return throwable -> {
            throwable.printStackTrace();
            if (errorVerify != null) {
                if (!NetUtil.isConnected(context)) {
                    errorVerify.call(NET_ERROR, "暂无网络");
                } else {
                    if (BuildConfig.DEBUG) {
                        errorVerify.call(0, throwable.toString());
                    } else {
                        errorVerify.call(0, "系统异常");
                    }
                }
            }
            return null;
        };
    }

    /**
     * sessionId 过期的过滤器
     */
    private static <T> Predicate<T> verifyResultCode(Context context) {
        return response -> {
            if (response instanceof CommonRequestBean) {
                CommonRequestBean baseResponse = (CommonRequestBean) response;
                String state = baseResponse.getError_code();
                if (!state.equals(RESULTOK)) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        };
    }

    /**
     * 优先使用这个，可以继续使用操作符
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 聚合了session过滤器,业务过滤器及合并操作 自定义错误回调
     */
    public static <T> ObservableTransformer<T, T>
    applySchedulersAndAllFilter(Context context, ErrorVerify errorVerify) {
        return observable -> observable
                .compose(applySchedulers())
                .filter(verifyNotEmpty())
                .filter(verifyBusiness(errorVerify))
                .onErrorReturn(doError(context, errorVerify))
                .doFinally(() -> {
                    if (context instanceof BaseActivity)
                        ((BaseActivity) context).hideWaitDialog();
                });
    }


//    /**
//     * 聚合了session过滤器,简单业务过滤器及合并操作及自定义的错误返回
//     */
//    public static <T> ObservableTransformer<BaseModel<T>, T>
//    applySchedulersResult(Context context, ErrorVerify errorVerify) {
//        return observable -> observable
//                .compose(applySchedulersAndAllFilter(context, errorVerify))
//                .map(BaseModel::getData);
//    }
//
//    /**
//     * 聚合了session过滤器,简单业务过滤器及合并操作
//     * 结果直接返回包含BaseModelNew<T>里定义的 T 对象
//     */
//    public static <T> ObservableTransformer<BaseModel<T>, T> applySchedulerResult(Context context) {
//        return applySchedulersResult(context, new SimpleErrorVerify(context));
//    }

    /**
     * 聚合了session过滤器,简单业务过滤器及合并操作
     * 结果返回包含BaseModelNew的对象
     */
    public static <T> ObservableTransformer<T, T> applySchedulerAndAllFilter(Context context) {
        return applySchedulersAndAllFilter(context, new SimpleErrorVerify(context));
    }

}

