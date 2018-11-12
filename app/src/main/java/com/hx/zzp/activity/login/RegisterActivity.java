package com.hx.zzp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.bg.baseutillib.net.CommonNetBean;
import com.bg.baseutillib.net.RxNetCallback;
import com.bg.baseutillib.net.exception.ApiException;
import com.bg.baseutillib.tool.SystemUtils;
import com.bg.baseutillib.tool.ToastUtil;
import com.bg.baseutillib.view.TitleBarView;
import com.bg.baseutillib.view.VerificationCodeView;
import com.hx.zzp.R;
import com.hx.zzp.RvBaseActivity;
import com.hx.zzp.activity.partner.PartnerStatementActivity;
import com.hx.zzp.activity.partner.UpgradeAgentActivity;
import com.hx.zzp.net.login.LoginDao;
import com.hx.zzp.net.login.request.CodeBody;
import com.hx.zzp.net.login.request.RegisterBody;
import com.hx.zzp.net.login.response.CodeBean;
import com.hx.zzp.net.login.response.SessionBean;
import com.hx.zzp.utils.AppConfig;
import com.hx.zzp.utils.AppUserData;
import com.hx.zzp.utils.StringUtils;
import com.hx.zzp.widget.AddSpaceTextWatcher;

import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends RvBaseActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.ivCleanPhone)
    ImageView ivCleanPhone;
    @BindView(R.id.ivCleanIdCard)
    ImageView ivCleanIdCard;
    @BindView(R.id.ivCleanIdInvitCode)
    ImageView ivCleanInvitCode;

    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.VCodeView)
    VerificationCodeView VCodeView;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.etInvitCode)
    EditText etInvitCode;
    @BindView(R.id.tbLook)
    ToggleButton tbLook;
//    @BindView(R.id.ckbAgree)
//    CheckBox ckbAgree;

    private AddSpaceTextWatcher[] asEditTexts=new AddSpaceTextWatcher[3];

    private EditText[] editTexts=new EditText[3];
    @Override
    public int setLayoutResID() {
        return R.layout.activity_register;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //setPaddingTop(titleBar);
        editTexts[2]= etIdCard;//身份证
        asEditTexts[2]=new AddSpaceTextWatcher(editTexts[2],21);
        asEditTexts[2].setSpaceType(AddSpaceTextWatcher.SpaceType.IDCardNumberType);
    }

    @Override
    public void initListener() {
        tbLook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPassword.setSelection(etPassword.length());
            }
        });
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
        etIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String idCard = etIdCard.getText().toString();
                if (idCard.length() > 0) {
                    ivCleanIdCard.setVisibility(View.VISIBLE);
                } else {
                    ivCleanIdCard.setVisibility(View.GONE);
                }
            }
        });
        etInvitCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String invitCode = etInvitCode.getText().toString();
                if (invitCode.length() > 0) {
                    ivCleanInvitCode.setVisibility(View.VISIBLE);
                } else {
                    ivCleanInvitCode.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public LoginDao onCreateRequestData() {
        return new LoginDao();
    }

    @OnClick({R.id.ivCleanPhone, R.id.VCodeView, R.id.btnRegister,
            R.id.tvToLogin,R.id.re_title_left_icon, R.id.ivCleanIdCard, R.id.ivCleanIdInvitCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivCleanPhone:
                etPhone.setText("");
                break;
            case R.id.ivCleanIdCard:
                etIdCard.setText("");
                break;
            case R.id.ivCleanIdInvitCode:
                etInvitCode.setText("");
                break;
            case R.id.VCodeView://获取
                getCode();
//                checkRegister();
                break;
            case R.id.btnRegister://注册
                register();
                break;
            case R.id.tvToLogin://已注册去登录
                finish();
                break;
            case R.id.re_title_left_icon:
                finish();
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

        CodeBody body = new CodeBody();
        body.setMobile(moblie);
        body.setMobileCodeType("user_reigter");
        ((LoginDao)createRequestData).findMobileCode(this, body.getMobile(), new RxNetCallback<CodeBean>() {
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
     * 注册
     */
    private void register() {
        final String phone = etPhone.getText().toString().trim();
        final String pwd = etPassword.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String idCard = StringUtils.remove(etIdCard.getText().toString());
        String invitCode = etInvitCode.getText().toString().trim();

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
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showShortToast("请输入密码");
            return;
        }
        if (pwd.length() < 6) {
            ToastUtil.showShortToast("请输入6-18位密码");
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            ToastUtil.showShortToast("请输入身份证号");
            return;
        }
        if (TextUtils.isEmpty(invitCode)) {
            ToastUtil.showShortToast("请输入邀请码");
            return;
        }
        /*if (!ckbAgree.isChecked()) {
            ToastUtil.showShortToast("请阅读并同意《驴道租车用户协议》");
            return;
        }*/
        RegisterBody body = new RegisterBody();
        body.setMobile(phone);
        body.setPassword(pwd);
        body.setValidCode(code);
        body.inviteCode = invitCode;
        body.idCard = idCard;
        ((LoginDao)createRequestData).register(this, body, new RxNetCallback<SessionBean>() {
            @Override
            public void onSuccess(SessionBean commonNetBean) {
                ToastUtil.showShortToast("注册成功");
//                AppUserData.getInstance().setSessionId(commonNetBean.getBussData());
                AppUserData.getInstance().setMobile(phone);
                AppUserData.getInstance().setPassWord(pwd);
                AppUserData.getInstance().setIsLogin(true);
//                EventBus.getDefault().post(new LoginEvent(true));
                if(AppUserData.getInstance().getUserBean().isPartner){//是合伙人
                    startActivity(UpgradeAgentActivity.class);
                }else {
                    startActivity(PartnerStatementActivity.class);
                }
//                setResult(125, new Intent());
                finish();
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
    private void checkRegister() {
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("请输入手机号");
            return;
        }
        if (!AppConfig.isMobileNO(phone) && !SystemUtils.isApkInDebug(mContext)) {
            ToastUtil.showShortToast("请输入正确的手机号");
            return;
        }
        ((LoginDao)createRequestData).checkRegister(mContext, phone, new RxNetCallback<CommonNetBean>() {
            @Override
            public void onSuccess(CommonNetBean commonNetBean) {
                if (commonNetBean != null) {
                    ToastUtil.showShortToast("手机号已被注册");
                }
            }

            @Override
            public void onError(ApiException e) {
                if (e.getCode() == 500) {
                    getCode();
                } else {
                    ToastUtil.showShortToast(e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (VCodeView != null) {
            VCodeView.stopCountdown();
        }
    }
}
