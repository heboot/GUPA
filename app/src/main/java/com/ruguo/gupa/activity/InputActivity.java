package com.ruguo.gupa.activity;

import com.alibaba.fastjson.JSON;
import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.R;
import com.ruguo.gupa.base.BaseActivity;
import com.ruguo.gupa.data.ConfigModel;
import com.ruguo.gupa.data.MDataModel;
import com.ruguo.gupa.databinding.ActivityInputMsgBinding;
import com.ruguo.gupa.utils.LocalDataUtils;
import com.ruguo.gupa.utils.LogUtils;
import com.ruguo.gupa.utils.PreferencesUtils;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class InputActivity extends BaseActivity<ActivityInputMsgBinding> {

    private List<String> accounts;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_msg;
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {
        getLocalAccount();
    }

    @Override
    public void initListener() {
        binding.btnInput.setOnClickListener((v) -> {

            accounts = Arrays.asList(binding.etInput.getText().toString().split("\n"));

//            for (String str : accounts) {
//                zhanghao.add(str);
//            }



            LogUtils.E(TAG, JSON.toJSONString(accounts));

            Toasty.info(this, "识别出" + accounts.size() + "个话术").show();

            LocalDataUtils.setCommentContent(JSON.toJSONString(accounts));


        });


    }

    private void getLocalAccount() {

        MDataModel mDataModel = (MDataModel)PreferencesUtils.getObj(MAPP.mapp, "mdatamodel");

        if(mDataModel != null && mDataModel.getCommentContentList() != null && mDataModel.getCommentContentList().size() > 0){
            accounts = mDataModel.getCommentContentList();
        }

        if (accounts != null && accounts.size() > 0) {
            for (String str : accounts) {

                binding.etInput.append(str + "\n");

            }
        } else {
            binding.etInput.setText("");
            binding.etInput.setHint("来填写评论话术吧少年");
        }


    }

}
