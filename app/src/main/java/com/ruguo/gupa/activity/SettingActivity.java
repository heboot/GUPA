package com.ruguo.gupa.activity;

import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.R;
import com.ruguo.gupa.base.BaseActivity;
import com.ruguo.gupa.databinding.ActivitySettingBinding;
import com.ruguo.gupa.utils.LocalDataUtils;
import com.ruguo.gupa.utils.PreferencesUtils;
import com.ruguo.gupa.utils.ToastUtils;

import es.dmoral.toasty.Toasty;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    private QMUIDialog inputCountDialog;

    private QMUIDialog.EditTextDialogBuilder editTextDialogBuilder;

    private CharSequence[] versions = { "5秒(高配建议)", "8秒(旧手机建议)"};

    private QMUIDialog qmuiDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {
        qmuiDialog = new QMUIDialog.CheckableDialogBuilder(this)
                .addItems(versions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       switch (i){
//                           case 0:
//                               PreferencesUtils.putInt(MAPP.mapp, "jgTime", 2000);
//                               break;
                           case 0:
                               PreferencesUtils.putInt(MAPP.mapp, "jgTime", 5000);
                               break;
                           case 1:
                               PreferencesUtils.putInt(MAPP.mapp, "jgTime", 8000);
                               break;
                       }
                        Toasty.success(MAPP.mapp, "设置时间" + versions[i].toString() + "成功").show();
                        qmuiDialog.dismiss();
                    }
                })
                .create();

        editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(this)
                .setTitle("请输入发送信息的条数，达到条数后会自动切换账号")
                .setDefaultText(LocalDataUtils.getSendCount() > 0?LocalDataUtils.getSendCount()+"":5+"")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        PreferencesUtils.putInt(MAPP.mapp,"count",Integer.parseInt(editTextDialogBuilder.getEditText().getText().toString()));
                        ToastUtils.ToastSuccess("保存成功" +Integer.parseInt(editTextDialogBuilder.getEditText().getText().toString())+"条" );
                        inputCountDialog.dismiss();
                    }
                });
        inputCountDialog  = editTextDialogBuilder.create();
    }

    @Override
    public void initListener() {
        binding.btnSetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputCountDialog.show();
            }
        });
        binding.btnSetTime.setOnClickListener(view->{
            qmuiDialog.show();
        });
    }
}
