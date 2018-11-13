package com.hx.zzp.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.bg.baseutillib.net.RxNetCallback;
import com.bg.baseutillib.net.exception.ApiException;
import com.bg.baseutillib.tool.SystemUtils;
import com.bg.baseutillib.tool.ToastUtil;
import com.bg.baseutillib.view.VerificationCodeView;
import com.hx.zzp.R;
import com.hx.zzp.RvBaseActivity;
import com.hx.zzp.activity.partner.PartnerStatementActivity;
import com.hx.zzp.activity.partner.UpgradeAgentActivity;
import com.hx.zzp.event.LoginEvent;
import com.hx.zzp.net.login.LoginDao;
import com.hx.zzp.net.login.request.RegisterBody;
import com.hx.zzp.net.login.response.CodeBean;
import com.hx.zzp.net.login.response.SessionBean;
import com.hx.zzp.utils.AppConfig;
import com.hx.zzp.utils.AppUserData;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 重置密码
 */
public class ResetPasswordActivity extends RvBaseActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.ivCleanPhone)
    ImageView ivCleanPhone;
    @BindView(R.id.ivCleanPass1)
    ImageView ivCleanPass1;
    @BindView(R.id.ivCleanPass2)
    ImageView ivCleanPass2;

    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.VCodeView)
    VerificationCodeView VCodeView;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordAgain)
    EditText etPasswordAgain;

    private String phone;
    @Override
    public int setLayoutResID() {
        return R.layout.login_activity_reset_password;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (getIntent().getSerializableExtra("phone") != null) {
            phone = getIntent().getStringExtra("phone");
        }
        etPhone.setText(phone);
        etPhone.setFocusable(true);
        etPhone.setFocusableInTouchMode(true);
        etPhone.requestFocus();
        ivCleanPhone.setVisibility(View.VISIBLE);
        //setPaddingTop(titleBar);
    }

    @Override
    public void initListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = etPhone.getText().toString();
                if (phone.length() > 0) {
                    ivCleanPhone.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPhone.setVisibility(View.GONE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = etPassword.getText().toString();
                if (password.length() > 0) {
                    ivCleanPass1.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPass1.setVisibility(View.GONE);
                }
            }
        });

        etPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String passwordAgain = etPasswordAgain.getText().toString();
                if (passwordAgain.length() > 0) {
                    ivCleanPass2.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPass2.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public LoginDao onCreateRequestData() {
        return new LoginDao();
    }

    @OnClick({R.id.ivCleanPhone, R.id.VCodeView, R.id.btnReset,R.id.re_title_left_icon,R.id.ivCleanPass1,R.id.ivCleanPass2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivCleanPhone://
                etPhone.setText("");
                break;
            case R.id.VCodeView://获取
                checkRegister();
                break;
            case R.id.btnReset://重置密码
                resetPassword();
                break;
            case R.id.re_title_left_icon:
                finish();
                break;
            case R.id.ivCleanPass1://
                etPassword.setText("");
                break;
            case R.id.ivCleanPass2://
                etPasswordAgain.setText("");
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String moblie = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(moblie)) {
            ToastUtil.showShortToast("请输入手机号");
            return;
        }

//        CodeBody body = new CodeBody();
//        body.setMobile(moblie);
//        body.setMobileCodeType("user_resetpassword");
        ((LoginDao)createRequestData).findMobileCode(this,moblie, new RxNetCallback<CodeBean>() {
            @Override
            public void onSuccess(CodeBean s) {
                VCodeView.startCountdown(60);
                ToastUtil.showShortToast("验证码获取成功");
                if (SystemUtils.isApkInDebug(mContext)) {
                    etCode.setText(s.getBussData());
                }
            }

            @Override
            public void onError(ApiException e) {
                ToastUtil.showShortToast(e.getMessage());
            }
        });
    }

    /**
     * 重置密码
     */
    private void resetPassword() {
        final String phone = etPhone.getText().toString().trim();
        final String pwd = etPassword.getText().toString().trim();
        final String pwdAgain = etPasswordAgain.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("请输入手机号");
            return;
        }
        if (!AppConfig.isMobileNO(phone) && !SystemUtils.isApkInDebug(mContext)) {
            ToastUtil.showShortToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShortToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
            ToastUtil.showShortToast("请输入密码");
            return;
        }
        if (pwd.length() < 6 || pwdAgain.length() < 6) {
            ToastUtil.showShortToast("请输入6-18位密码");
            return;
        }
        if (!pwd.trim().equals(pwdAgain.trim())) {
            ToastUtil.showShortToast(getString(R.string.pwd_validate));
            return  ;
        }
        RegisterBody body = new RegisterBody();
        body.setMobile(phone);
        body.setPassword(pwd);
        body.setValidCode(code);
        ((LoginDao)createRequestData).resetPassword(mContext, body, new RxNetCallback<SessionBean>() {
            @Override
            public void onSuccess(SessionBean userBean) {
                if (userBean != null) {
                    ToastUtil.showShortToast("密码重置成功");
                    AppUserData.getInstance().setMobile(phone);
//                    AppUserData.getInstance().setSessionId(userBean.getBussData());
                    AppUserData.getInstance().setMobile(phone);
                    AppUserData.getInstance().setPassWord(pwd);
                    AppUserData.getInstance().setIsLogin(true);
                    EventBus.getDefault().post(new LoginEvent(true));
                    if(AppUserData.getInstance().getUserBean().isPartner){//是合伙人
                        startActivity(UpgradeAgentActivity.class);
                    }else {
                        startActivity(PartnerStatementActivity.class);
                    }
                    finish();
                }
            }

            @Override
            public void onError(ApiException e) {
                ToastUtil.showShortToast(e.getMessage());
            }
        });
    }

    /**
     * 校验是否注册
     */
    private void checkRegister(){
         String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("请输入手机号");
            return;
        }
        if (!AppConfig.isMobileNO(phone) && !SystemUtils.isApkInDebug(mContext)) {
            ToastUtil.showShortToast("请输入正确的手机号");
            return;
        }
        getCode();
//        ((LoginDao)createRequestData).checkRegister(mContext, phone, new RxNetCallback<CommonNetBean>() {
//            @Override
//            public void onSuccess(CommonNetBean commonNetBean) {
//                if (commonNetBean != null) {
////                    ToastUtil.showShortToast("手机号已被注册");
//                    getCode();
//                }
//            }
//
//            @Override
//            public void onError(ApiException e) {
////                if (e.getCode() == 500) {
////                    getCode();
////                } else {
//                    ToastUtil.showShortToast(e.getMessage());
////                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (VCodeView != null) {
            VCodeView.stopCountdown();
        }
    }
}
