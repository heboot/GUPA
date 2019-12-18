package com.ruguo.gupa.utils;

import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.service.MAccService;
import com.ruguo.gupa.service.MHandler;

import static com.ruguo.gupa.data.MMessageWhat.CLICK_HUIXIN_BTN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_LOGIN_LOGIN_BTN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_OTHER_LOGIN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_QIEHUAN_ACCOUNT;
import static com.ruguo.gupa.data.MMessageWhat.FIND_GROUP_LISTVIEW2;
import static com.ruguo.gupa.data.MMessageWhat.FIND_GROUP_USERS_GRIDVIEW;

public class APPUtils {

    public static int getCurrentPageName(String s) {
        switch (s) {
            case "com.android.dazhihui.ui.screen.InitScreen":
                return CLICK_HUIXIN_BTN;
            case "com.android.dazhihui.ui.screen.stock.MainScreen":
                return CLICK_HUIXIN_BTN;
            case "com.android.dazhihui.ui.screen.stock.LoginScreen":
                return CLICK_HUIXIN_BTN;
            case "com.tencent.im.activity.IMP2PMessageActivity":
                return CLICK_LOGIN_LOGIN_BTN;
            case "com.tencent.im.activity.IMGroupSettingActivity":
                return FIND_GROUP_LISTVIEW2;
            case "com.tencent.im.activity.ChooseNewOwnerActivity":
                return FIND_GROUP_USERS_GRIDVIEW;
            case "com.tencent.im.activity.IMTeamMessageActivity":
                return FIND_GROUP_LISTVIEW2;
            case "com.android.dazhihui.ui.screen.stock.PersonalScreenActivity":
                return CLICK_QIEHUAN_ACCOUNT;
            case "com.android.dazhihui.ui.screen.stock.LoginMainScreen":
                return CLICK_OTHER_LOGIN;


        }
        return CLICK_HUIXIN_BTN;
    }







    public static boolean isIMP2PMessageActivity(){

        if(MAPP.mapp.getActivityInfo().getClsName().equals("com.tencent.im.activity.IMP2PMessageActivity")  ){
            return true;
        }
        return false;
    }

    public static boolean isGroupManagerPage(){

        if(MAPP.mapp.getActivityInfo().getClsName().equals("com.tencent.im.activity.IMGroupSettingActivity")  ){
            return true;
        }
        return false;
    }

    public static boolean isGroupAllUsersPage(){

        if(MAPP.mapp.getActivityInfo().getClsName().equals("com.tencent.im.activity.ChooseNewOwnerActivity")  ){
            return true;
        }
        return false;
    }

    public static boolean isLoginPage(String s){
        if(s.equals("com.android.dazhihui.ui.screen.stock.LoginScreen")){
            return true;
        }
        return false;
    }


    //程序出错 软件将重启的弹窗上面的  知道了按钮ID  android:id/button3
    //切换账号后出现的同步弹窗 com.android.dazhihui.ui.screen.dialog.SelfStockDialog
    public static boolean isDialog(String s){
        if(s.equals("android.app.Dialog")  ){
            MHandler.currentSendIndex = LocalDataUtils.getSendCount();
            return true;
        }else if ( s.equals("com.tencent.im.view.EasyProgressDialog") || s.equals("android.app.AlertDialog")){
            return true;
        }
        return false;
    }

    public static boolean isTongbuDialog(){
        if(MAPP.mapp.getActivityInfo().getClsName().equals("com.android.dazhihui.ui.screen.dialog.SelfStockDialog")  ){
            return true;
        }
        return false;
    }



    public static boolean isMainPage(String s){
        if(s.equals("com.android.dazhihui.ui.screen.stock.MainScreen")){
            return true;
        }
        return false;

    }

}
