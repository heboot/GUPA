package com.ruguo.gupa.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.R;
import com.ruguo.gupa.base.BaseActivity;
import com.ruguo.gupa.data.ConfigModel;
import com.ruguo.gupa.databinding.ActivityMainBinding;
import com.ruguo.gupa.dialog.ProgressDialog;
import com.ruguo.gupa.service.MAccService;
import com.ruguo.gupa.task.GPTask;
import com.ruguo.gupa.utils.IntentUtils;
import com.ruguo.gupa.utils.LogUtils;
import com.ruguo.gupa.utils.MiscUtils;
import com.ruguo.gupa.utils.StringUtils;
import com.ruguo.gupa.utils.ToastUtils;

import java.io.IOException;

import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private QMUIDialog tipDialog;

    private QMUIDialog updateDialog;

    private ProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {
        AVQuery<ConfigModel> configQuery = new AVQuery<>("app_config");
        configQuery.getFirstInBackground().subscribe(new Observer<ConfigModel>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ConfigModel configModel) {
                binding.tvVersion.setText("当前版本" + MiscUtils.getAppVersion(MainActivity.this) + "/最新版本" + configModel.getVersion());
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.E(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void initListener() {
        binding.llytStartTT.setOnClickListener(view -> {
            if (MAPP.mapp.getCurrentUser() == null || StringUtils.isEmpty(MAPP.mapp.getCurrentUser().getUsername())) {
                IntentUtils.doIntent(MainActivity.this, LoginActivity.class);
                return;
            }
            if (!MAccService.check()) {
                tipDialog = new QMUIDialog.MessageDialogBuilder(MainActivity.this).addAction("去设置", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                }).setMessage("请先打开无障碍/服务权限").create();
                tipDialog.show();
                return;
            }
            if (MAPP.mapp.getCurrentAccounts() == null || MAPP.mapp.getCurrentAccounts().size() <= 0) {
                ToastUtils.ToastError("请先录入您要切换的账号");
                return;
            }
            if (MAPP.mapp.getDataModel() == null || MAPP.mapp.getDataModel().getCommentContentList() == null || MAPP.mapp.getDataModel().getCommentContentList().size() <= 0) {
                ToastUtils.ToastError("请先录入您要发送的文本");
                return;
            }
            GPTask.startDZH();
        });
        binding.llytInputAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MAPP.mapp.getCurrentUser() == null || StringUtils.isEmpty(MAPP.mapp.getCurrentUser().getUsername())) {
                    IntentUtils.doIntent(MainActivity.this, LoginActivity.class);
                    return;
                }
                IntentUtils.doIntent(MainActivity.this, InputAccountActivity.class);
            }
        });

        binding.llytCommont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MAPP.mapp.getCurrentUser() == null || StringUtils.isEmpty(MAPP.mapp.getCurrentUser().getUsername())) {
                    IntentUtils.doIntent(MainActivity.this, LoginActivity.class);
                    return;
                }
                IntentUtils.doIntent(MainActivity.this, InputActivity.class);
            }
        });
        binding.llytSetting.setOnClickListener(view->{
            if (MAPP.mapp.getCurrentUser() == null || StringUtils.isEmpty(MAPP.mapp.getCurrentUser().getUsername())) {
                IntentUtils.doIntent(MainActivity.this, LoginActivity.class);
                return;
            }
            IntentUtils.doIntent(MainActivity.this, SettingActivity.class);
        });
        binding.llytUpdate.setOnClickListener(view->{
//            try {
//               new ProcessBuilder("input tap 810 720").start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            try {
                Runtime.getRuntime().exec("input tap 810 720");
            } catch (IOException e) {
                e.printStackTrace();
            }

//            if (MAPP.mapp.getCurrentUser() == null || StringUtils.isEmpty(MAPP.mapp.getCurrentUser().getUsername())) {
//                IntentUtils.doIntent(MainActivity.this, LoginActivity.class);
//                return;
//            }
//
//            AVQuery<ConfigModel> configQuery = new AVQuery<>("app_config");
//            configQuery.getFirstInBackground().subscribe(new Observer<ConfigModel>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//                }
//
//                @Override
//                public void onNext(ConfigModel configModel) {
//                    binding.tvVersion.setText("当前版本" + MiscUtils.getAppVersion(MainActivity.this) + "/最新版本" + configModel.getVersion());
//                    MAPP.mapp.setMyConfigModel(configModel);
//
//                    if (MAPP.mapp.getMyConfigModel() != null && !MAPP.mapp.getMyConfigModel().getVersion().equals(MiscUtils.getAppVersion(MainActivity.this))) {
//                        updateDialog = new QMUIDialog.MessageDialogBuilder(MainActivity.this).setTitle("新版本").addAction("更新", new QMUIDialogAction.ActionListener() {
//                            @Override
//                            public void onClick(QMUIDialog dialog, int index) {
//                                updateDialog.dismiss();
//                                progressDialog = new ProgressDialog(MAPP.mapp.getMyConfigModel().getApk_url(), "scriptapp");
//                                progressDialog.show(getSupportFragmentManager(),"");
//
//                            }
//                        }).setMessage(MAPP.mapp.getMyConfigModel().getContent()).create();
//                        updateDialog.show();
//                        return;
//                    } else {
//                        ToastUtils.ToastSuccess( "您已经是最新版");
//                    }
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    LogUtils.E(TAG, e.getMessage());
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
        });
    }
}
