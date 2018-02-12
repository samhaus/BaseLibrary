package com.samhaus.mylibrary.util;

import com.samhaus.mylibrary.BuildConfig;
import com.socks.library.KLog;

public class LogUtil {
    private static final boolean isDebug = BuildConfig.DEBUG;

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void i(String msg) {
        if (isDebug) {
            KLog.i(msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            KLog.i(msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            KLog.d(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            KLog.d(msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            KLog.e(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            KLog.e(tag, msg);
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            KLog.v(msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            KLog.v(msg);
        }
    }

    public static void json(String msg) {
        if (isDebug) {
            KLog.json(msg);
        }
    }

    public static void json(String tag, String msg) {
        if (isDebug) {
            KLog.json(tag, msg);
        }
    }
}
