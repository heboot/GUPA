package com.ruguo.gupa.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.data.DZHData;
import com.ruguo.gupa.data.MMessageWhat;
import com.ruguo.gupa.utils.APPUtils;
import com.ruguo.gupa.utils.AccServiceUtils;
import com.ruguo.gupa.utils.LocalDataUtils;
import com.ruguo.gupa.utils.ToastUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.waw.hr.mutils.LogUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.ruguo.gupa.data.MMessageWhat.CLICK_CHATPAGE_SEND;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_GROUP_ALLUSERS_TXT;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_GROUP_DETAIL_BTN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_HOMEPAGE_CHAT;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_HUIXIN_BTN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_LOGIN_CLEAN_ACCOUNT;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_LOGIN_EDIT_ACCOUNT;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_LOGIN_EDIT_PWD;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_LOGIN_LOGIN_BTN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_MY_BTN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_OTHER_LOGIN;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_QIEHUAN_ACCOUNT;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_REQUN_TAB;
import static com.ruguo.gupa.data.MMessageWhat.CLICK_USER_INFO_HEAD;
import static com.ruguo.gupa.data.MMessageWhat.FIND_CHATPAGE_EDITTEXT;
import static com.ruguo.gupa.data.MMessageWhat.FIND_GROUP_LISTVIEW;
import static com.ruguo.gupa.data.MMessageWhat.FIND_GROUP_LISTVIEW2;
import static com.ruguo.gupa.data.MMessageWhat.FIND_GROUP_USERS_GRIDVIEW;
import static com.ruguo.gupa.data.MMessageWhat.GOTO_CLICK_MY_BTN;
import static com.ruguo.gupa.data.MMessageWhat.LOGIN_SUC;
import static com.ruguo.gupa.data.MMessageWhat.SEND_ED_BACK;
import static com.ruguo.gupa.data.MMessageWhat.SEND_ED_BACK2;
import static com.ruguo.gupa.data.MMessageWhat.SEND_ED_BACK3;

public class MHandlerByRunTime extends Handler {

    //找不到控件重试次数
    public int repatCount = 0;

    //随机数 获取列表点击的某一项  0 - 6
    private int listIndex = 0, gridIndex = 0;

    private Random random;

    public static int currentSendIndex = 1;

    private int currentAccountIndex = 0;


