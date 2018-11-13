package com.hx.zzp.activity.partner;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bg.baseutillib.base.BaseRequestDao;
import com.bg.baseutillib.view.TitleBarView;
import com.hx.zzp.MyApplication;
import com.hx.zzp.R;
import com.hx.zzp.RvBaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付成功
 */
public class PaySuccessActivity  extends RvBaseActivity {

    @BindView(R.id.tv_vip_partner)
    TextView tvVipLevel;

    @BindView(R.id.tv_vip_amount)
    TextView tvAmount;

    @BindView(R.id.iv_pay_logo)
    ImageView ivPayLogo;

    private String amount;
    private String vipLevel;
    private String type;


    @Override
    public int setLayoutResID() {
        return R.layout.activity_pay_success;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        MyApplication.getInstance().addEnterActivity(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (getIntent().getSerializableExtra("amount") != null) {
            amount = getIntent().getStringExtra("amount");
        }
        if (getIntent().getSerializableExtra("vipLevel") != null) {
            vipLevel = getIntent().getStringExtra("vipLevel");
        }
        if (getIntent().getSerializableExtra("type") != null) {
            type = getIntent().getStringExtra("type");
        }
        tvVipLevel.setText(vipLevel+"合伙人");
        tvAmount.setText(getString(R.string.util_money)+amount);

        if("2".equals(type)){//微信支付
            ivPayLogo.setImageResource(R.mipmap.ic_pay_wx);
        }else {
            ivPayLogo.setImageResource(R.mipmap.ic_pay_alpay);
        }
    }
    @OnClick({R.id.re_complete,R.id.re_title_left_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_complete:
                MyApplication.getInstance().exitEnterAllActivity();
                startActivity(UpgradeAgentActivity.class);
                finish();
                break;
            case R.id.re_title_left_icon:
                MyApplication.getInstance().exitEnterAllActivity();
                startActivity(UpgradeAgentActivity.class);
                finish();
                break;
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
