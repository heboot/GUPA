package com.waw.hr.mutils;

import android.util.Log;

public class LogUtil {

    public static void e(String TAG, String msg) {
//        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
//        }
    }

    public static void i(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

}
