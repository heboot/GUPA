package com.ruguo.gupa.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.ruguo.gupa.R;
import com.ruguo.gupa.databinding.DialogProgressBinding;
import com.ruguo.gupa.utils.DownloadUtils;
import com.ruguo.gupa.utils.LogUtils;
import com.ruguo.gupa.utils.SDCardUtils;

/**
 * Created by heboot on 2018/2/2.
 */

@SuppressLint("ValidFragment")
public class ProgressDialog extends DialogFragment {

    private DialogProgressBinding binding;

    private String url, name;


    public ProgressDialog(String url, String name) {
        this.url = url;
        this.name = name;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.payTypeDialogStyle);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        //设置 dialog 的背景为 null
//        getDialog().getWindow().setBackgroundDrawable(null);


        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;

        window.setAttributes(windowParams);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_progress, null, false);

        initView();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; //底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


        return binding.getRoot();
    }


    private void initView() {
        FileDownloader.setup(getContext());
        FileDownloader.getImpl().create(url).setPath(SDCardUtils.getRootPathFilePath(), true)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        binding.qpbProgress.setMaxValue(totalBytes);
                        binding.qpbProgress.setProgress(soFarBytes);
                    }


                    @Override
                    protected void completed(BaseDownloadTask task) {
                        dismiss();
                        LogUtils.E("下载完成", task.getTargetFilePath() + ">>" + task.getFilename() + "url:" + url);
                        DownloadUtils.installAPP(getActivity(), task.getTargetFilePath());
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        dismiss();
                        LogUtils.E("下载出错", JSON.toJSONString(e));
                    }

                }).start();

    }


}
