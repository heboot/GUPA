package com.ruguo.gupa.activity;

import com.alibaba.fastjson.JSON;
import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.R;
import com.ruguo.gupa.base.BaseActivity;
import com.ruguo.gupa.databinding.ActivityInputAccountBinding;
import com.ruguo.gupa.utils.LogUtils;
import com.ruguo.gupa.utils.PreferencesUtils;
import com.ruguo.gupa.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class InputAccountActivity extends BaseActivity<ActivityInputAccountBinding> {
    private List<String> accounts;

    private String zhanghaoStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_account;
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {


        zhanghaoStr = PreferencesUtils.getString(this, "accounts");

        if (StringUtils.isEmpty(zhanghaoStr)) {
            accounts = new ArrayList<>();
        } else {
            getLocalAccount();
        }

    }

    @Override
    public void initListener() {
        binding.btnInput.setOnClickListener((v) -> {

            accounts = Arrays.asList(binding.etInput.getText().toString().split("\n"));

//            for (String str : accounts) {
//                zhanghao.add(str);
//            }


            LogUtils.E(TAG, JSON.toJSONString(accounts));

            Toasty.info(this, "识别出" + accounts.size() + "个账号").show();

            PreferencesUtils.putString(this, "accounts", JSON.toJSONString(accounts));

            MAPP.mapp.setCurrentAccounts(accounts);

        });

        binding.btnClean.setOnClickListener((v) -> {
            PreferencesUtils.putString(this, "accounts", null);
            getLocalAccount();
        });
    }

    private void getLocalAccount() {
        zhanghaoStr = PreferencesUtils.getString(this, "accounts");

        accounts = JSON.parseArray(zhanghaoStr, String.class);

        if (accounts != null && accounts.size() > 0) {
            for (String str : accounts) {

                binding.etInput.append(str + "\n");

            }
        } else {
            binding.etInput.setText("");
            binding.etInput.setHint("来粘贴账号吧少年");
        }


    }
}
