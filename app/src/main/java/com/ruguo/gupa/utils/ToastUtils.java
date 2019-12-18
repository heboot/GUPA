package com.ruguo.gupa.utils;

import com.ruguo.gupa.MAPP;

import es.dmoral.toasty.Toasty;

public class ToastUtils {


    public static void ToastSuccess(String msg){
        Toasty.success(MAPP.mapp,msg,1000).show();
    }
    public static void ToastError(String msg){
        Toasty.error(MAPP.mapp,msg,1000).show();
    }
}
