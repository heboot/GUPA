package com.ruguo.gupa.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.ruguo.gupa.BuildConfig;

import java.io.File;

public class DownloadUtils {


//    public DownloadTask doDownloadAPK(String url, String appName, DownloadListener listener) {
//        DownloadTask task = new DownloadTask.Builder(url, SDCardUtils.getRootPathFilePath(), appName)
//                // the minimal interval millisecond for callback progress
//                .setMinIntervalMillisCallbackProcess(30)
//                // do re-download even if the task has already been completed in the past.
//                .setPassIfAlreadyCompleted(false)
//                .build();
//
//        task.enqueue(listener);
//
//        return task;
//
//// cancel
////        task.cancel();
//
//// execute task synchronized
////        task.execute(listener);
//    }


    public static void installAPP(Context context, String url) {
        // 核心是下面几句代码
        Intent intent;
        File file = new File(url);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            Uri apkUri = Uri.fromFile(file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

}
