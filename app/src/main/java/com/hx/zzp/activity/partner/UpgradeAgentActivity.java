package com.hx.zzp.activity.partner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bg.baseutillib.net.RxNetCallback;
import com.bg.baseutillib.net.exception.ApiException;
import com.bg.baseutillib.tool.ToastUtil;
import com.hx.zzp.R;
import com.hx.zzp.RvBaseActivity;
import com.hx.zzp.dialog.SelectDialog;
import com.hx.zzp.net.login.LoginDao;
import com.hx.zzp.net.login.response.UserBean;
import com.hx.zzp.utils.AppUserData;
import com.hx.zzp.utils.QRCodeUtil;
import butterknife.BindView;
import butterknife.OnClick;

/***
 * 升级代理人
 */
public class UpgradeAgentActivity extends RvBaseActivity implements SelectDialog.DialogButtonClickListener{

    @BindView(R.id.iv_qr)
    ImageView ivQr;

    @BindView(R.id.tv_profit)
    TextView tvProfit;

    @BindView(R.id.tv_user_num)
    TextView tvUserNum;

    @BindView(R.id.tv_parner)
    TextView tvParner;

    @BindView(R.id.tv_invitation_codev)
    TextView tvInvitationCodev;

    private String inviteUrl;
    private String level;
    private String inviteCode;
    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_upgrade_agent;
    }
    @OnClick({R.id.btn_upgrade,R.id.re_title_left_icon,R.id.re_title_right_icon,R.id.re_invitation_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_upgrade://升级代理人
                if(!TextUtils.isEmpty(level)&&"10".equals(level)){
                    ToastUtil.showShortToast("您已经是最高级VIP！");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("level",level);
                startActivity(UpgradeAgentActivity1.class,bundle);
                break;
            case R.id.re_title_left_icon:
                startActivity(ExplainActivity.class);
                break;
            case R.id.re_title_right_icon:
                SelectDialog dlg = new SelectDialog(mContext, 0, inviteUrl,
                       "","");
                dlg.showDialog((SelectDialog.DialogButtonClickListener) mContext);
                break;
            case R.id.re_invitation_copy:
                ClipboardManager mClipboardManager = (ClipboardManager)  mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("copy from 赚赚拍",inviteCode);
                mClipboardManager.setPrimaryClip(clipData);
                ToastUtil.showShortToast("复制成功");
                break;
        }
    }
    @Override
    public void initData(Bundle savedInstanceState) {


    }

    @Override
    public void initListener() {

    }

    @Override
    public LoginDao onCreateRequestData() {
        return new LoginDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("jsh","onResume");
        ((LoginDao)createRequestData).findDetail(this, new RxNetCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {
                if (userBean != null) {//
                    userBean.mobile =   AppUserData.getInstance().getUserBean().getMobile();
                    AppUserData.getInstance().setUserBean(userBean);
                    AppUserData.getInstance().setIsLogin(true);
                    if(!TextUtils.isEmpty(userBean.invitCount)){
                        tvUserNum.setText(userBean.invitCount+"人");
                    }
                    if(userBean.income != null && !TextUtils.isEmpty(userBean.invitCount)){
                        tvProfit.setText(userBean.income.money+"元");
                    }
                    if(!TextUtils.isEmpty(userBean.level)){
                        tvParner.setText("VIP"+userBean.level+"合伙人");
                    }
                    inviteUrl = userBean.inviteUrl;
                    inviteCode =  userBean.inviteCode;
                    if(!TextUtils.isEmpty(inviteCode)){
                        tvInvitationCodev.setText(inviteCode);
                    }
                    level = userBean.level;
//                    generateQrCode(inviteUrl);
                }
            }
            @Override
            public void onError(ApiException e) {
                ToastUtil.showShortToast(e.getMessage());
            }
        });
    }

    private void generateQrCode(String dataStr) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        double percent = 0.592;
        int width = (int) (wm.getDefaultDisplay().getWidth() * percent);
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(dataStr, width, width);
        ivQr.setImageBitmap(mBitmap);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSelectDialogButtonClick(int requestCode, int resultCode) {
        
    }
}
