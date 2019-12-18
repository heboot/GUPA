package com.ruguo.gupa;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.ruguo.gupa.data.ConfigModel;
import com.ruguo.gupa.data.DZHData;
import com.ruguo.gupa.data.MActivityInfo;
import com.ruguo.gupa.data.MDataModel;
import com.ruguo.gupa.utils.LogUtils;
import com.ruguo.gupa.utils.PreferencesUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVUser;

public class MAPP extends Application {

    public static MAPP mapp;

    private MActivityInfo activityInfo;

    private List<String> needIdsList = new ArrayList<>();

    private MDataModel dataModel;

    private List<String> currentAccounts;


    private ConfigModel myConfigModel;


    @Override
    public void onCreate() {
        super.onCreate();
        this.mapp = this;

        if (DZHData.needNodeInfoList == null) {
            DZHData.needNodeInfoList = new HashMap<>();
        }

        needIdsList.add("com.android.dazhihui:id/bottom_menu_button_2");
//        needIdsList.add("com.android.dazhihui:id/radioGroup"); //这玩意找不到
        needIdsList.add("com.android.dazhihui:id/scrollview_radioGroup");
        needIdsList.add("com.android.dazhihui:id/listView");
        needIdsList.add("com.android.dazhihui:id/group_right");
        needIdsList.add("com.android.dazhihui:id/member_more");
        needIdsList.add("com.android.dazhihui:id/member_gv");
        needIdsList.add("com.android.dazhihui:id/gridview_member");
        needIdsList.add("com.android.dazhihui:id/head_rl");
        needIdsList.add("com.android.dazhihui:id/ll_chat");
        needIdsList.add("com.android.dazhihui:id/editTextMessage");
        needIdsList.add("com.android.dazhihui:id/buttonSendMessage");


        needIdsList.add("com.android.dazhihui:id/messageBtn");
        needIdsList.add("com.android.dazhihui:id/switch_account_ll");
        needIdsList.add("com.android.dazhihui:id/again_other_login_ll");
        needIdsList.add("com.android.dazhihui:id/nickCancel");
        needIdsList.add("com.android.dazhihui:id/loginNick");
        needIdsList.add("com.android.dazhihui:id/loginPwd");
        needIdsList.add("com.android.dazhihui:id/confirmBtn");

        needIdsList.add("android:id/button3");
        needIdsList.add("com.android.dazhihui:id/sync_self_confirm");


        String s = PreferencesUtils.getString(this, "accounts");
        dataModel = (MDataModel)PreferencesUtils.getObj(MAPP.mapp, "mdatamodel");

        currentAccounts =
                JSON.parseArray(s, String.class);

        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);// or AVOSCloud.setLogLevel(AVLogger.Level.VERBOSE);
        AVOSCloud.initialize(this, "hQ2I9OOI1FVW7i4GLDHSjbdk-gzGzoHsz", "eBXAfB23IGL6ejCpis08DeHE", "https://hq2i9ooi.lc-cn-n1-shared.com");
        AVObject.registerSubclass(ConfigModel.class);

        CrashReport.initCrashReport(getApplicationContext(), "135b272432", false);
    }

    public MActivityInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(MActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

    public List<String> getNeedIdsList() {
        return needIdsList;
    }

    public MDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(MDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public List<String> getCurrentAccounts() {
        return currentAccounts;
    }

    public void setCurrentAccounts(List<String> currentAccounts) {
        this.currentAccounts = currentAccounts;
    }

    public AVUser getCurrentUser() {
        return AVUser.getCurrentUser();
    }

    public ConfigModel getMyConfigModel() {
        return myConfigModel;
    }

    public void setMyConfigModel(ConfigModel myConfigModel) {
        this.myConfigModel = myConfigModel;
    }
}
