package com.ruguo.gupa.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.data.AccountModel;
import com.ruguo.gupa.data.MDataModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class LocalDataUtils {


    public static void setCommentContent(String commentContent) {
        MDataModel douYinConfigModel = (MDataModel) PreferencesUtils.getObj(MAPP.mapp, "mdatamodel");
        if (douYinConfigModel == null) {
            douYinConfigModel = new MDataModel();

        }
        douYinConfigModel.setCommentContentList(JSONArray.parseArray(commentContent, String.class));
        MAPP.mapp.setDataModel(douYinConfigModel);
        PreferencesUtils.saveObj(MAPP.mapp,"mdatamodel",douYinConfigModel);
        Toasty.success(MAPP.mapp, "更新评论话术成功").show();
    }


    public static int getSendCount() {
        return PreferencesUtils.getInt(MAPP.mapp, "count");
    }

    public static int getScrollCount(){
        return rand.nextInt(6);
    }

    public static int getMaxRepatCount() {
        return 3;
    }

    public static int getJGTime(){
        int time = PreferencesUtils.getInt(MAPP.mapp, "jgTime");
        if(time == 0){
            time = 7000;
        }
        return time;
    }


    /**
     * 获取当前账号
     *
     * @param index
     * @return
     */
    public static AccountModel getCurrentAccount(int index) {
        AccountModel account = new AccountModel();
        if (index >= MAPP.mapp.getCurrentAccounts().size()) {
            account.setAccount(MAPP.mapp.getCurrentAccounts().get(0).split(" ")[0]);
            account.setPwd(MAPP.mapp.getCurrentAccounts().get(0).split(" ")[1]);
        }else{
            account.setAccount(MAPP.mapp.getCurrentAccounts().get(index).split(" ")[0]);
            account.setPwd(MAPP.mapp.getCurrentAccounts().get(index).split(" ")[1]);
        }
        LogUtils.E("获取到的切换账号:", JSON.toJSONString(account));
        return account;
    }


    private static Random rand = new Random();

    /**
     * 随机一条视频评论
     */
    public static String getRandomComment() {
        if (MAPP.mapp.getDataModel().getCommentContentList() == null || MAPP.mapp.getDataModel().getCommentContentList().size() == 0) {
            return "hi~";
        } else if (MAPP.mapp.getDataModel().getCommentContentList().size() == 1) {
            return MAPP.mapp.getDataModel().getCommentContentList().get(0);
        } else {
// randNumber 将被赋值为一个 MIN 和 MAX 范围内的随机数
            int randNumber = rand.nextInt(MAPP.mapp.getDataModel().getCommentContentList().size());
            return MAPP.mapp.getDataModel().getCommentContentList().get(randNumber);
        }
    }

}
