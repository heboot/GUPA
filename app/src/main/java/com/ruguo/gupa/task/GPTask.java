package com.ruguo.gupa.task;

import android.content.Intent;

import com.ruguo.gupa.MAPP;
import com.ruguo.gupa.data.MMessageWhat;
import com.ruguo.gupa.service.MHandler;
import com.ruguo.gupa.utils.LocalDataUtils;
import com.ruguo.gupa.utils.ToastUtils;

public class GPTask {

    private static MHandler handler = new MHandler();

    public static void startDZH(){
        Intent intent = MAPP.mapp.getPackageManager().getLaunchIntentForPackage("com.android.dazhihui");
        if (intent != null) {
            intent.putExtra("type", "110");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MAPP.mapp.startActivity(intent);
            handler.repatCount = 0;
            ToastUtils.ToastSuccess("准备去找慧信页面");
            handler.sendEmptyMessageDelayed(MMessageWhat.CLICK_HUIXIN_BTN, LocalDataUtils.getJGTime());
        }
    }

}
