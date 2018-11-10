package com.hx.zzp.activity.partner;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import com.bg.baseutillib.base.BaseRequestDao;
import com.bg.baseutillib.view.TitleBarView;
import com.hx.zzp.R;
import com.hx.zzp.RvBaseActivity;

import butterknife.OnClick;

public class ExplainActivity extends RvBaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_explain;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public BaseRequestDao onCreateRequestData() {
        return null;
    }

    @Override
    public void setPaddingTop(TitleBarView titleBar) {
        super.setPaddingTop(titleBar);
    }
    @OnClick(R.id.re_title_left_icon)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_title_left_icon:
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
