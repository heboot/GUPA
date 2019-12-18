package com.ruguo.gupa.activity;

import android.Manifest;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.R;
import com.ruguo.gupa.base.BaseActivity;
import com.ruguo.gupa.databinding.ActivityLoginBinding;
import com.ruguo.gupa.utils.DialogUtils;
import com.ruguo.gupa.utils.IntentUtils;
import com.ruguo.gupa.utils.LogUtils;
import com.ruguo.gupa.utils.MiscUtils;
import com.ruguo.gupa.utils.PreferencesUtils;
import com.ruguo.gupa.utils.StringUtils;
import com.waw.hr.mutils.MStatusBarUtils;

import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements EasyPermissions.PermissionCallbacks {

    private QMUITipDialog loadingDialog;

    private Boolean has;

    private boolean hasPer = false;

    private  int READ_PHONE_STATE = 991;

    private  String[] perms = {Manifest.permission.READ_PHONE_STATE};

    private QMUIDialog perDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
        perDialog = new QMUIDialog.MessageDialogBuilder(this)
                .setMessage("需要权限才能使用").
                        addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                perDialog.dismiss();
                                reqPer();
                            }
                        })
                .create();
    }

    @Override
    public void initData() {
        reqPer();
        loadingDialog.show();
        if (MAPP.mapp.getCurrentUser() != null && !StringUtils.isEmpty(MAPP.mapp.getCurrentUser().getUsername())) {
            IntentUtils.doIntent(LoginActivity.this, MainActivity.class);
            loadingDialog.dismiss();
            finish();
        }else{loadingDialog.dismiss();
        }
    }

    private void reqPer(){
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "需要读取您的手机状态",
                    READ_PHONE_STATE, perms);
        }
    }

    @Override
    public void initListener() {
        binding.btnLogin.setOnClickListener((v) -> {

            if(!EasyPermissions.hasPermissions(this, perms)){
                perDialog.show();
                return;
            }

            if (StringUtils.isEmpty(binding.etAccount.getText())) {
                tipDialog = DialogUtils.getFailDialog(this, "请输入账号", true);
                tipDialog.show();
                return;
            }

            if (StringUtils.isEmpty(binding.etPwd.getText())) {
                tipDialog = DialogUtils.getFailDialog(this, "请输入密码", true);
                tipDialog.show();
                return;
            }

            loadingDialog.show();

            AVUser.logIn(binding.etAccount.getText().toString(), binding.etPwd.getText().toString()).subscribe(new Observer<AVUser>() {
                public void onSubscribe(Disposable disposable) {
                }

                public void onNext(AVUser avUser) {
                    loadingDialog.dismiss();
                    if (avUser == null) {
                        tipDialog = DialogUtils.getFailDialog(LoginActivity.this, "用户不存在", true);
                        tipDialog.show();
                    } else {
                        AVQuery<AVObject> query = new AVQuery<>("tb_device");
                        query.whereEqualTo("user_name", avUser.getUsername());
                        query.countInBackground().subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer count) throws Exception {

                                if (count == null) {
                                    count = 0;
                                }
                                if (count >=   (int) avUser.get("authorization")) {
                                    loadingDialog.dismiss();
                                    tipDialog = DialogUtils.getFailDialog(LoginActivity.this, "设备数量已达上限", true);
                                    tipDialog.show();
                                    AVUser.logOut();
                                    return;
                                } else {
                                    query.whereEqualTo("device_id", MiscUtils.getIMEI(LoginActivity.this)).getFirstInBackground().subscribe(new Observer<AVObject>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            LogUtils.E(TAG, "><>>>>>>> onSubscribe");
                                        }

                                        @Override
                                        public void onNext(AVObject result) {
                                            loadingDialog.dismiss();
                                            if (result == null) {
                                                has = false;
                                                AVObject avObject = new AVObject("tb_device");
                                                avObject.put("user_name", avUser.getUsername());
                                                avObject.put("device_id", MiscUtils.getIMEI(LoginActivity.this));
                                                avObject.saveInBackground().subscribe();
                                                saveUser();
                                                IntentUtils.doIntent(LoginActivity.this, MainActivity.class);
                                                finish();
                                            } else {
                                                has = true;
                                                result.saveInBackground().subscribe();
                                                saveUser();
                                                IntentUtils.doIntent(LoginActivity.this, MainActivity.class);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            LogUtils.E(TAG, "><>>>>>>>" + e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {
                                            if (has == null) {
                                                AVObject avObject = new AVObject("tb_device");
                                                avObject.put("user_name", avUser.getUsername());
                                                avObject.put("device_id", MiscUtils.getIMEI(LoginActivity.this));
                                                avObject.saveInBackground().subscribe();
                                                saveUser();
                                                IntentUtils.doIntent(LoginActivity.this, MainActivity.class);
                                                finish();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                }

                public void onError(Throwable throwable) {
                    LogUtils.E("登录出错",throwable.getMessage());
                    LogUtils.E("登录出错", JSON.toJSONString(throwable));
                    loadingDialog.dismiss();
                    // 登录失败（可能是密码错误）
                    tipDialog = DialogUtils.getFailDialog(LoginActivity.this, "用户不存在/密码错误", true);
                    tipDialog.show();
                }

                public void onComplete() {
                }
            });
        });
    }


    private void saveUser(){
        PreferencesUtils.saveObj(this,"user",MAPP.mapp.getCurrentUser());
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        hasPer = true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
 perDialog.show();
    }
}
