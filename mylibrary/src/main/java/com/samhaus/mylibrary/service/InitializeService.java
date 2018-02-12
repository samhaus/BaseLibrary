package com.samhaus.mylibrary.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import com.github.moduth.blockcanary.BlockCanary;
import com.samhaus.mylibrary.BuildConfig;
import com.samhaus.mylibrary.application.AppBlockCanaryContext;
import com.samhaus.mylibrary.application.AppCrashHandler;
import com.samhaus.mylibrary.base.AppContext;
import com.squareup.leakcanary.LeakCanary;


public class InitializeService extends IntentService {
    public static final String ACTION_INIT_LIBRARY = "service.action.initThirdLibrary";

    public InitializeService() {
        super("InitializeService");
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setPackage(context.getPackageName());
        intent.setAction(ACTION_INIT_LIBRARY);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_LIBRARY.equals(action)) {
                handleAction();
            }
        }
    }

    private void handleAction() {
        if (LeakCanary.isInAnalyzerProcess(AppContext.mAppContext)) {
            return;
        }
        //api网关
//        AppConfigurationInitializer.init();
        //初始化自定义字体
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/black.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());
        //chrome调试
//        Stetho.initializeWithDefaults(App.getInstance());
        //bugly
//        CrashReport.initCrashReport(getApplicationContext(), AppConstants.BUGLY_APPID, false);
        if (BuildConfig.DEBUG) {
//            initStrictMode();
            LeakCanary.install(AppContext.getContext());
            BlockCanary.install(this, new AppBlockCanaryContext(getApplicationContext())).start();
        }else {
            AppCrashHandler.getInstance().init();
        }
    }

    private void initStrictMode() {
        StrictMode.noteSlowCall("slowCallInThread");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls()//自定义的耗时调用
                .detectDiskReads()//磁盘读取操作
                .detectDiskWrites()//磁盘写入操作
                .detectNetwork()//网络操作
                .detectAll()
                .penaltyDeath()
                .penaltyLog()
                .penaltyDialog()
                .penaltyFlashScreen()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
