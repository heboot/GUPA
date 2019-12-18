package com.ruguo.gupa.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


/**
 * Created by heboot on 2018/1/17.
 */

public abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity implements BaseUIInterface {

    protected String TAG = this.getClass().getName();

    protected T binding;

    protected QMUITipDialog tipDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        initUI();
        initData();
        initListener();
    }

    protected void initContentView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @LayoutRes
    protected abstract int getLayoutId();


}
