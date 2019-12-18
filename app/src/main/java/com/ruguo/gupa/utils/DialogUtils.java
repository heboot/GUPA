package com.ruguo.gupa.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * Created by heboot on 2018/2/7.
 */

public class DialogUtils {

    private static QMUITipDialog tipDialog;


    private static Observable timeObservable = Observable.timer(1000, TimeUnit.MILLISECONDS);

    private static Consumer consumer = (v) -> {
        if (tipDialog != null) {
            tipDialog.dismiss();
            tipDialog = null;
        }
    };


    private static List<Dialog> dialogs = new ArrayList<>();


    public static void addDialog2Stack(Dialog dialog) {
        if (dialog != null) {//&& dialogs.indexOf(dialog) == -1
            dialogs.add(dialog);
        }
    }

    public static void showDialogStack() {
        if (dialogs != null && dialogs.size() > 0) {
            dialogs.get(0).show();
            dialogs.get(0).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialogs.remove(0);
                    showDialogStack();
                }
            });
        }
    }


    /**
     * 构建一个带加载圈的弹出框
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
    public static QMUITipDialog getLoadingDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(text)
                .create();
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }

    /**
     * 构建一个提示失败的弹窗
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
    public static QMUITipDialog getFailDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(text)
                .create();
        tipDialog.setCancelable(true);
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }

    /**
     * 构建一个提示成功的弹窗
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
    public static QMUITipDialog getSuclDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(text)
                .create();
        tipDialog.setCancelable(true);
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }

    public static QMUITipDialog getInfolDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(text)
                .create();
        tipDialog.setCancelable(true);
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }


    public static QMUIBottomSheet getSexbottomSheet(Context context, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(context)
                .addItem("女")
                .addItem("男").setOnSheetItemClickListener(listener)
                .build();
        return qmuiBottomSheet;
    }

    public static QMUIBottomSheet getAvatarBottomSheet(Context context, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(context)
                .addItem("拍照")
                .addItem("相册").setOnSheetItemClickListener(listener)
                .build();
        return qmuiBottomSheet;
    }

    public static QMUIBottomSheet getLogoutBottomSheet(Context context, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(context)
                .addItem("换账号登录")
                .addItem("退出登录")
                .addItem("取消")
                .setOnSheetItemClickListener(listener)
                .build();
        return qmuiBottomSheet;
    }

    public static QMUIBottomSheet getNAVBottomSheet(Context context, List<String> text, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet.BottomListSheetBuilder qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(context);
        for (String s : text) {
            qmuiBottomSheet.addItem(s);
        }
        qmuiBottomSheet.setOnSheetItemClickListener(listener);
        return qmuiBottomSheet.build();
    }

//
//    /**
//     * 构建一个选择头像的选择器
//     *
//     * @param context
//     * @param text
//     * @param autoDismiss
//     * @return
//     */
//    public static BottomSheetDialog getAvatarBottomSheet(Context context, Consumer<Integer> consumer) {
//        List<String> list = new ArrayList<>();
//        list.add(context.getString(R.string.choose_pic_camera));
//        list.add(context.getString(R.string.choose_pic_album));
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog.Builder(context, consumer, list).create();
//        return bottomSheetDialog;
//    }
//
//    public static BottomSheetDialog getVideoOptionBottomSheet(Context context, Consumer<Integer> consumer) {
//        List<String> list = new ArrayList<>();
//        list.add("删除");
//        list.add("设为主视频");
//        list.add("设为收费视频");
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog.Builder(context, consumer, list).create();
//        return bottomSheetDialog;
//    }
//
//
//    public static BottomSheetDialog getHomepageBottomSheet(Context context, Consumer<Integer> consumer, boolean isBlack) {
//        List<String> list = new ArrayList<>();
//        list.add("举报");
//        list.add(isBlack ? "取消拉黑" : "拉黑");
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog.Builder(context, consumer, list).create();
//        return bottomSheetDialog;
//    }
//
//
//    /**
//     * 构建一个选择视频的选择器
//     *
//     * @param context
//     * @param text
//     * @param autoDismiss
//     * @return
//     */
//    public static BottomSheetDialog getAuthSkillBottomSheet(Context context, Consumer<Integer> consumer) {
//        List<String> list = new ArrayList<>();
//        list.add(context.getString(R.string.choose_video_camera));
//        list.add(context.getString(R.string.choose_video_album));
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog.Builder(context, consumer, list).create();
//        return bottomSheetDialog;
//    }
//
//    public static void showUpGoldVipDialog(Context context) {
//        ServiceTipDialog serviceTipDialog = new ServiceTipDialog.Builder(context, new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//
//            }
//        }, MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP, null, UserService.getInstance().getUser().getAvatar(), null, null).create();
////        serviceTipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
////            @Override
////            public void onDismiss(DialogInterface dialog) {
////
////            }
////        });
//        serviceTipDialog.show();
//    }

}
