package com.ruguo.gupa.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;
import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.data.DZHData;
import com.ruguo.gupa.data.MActivityInfo;
import com.ruguo.gupa.utils.LogUtils;


public class MAccService extends AccessibilityService {

    private static String TAG = MAccService.class.getName();

    public static MAccService mInstatnce = null;

    private MActivityInfo activityInfo;

    public static AccessibilityNodeInfo root;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //这段代码是为了不root当前在哪个app哪个页面
        //TYPE_WINDOW_CONTENT_CHANGED  2048    窗口的内容发生变化，或子树根布局发生变化 TYPE_WINDOW_STATE_CHANGED 32    打开了一个PopupWindow，Menu或Dialog TYPE_WINDOWS_CHANGED    屏幕上的窗口变化事件，需要API 21+ 4194304
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getEventType() == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
            if (event != null && event.getSource() != null) {

                if (activityInfo == null) {
                    activityInfo = new MActivityInfo();
                }
                if (event.getClassName() != null) {
                    activityInfo.setClsName(event.getClassName().toString());
                }
                if (event.getPackageName() != null) {
                    activityInfo.setPkgName(event.getPackageName().toString());
                }

                MAPP.mapp.setActivityInfo(activityInfo);
                LogUtils.E(TAG, "页面" + JSON.toJSONString(activityInfo) + "事件" + event.getEventType());
            }
        }

        root = event.getSource();
        recycle(getRootInActiveWindow());
    }


    private void recycle(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.getChildCount() == 0) {
                if (MAPP.mapp.getNeedIdsList().indexOf(accessibilityNodeInfo.getViewIdResourceName()) > -1) {
                    LogUtils.E("找到有需要备份的了", accessibilityNodeInfo.getViewIdResourceName());
                    DZHData.needNodeInfoList.put(accessibilityNodeInfo.getViewIdResourceName(),accessibilityNodeInfo);
                }
            } else {
                for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                    if (MAPP.mapp.getNeedIdsList().indexOf(accessibilityNodeInfo.getViewIdResourceName()) > -1) {
                        LogUtils.E("找到有需要备份的了", accessibilityNodeInfo.getViewIdResourceName());
                        DZHData.needNodeInfoList.put(accessibilityNodeInfo.getViewIdResourceName(),accessibilityNodeInfo);
                    }
                    recycle(accessibilityNodeInfo.getChild(i));
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
    }


    protected void onServiceConnected() {
        mInstatnce = this;
        super.onServiceConnected();
    }

    public boolean onUnbind(Intent paramIntent) {
        mInstatnce = null;
        return super.onUnbind(paramIntent);
    }

    private long lastEventTime;

    @Override
    protected boolean onKeyEvent(KeyEvent paramKeyEvent) {

//        if (lastEventTime > 0) {
//            if (Math.abs(paramKeyEvent.getEventTime() - lastEventTime) < 1000) {
//                return false;
//            }
//        }
//        //如果是音量下 就结束脚本
//        lastEventTime = paramKeyEvent.getEventTime();

        if (paramKeyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {

        }

        return super.onKeyEvent(paramKeyEvent);
    }


    public static boolean check() {
        return mInstatnce != null;
    }
}
