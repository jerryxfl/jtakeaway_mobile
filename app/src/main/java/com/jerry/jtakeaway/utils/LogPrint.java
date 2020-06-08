package com.jerry.jtakeaway.utils;

import android.util.Log;

/**
 * 日志打印
 */
public class LogPrint {
    public static enum logType {
        Info, Waring, Debug, Virtual
    }

    public static void print(String msg, logType logType) {
        switch (logType) {
            case Waring:
                Log.w("Jerry", msg);
                break;
            case Info:
                Log.i("Jerry", msg);
                break;
            case Debug:
                Log.d("Jerry", msg);
                break;
            case Virtual:
                Log.v("Jerry", msg);
                break;
            default:
                Log.i("Jerry", msg);
                break;

        }

    }
}
