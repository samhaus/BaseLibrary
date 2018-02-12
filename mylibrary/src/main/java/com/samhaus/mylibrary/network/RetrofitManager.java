package com.samhaus.mylibrary.network;

/**
 * Created by samhaus on 2017/9/6.
 */

import android.text.TextUtils;

import com.samhaus.mylibrary.base.AppContext;
import com.samhaus.mylibrary.util.AppConstants;
import com.samhaus.mylibrary.util.LogUtil;
import com.samhaus.mylibrary.util.NetUtil;
import com.samhaus.mylibrary.util.SPUtil;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit2管理类
 */
public class RetrofitManager {

    private volatile static OkHttpClient mOkHttpClient;
    //缓存有效期
    private static final int CACHE_STALE_LONG = 60 * 60;

    public static Retrofit getRetrofit(String baseUrl) {
        initOkHttpClient();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小20Mb
                    Cache cache = new Cache(new File(AppContext.mAppContext.getCacheDir(), "Samhaus/HttpCache"),
                            1024 * 1024 * 20);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(logger)
                            .addInterceptor(requestInterceptor)
                            .addInterceptor(cacheInterceptor)
                            .addNetworkInterceptor(cacheInterceptor)
                            .cookieJar(new CookieJar() {
                                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                                @Override
                                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                    cookieStore.put(url, cookies);
                                }

                                @Override
                                public List<Cookie> loadForRequest(HttpUrl url) {
                                    List<Cookie> cookies = cookieStore.get(url);
                                    return cookies != null ? cookies : new ArrayList<Cookie>();
                                }
                            })
                            .retryOnConnectionFailure(true)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    private static Interceptor logger = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtil.d(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            LogUtil.d(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtil.json(content);
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    // 响应头拦截器，用来配置缓存策略
    private static Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isConnected(AppContext.mAppContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (NetUtil.isConnected(AppContext.mAppContext)) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                KLog.e("retrofit", "network not connect");
                return response.newBuilder()
                        .removeHeader("Cache-Control")
                        .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    //设置请求拦截
    private static Interceptor requestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request;
            Request.Builder builder = original
                    .newBuilder()
                    .method(original.method(), original.body());

            String token = SPUtil.getInstance(AppContext.mAppContext).getStringValue(AppConstants.TOKEN);
            if (!TextUtils.isEmpty(token)) {
                builder.header("Authorization", token);
            }
            request = builder.build();
            return chain.proceed(request);
        }
    };


}