    @Override
    public void handleMessage(@NonNull Message msg) {
        LogUtil.e("当前执行", msg.what + ">");
        if (APPUtils.isDialog(MAPP.mapp.getActivityInfo().getClsName())) {
            clickDialog();
            MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
        if (APPUtils.isTongbuDialog()) {
            clickTongBuDialog();
        }
        if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
            repatCount = 0;
            removeMessages(msg.what);
            sendEmptyMessageDelayed(APPUtils.getCurrentPageName(MAPP.mapp.getActivityInfo().getClsName()), 5000);
            return;
        }

        if (msg.what == -1) {
            repatCount = 0;
            removeMessages(msg.what);
            if (currentSendIndex >= LocalDataUtils.getSendCount()) {
                sendEmptyMessageDelayed(GOTO_CLICK_MY_BTN, 5000);
            } else {
                sendEmptyMessageDelayed(APPUtils.getCurrentPageName(MAPP.mapp.getActivityInfo().getClsName()), 5000);
            }

        } else if (msg.what == GOTO_CLICK_MY_BTN) {
            if (APPUtils.isMainPage(MAPP.mapp.getActivityInfo().getClsName())) {
                sendEmptyMessageDelayed(GOTO_CLICK_MY_BTN, 5000);
            } else {
                sendEmptyMessageDelayed(GOTO_CLICK_MY_BTN, 5000);
            }
        } else if (msg.what == CLICK_HUIXIN_BTN) {
            if (currentSendIndex >= LocalDataUtils.getSendCount()) {
                sendEmptyMessageDelayed(CLICK_MY_BTN, 5000);
            } else {
                clickHuixinBtn();
            }

        } else if (msg.what == CLICK_REQUN_TAB) {
            if (currentSendIndex >= LocalDataUtils.getSendCount()) {
                sendEmptyMessageDelayed(CLICK_MY_BTN, 5000);
            } else {
                clickRequnTab();
            }
        } else if (msg.what == FIND_GROUP_LISTVIEW) {
            if (currentSendIndex >= LocalDataUtils.getSendCount()) {
                sendEmptyMessageDelayed(CLICK_MY_BTN, 5000);
            } else {
                scrollGroupListView();
            }
        } else if (msg.what == FIND_GROUP_LISTVIEW2) {
            MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            sendEmptyMessageDelayed(FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
        } else if (msg.what == CLICK_GROUP_DETAIL_BTN) {
            //这个时候如果还在首页就是不正常了 继续执行上一步
            if (APPUtils.isMainPage(MAPP.mapp.getActivityInfo().getClsName())) {
                sendEmptyMessageDelayed(FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
                return;
            }
            clickGroupDetailBtn();
        } else if (msg.what == CLICK_GROUP_ALLUSERS_TXT) {
            //这个时候如果还在首页就是不正常了 继续执行上一步
            if (APPUtils.isMainPage(MAPP.mapp.getActivityInfo().getClsName())) {
                sendEmptyMessageDelayed(FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
                return;
            }
            cliclAllGroupTextView();
        } else if (msg.what == FIND_GROUP_USERS_GRIDVIEW) {
            //这个时候如果还在首页就是不正常了 继续执行上一步
            if (APPUtils.isMainPage(MAPP.mapp.getActivityInfo().getClsName())) {
                sendEmptyMessageDelayed(FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
                return;
            }
            //这个时候还在群管理页面 说明没有进入全部群成员页面
            if (APPUtils.isGroupManagerPage()) {
                MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                sendEmptyMessageDelayed(FIND_GROUP_LISTVIEW2, LocalDataUtils.getJGTime());
            } else {
                if (APPUtils.isGroupAllUsersPage()) {
                    scrollGroupUsersGridView();
                } else {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                }

            }
        } else if (msg.what == CLICK_USER_INFO_HEAD) {

            clickUserHead();
        } else if (msg.what == CLICK_HOMEPAGE_CHAT) {
            clickHomepageChatBtn();
        } else if (msg.what == FIND_CHATPAGE_EDITTEXT) {
            if (APPUtils.isIMP2PMessageActivity()) {
                findChatInputEdittext();
            } else {
                sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
            }
        } else if (msg.what == CLICK_CHATPAGE_SEND) {
            if (APPUtils.isIMP2PMessageActivity()) {
                clickChatPageSend();
            } else {
                sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
            }
        } else if (msg.what == SEND_ED_BACK) {
            sendEDBack();
        } else if (msg.what == SEND_ED_BACK2) {
            sendEDBack2();
        } else if (msg.what == SEND_ED_BACK3) {
            if (currentSendIndex >= LocalDataUtils.getSendCount()) {
                MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                sendEmptyMessageDelayed(CLICK_MY_BTN, LocalDataUtils.getJGTime());
            } else {
                //继续发送
                scrollGroupUsersGridView();
            }
        } else if (msg.what == CLICK_MY_BTN) {
            findClickMy();
        } else if (msg.what == CLICK_QIEHUAN_ACCOUNT) {
            clickQiehuan();
        } else if (msg.what == CLICK_OTHER_LOGIN) {
            clickOtherLogin();
        } else if (msg.what == CLICK_LOGIN_CLEAN_ACCOUNT) {
            clickClearAccount();
        } else if (msg.what == CLICK_LOGIN_EDIT_ACCOUNT) {
            clickAccountEdit();
        } else if (msg.what == CLICK_LOGIN_EDIT_PWD) {
            clickPwdEdit();
        } else if (msg.what == CLICK_LOGIN_LOGIN_BTN) {
            clickLoginbtn();
        } else if (msg.what == LOGIN_SUC) {
            //如果还停留在登录页面
            if (APPUtils.isLoginPage(MAPP.mapp.getActivityInfo().getClsName())) {
                sendEmptyMessageDelayed(CLICK_LOGIN_LOGIN_BTN, LocalDataUtils.getJGTime());
            } else {
                currentSendIndex = 0;
                MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                sendEmptyMessageDelayed(CLICK_HUIXIN_BTN, LocalDataUtils.getJGTime());
            }

        }


    }

    /**
     * 点击切换账号后的弹窗
     */
    private void clickTongBuDialog() {
        AccessibilityNodeInfo clean = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/sync_self_confirm");
        if (clean == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/sync_self_confirm")) {
                clean = DZHData.needNodeInfoList.get("com.android.dazhihui:id/sync_self_confirm");
                clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
            }
        } else {
            clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
        }
    }

    /**
     * 点击某些弹窗
     */
    private void clickDialog() {
        AccessibilityNodeInfo clean = AccServiceUtils.findInfoById(MAccService.root, "android:id/button3");
        if (clean == null) {
            if (DZHData.needNodeInfoList.containsKey("android:id/button3")) {
                clean = DZHData.needNodeInfoList.get("android:id/button3");
                clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            }
        } else {
            clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);

        }
    }


    /**
     * 点击登录页面的 登录按钮
     */
    private void clickLoginbtn() {
        AccessibilityNodeInfo clean = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/confirmBtn");
        if (clean == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/confirmBtn")) {
                repatCount = 0;
                clean = DZHData.needNodeInfoList.get("com.android.dazhihui:id/confirmBtn");
                clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                sendEmptyMessageDelayed(LOGIN_SUC, 10000);
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到其他登录按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到其他登录按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_LOGIN_LOGIN_BTN, 10000);
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(LOGIN_SUC, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 点击登录页面的密码输入框
     */
    private void clickPwdEdit() {
        AccessibilityNodeInfo input = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/loginPwd");
        if (input == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/loginPwd")) {
                repatCount = 0;
                input = DZHData.needNodeInfoList.get("com.android.dazhihui:id/loginPwd");
                input.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, LocalDataUtils.getCurrentAccount(currentAccountIndex).getPwd());
                input.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                sendEmptyMessageDelayed(CLICK_LOGIN_LOGIN_BTN, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    ToastUtils.ToastError("没有找到其他登录按钮，重试失败，请联系管理员");
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    return;
                }
                ToastUtils.ToastError("没有找到其他登录按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_OTHER_LOGIN, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            input.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, LocalDataUtils.getCurrentAccount(currentAccountIndex).getPwd());
            input.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            sendEmptyMessageDelayed(CLICK_LOGIN_LOGIN_BTN, LocalDataUtils.getJGTime());
        }
    }

    /**
     * 点击登录页面的账号输入框
     */
    private void clickAccountEdit() {
        AccessibilityNodeInfo input = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/loginNick");
        if (input == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/loginNick")) {
                repatCount = 0;
                input = DZHData.needNodeInfoList.get("com.android.dazhihui:id/loginNick");

                currentAccountIndex = currentAccountIndex + 1;

                input.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, LocalDataUtils.getCurrentAccount(currentAccountIndex).getAccount());
                input.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                sendEmptyMessageDelayed(CLICK_LOGIN_EDIT_PWD, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到其他登录按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到其他登录按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_LOGIN_EDIT_ACCOUNT, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            input.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, LocalDataUtils.getCurrentAccount(currentAccountIndex).getAccount());
            input.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            sendEmptyMessageDelayed(CLICK_LOGIN_EDIT_PWD, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 点击登录页面的 清除账号框按钮
     */
    private void clickClearAccount() {
        AccessibilityNodeInfo clean = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/nickCancel");
        if (clean == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/nickCancel")) {
                repatCount = 0;
                clean = DZHData.needNodeInfoList.get("com.android.dazhihui:id/nickCancel");
                clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_LOGIN_EDIT_ACCOUNT, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到其他登录按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到其他登录按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_LOGIN_CLEAN_ACCOUNT, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            clean.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_LOGIN_EDIT_ACCOUNT, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 点击其他登录按钮
     */
    private void clickOtherLogin() {
        AccessibilityNodeInfo qiehuan = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/again_other_login_ll");
        if (qiehuan == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/again_other_login_ll")) {
                repatCount = 0;
                qiehuan = DZHData.needNodeInfoList.get("com.android.dazhihui:id/again_other_login_ll");
                qiehuan.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_LOGIN_CLEAN_ACCOUNT, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到其他登录按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到其他登录按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_OTHER_LOGIN, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            qiehuan.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_LOGIN_CLEAN_ACCOUNT, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 点击切换按钮
     */
    private void clickQiehuan() {
        AccessibilityNodeInfo qiehuan = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/switch_account_ll");
        if (qiehuan == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/switch_account_ll")) {
                repatCount = 0;
                qiehuan = DZHData.needNodeInfoList.get("com.android.dazhihui:id/switch_account_ll");
                qiehuan.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_OTHER_LOGIN, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到切换按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到切换按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(CLICK_QIEHUAN_ACCOUNT, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            qiehuan.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_OTHER_LOGIN, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 查找左上角我的按钮
     */
    private void findClickMy() {

        AccessibilityNodeInfo myIcon = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/messageBtn");
        if (myIcon == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/messageBtn")) {
                repatCount = 0;
                myIcon = DZHData.needNodeInfoList.get("com.android.dazhihui:id/messageBtn");
                myIcon.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_QIEHUAN_ACCOUNT, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到我的按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到我的按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.SEND_ED_BACK3, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            myIcon.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_QIEHUAN_ACCOUNT, LocalDataUtils.getJGTime());
        }
    }


    private void sendEDBack() {
        MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        sendEmptyMessageDelayed(SEND_ED_BACK2, 3000);

    }

    private void sendEDBack2() {
        MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        sendEmptyMessageDelayed(SEND_ED_BACK3, 3000);
    }

    /**
     * 点击聊天页面的发送按钮
     */
    private void clickChatPageSend() {
        AccessibilityNodeInfo sendBtn = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/buttonSendMessage");
        if (sendBtn == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/buttonSendMessage")) {
                repatCount = 0;
                sendBtn = DZHData.needNodeInfoList.get("com.android.dazhihui:id/buttonSendMessage");
                sendBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                currentSendIndex = currentSendIndex + 1;
                sendEmptyMessageDelayed(MMessageWhat.SEND_ED_BACK, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到发送按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到发送按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(CLICK_HUIXIN_BTN, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            sendBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            currentSendIndex = currentSendIndex + 1;
            sendEmptyMessageDelayed(MMessageWhat.SEND_ED_BACK, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 查找聊天页面的输入框
     */
    private void findChatInputEdittext() {
        AccessibilityNodeInfo input = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/editTextMessage");
        if (input == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/editTextMessage")) {
                repatCount = 0;
                input = DZHData.needNodeInfoList.get("com.android.dazhihui:id/editTextMessage");
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, LocalDataUtils.getRandomComment());
                input.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                input.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_CHATPAGE_SEND, LocalDataUtils.getJGTime());
                ToastUtils.ToastSuccess("准备发送");
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到头像区域，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到头像区域，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_CHATPAGE_SEND, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, LocalDataUtils.getRandomComment());
            input.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            sendEmptyMessageDelayed(CLICK_CHATPAGE_SEND, LocalDataUtils.getJGTime());
            ToastUtils.ToastSuccess("准备发送");
        }
    }


    /**
     * 点击用户个人主页上的 聊天按钮
     */
    private void clickHomepageChatBtn() {
        AccessibilityNodeInfo huixinBtn = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/ll_chat");
        if (huixinBtn == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/ll_chat")) {
                repatCount = 0;
                huixinBtn = DZHData.needNodeInfoList.get("com.android.dazhihui:id/ll_chat");
                huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(FIND_CHATPAGE_EDITTEXT, LocalDataUtils.getJGTime());
                ToastUtils.ToastSuccess("准备写字");
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到聊天按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到聊天按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_HOMEPAGE_CHAT, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(FIND_CHATPAGE_EDITTEXT, LocalDataUtils.getJGTime());
            ToastUtils.ToastSuccess("准备写字");
        }
    }


    /**
     * 点击用户资料页面 上面用户头像的区域
     */
    private void clickUserHead() {
        AccessibilityNodeInfo huixinBtn = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/head_rl");
        if (huixinBtn == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/head_rl")) {
                repatCount = 0;
                huixinBtn = DZHData.needNodeInfoList.get("com.android.dazhihui:id/head_rl");
                huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_HOMEPAGE_CHAT, LocalDataUtils.getJGTime());
                ToastUtils.ToastSuccess("准备点击聊天按钮");
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到头像区域，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到头像区域，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_USER_INFO_HEAD, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_HOMEPAGE_CHAT, LocalDataUtils.getJGTime());
            ToastUtils.ToastSuccess("准备点击聊天按钮");

        }
    }


    private AccessibilityNodeInfo groupListView;
    /**
     * 找到全部成员列表的gridview 滑动一下 随机点
     */
    private void scrollGroupUsersGridView() {
          groupListView = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/gridview_member");
        if (groupListView == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/gridview_member")) {
                repatCount = 0;
                groupListView = DZHData.needNodeInfoList.get("com.android.dazhihui:id/gridview_member");
                for(int c =  0; c < LocalDataUtils.getScrollCount();c++){
                    groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    Observable.timer(2000, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            try {
                                groupListView.getChild(getGridViewIndex()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                sendEmptyMessageDelayed(CLICK_USER_INFO_HEAD, LocalDataUtils.getJGTime());
                                ToastUtils.ToastSuccess("准备去个人主页");
                            } catch (Exception e) {
                                CrashReport.postCatchedException(e);
                                sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                            }
                        }
                    });
                }



            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
//                    scrollGroupUsersGridView2();
                    MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    MAccService.mInstatnce.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    sendEmptyMessageDelayed(MMessageWhat.FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
                    return;
                }
                ToastUtils.ToastError("没有找到群成员列表，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.FIND_GROUP_USERS_GRIDVIEW, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            groupListView.getChild(getListViewIndex()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_USER_INFO_HEAD, LocalDataUtils.getJGTime());
            ToastUtils.ToastSuccess("准备去个人主页");
        }
    }

    private void scrollGroupUsersGridView2() {
        AccessibilityNodeInfo groupListView = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/member_gv");
        if (groupListView == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/member_gv")) {
                repatCount = 0;
                groupListView = DZHData.needNodeInfoList.get("com.android.dazhihui:id/member_gv");
                groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                groupListView.getChild(getGridViewIndex2()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_USER_INFO_HEAD, LocalDataUtils.getJGTime());
                ToastUtils.ToastSuccess("准备去个人主页");

            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到群列表2，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到群列表，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.FIND_GROUP_USERS_GRIDVIEW, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            groupListView.getChild(getListViewIndex()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_USER_INFO_HEAD, LocalDataUtils.getJGTime());
            ToastUtils.ToastSuccess("准备去个人主页");
        }
    }


    /**
     * 点击全部群成员按钮
     */
    private void cliclAllGroupTextView() {
        AccessibilityNodeInfo allGroupUsersText = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/member_more");
        if (allGroupUsersText == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/member_more")) {
                repatCount = 0;
                allGroupUsersText = DZHData.needNodeInfoList.get("com.android.dazhihui:id/member_more");
                allGroupUsersText.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(FIND_GROUP_USERS_GRIDVIEW, LocalDataUtils.getJGTime());
                ToastUtils.ToastSuccess("准备随机点击群成员");
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到全部群成员，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到全部群成员，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_GROUP_ALLUSERS_TXT, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            allGroupUsersText.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(FIND_GROUP_USERS_GRIDVIEW, LocalDataUtils.getJGTime());
            ToastUtils.ToastSuccess("准备随机点击群成员");
        }
    }


    /**
     * 点击群右上角的群详情按钮
     */
    private void clickGroupDetailBtn() {
        AccessibilityNodeInfo huixinBtn = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/group_right");
        if (huixinBtn == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/group_right")) {
                repatCount = 0;
                huixinBtn = DZHData.needNodeInfoList.get("com.android.dazhihui:id/group_right");
                huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                ToastUtils.ToastSuccess("准备点击全部群成员");
                sendEmptyMessageDelayed(CLICK_GROUP_ALLUSERS_TXT, LocalDataUtils.getJGTime());
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到成员按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到成员按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(CLICK_GROUP_ALLUSERS_TXT, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            ToastUtils.ToastSuccess("准备点击全部群成员");
            sendEmptyMessageDelayed(CLICK_GROUP_ALLUSERS_TXT, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 滑动列表 并随机点击某一项
     */
    private void scrollGroupListView() {
        AccessibilityNodeInfo groupListView = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/listView");
        if (groupListView == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/listView")) {
                repatCount = 0;
                groupListView = DZHData.needNodeInfoList.get("com.android.dazhihui:id/listView");
                groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                try {
                    groupListView.getChild(getListViewIndex()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sendEmptyMessageDelayed(CLICK_GROUP_DETAIL_BTN, LocalDataUtils.getJGTime());
                } catch (Exception e) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                }


            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到群列表，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到群列表，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            groupListView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            groupListView.getChild(getListViewIndex()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_GROUP_DETAIL_BTN, LocalDataUtils.getJGTime());

        }
    }

    /**
     * 点击热群tab
     */
    private void clickRequnTab() {
        AccessibilityNodeInfo requntab = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/scrollview_radioGroup");
        if (requntab == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/scrollview_radioGroup")) {
                repatCount = 0;
                requntab = DZHData.needNodeInfoList.get("com.android.dazhihui:id/scrollview_radioGroup");
                try {
                    requntab.getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    ToastUtils.ToastSuccess("先滑动列表,再随机点群");
                    sendEmptyMessageDelayed(FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
                } catch (Exception ex) {
                    CrashReport.postCatchedException(ex);
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                }

            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到热群Tab，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到热群Tab，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(MMessageWhat.CLICK_REQUN_TAB, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            requntab.getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            ToastUtils.ToastSuccess("先滑动列表,再随机点群");
            sendEmptyMessageDelayed(FIND_GROUP_LISTVIEW, LocalDataUtils.getJGTime());
        }
    }


    /**
     * 点击底部慧信按钮
     */
    private void clickHuixinBtn() {
        AccessibilityNodeInfo huixinBtn = AccServiceUtils.findInfoById(MAccService.root, "com.android.dazhihui:id/bottom_menu_button_2");
        if (huixinBtn == null) {
            if (DZHData.needNodeInfoList.containsKey("com.android.dazhihui:id/bottom_menu_button_2")) {
                repatCount = 0;
                huixinBtn = DZHData.needNodeInfoList.get("com.android.dazhihui:id/bottom_menu_button_2");
                huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendEmptyMessageDelayed(CLICK_REQUN_TAB, LocalDataUtils.getJGTime());
                ToastUtils.ToastSuccess("准备去点击热群tab");
            } else {
                if (repatCount >= LocalDataUtils.getMaxRepatCount()) {
                    sendEmptyMessageDelayed(-1, LocalDataUtils.getJGTime());
                    ToastUtils.ToastError("没有找到慧信按钮，重试失败，请联系管理员");
                    return;
                }
                ToastUtils.ToastError("没有找到慧信按钮，" + LocalDataUtils.getJGTime() / 1000 + "秒后重试");
                sendEmptyMessageDelayed(CLICK_HUIXIN_BTN, LocalDataUtils.getJGTime());
                repatCount = repatCount + 1;
            }
        } else {
            repatCount = 0;
            huixinBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sendEmptyMessageDelayed(CLICK_REQUN_TAB, LocalDataUtils.getJGTime());
            ToastUtils.ToastSuccess("准备去点击热群tab");
        }
    }


    private int getGridViewIndex() {
        if (random == null) {
            random = new Random();
        }
        gridIndex = random.nextInt(40);
        return gridIndex;
    }

    private int getGridViewIndex2() {
        if (random == null) {
            random = new Random();
        }
        gridIndex = random.nextInt(13);
        return gridIndex;
    }

    private int getListViewIndex() {
        if (random == null) {
            random = new Random();
        }
        listIndex = random.nextInt(7);
        return listIndex;
    }
}
