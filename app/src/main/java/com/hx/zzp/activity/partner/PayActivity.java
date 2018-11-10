package com.hx.zzp.activity.partner;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bg.baseutillib.net.RxNetCallback;
import com.bg.baseutillib.net.exception.ApiException;
import com.bg.baseutillib.tool.ToastUtil;
import com.bg.baseutillib.view.TitleBarView;
import com.hx.zzp.MyApplication;
import com.hx.zzp.R;
import com.hx.zzp.RvBaseActivity;
import com.hx.zzp.net.ApiManager;
import com.hx.zzp.net.pay.PayDao;
import com.hx.zzp.net.pay.response.PreOrderBean;
import com.pay.AliPay;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import butterknife.BindView;
import butterknife.OnClick;
/***
 * 支付界面
 */
public class PayActivity  extends RvBaseActivity {

    @BindView(R.id.tv_vip_level)
    TextView tvVipLevel;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.iv_ce_wx)
    ImageView ivWx;

    @BindView(R.id.iv_alpay)
    ImageView ivAlpay;

    private String username;
    private String idCard;
    private String amount;
    private String vipLevel;

    private String payType = "1";//0 余额 1 支付宝 2 微信

    @Override
    public int setLayoutResID() {
        return R.layout.activity_pay;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        MyApplication.getInstance().addEnterActivity(this);
        msgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        msgApi.registerApp(ApiManager.APP_ID);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (getIntent().getSerializableExtra("username") != null) {
            username = getIntent().getStringExtra("username");
        }
        if (getIntent().getSerializableExtra("idcard") != null) {
            idCard = getIntent().getStringExtra("idcard");
        }
        if (getIntent().getSerializableExtra("amount") != null) {
            amount = getIntent().getStringExtra("amount");
        }
        if (getIntent().getSerializableExtra("vipLevel") != null) {
            vipLevel = getIntent().getStringExtra("vipLevel");
        }
        tvVipLevel.setText(vipLevel+"合伙人");
        tvAmount.setText(getString(R.string.util_money)+amount);

    }
    private void setPayPage(){
        ivWx.setImageResource(R.mipmap.icon_image_un_grey_select);
        ivAlpay.setImageResource(R.mipmap.icon_image_un_grey_select);
    }
    @Override
    public void initListener() {

    }

    @Override
    public PayDao onCreateRequestData() {
        return new PayDao();
    }

    @Override
    public void setPaddingTop(TitleBarView titleBar) {
        super.setPaddingTop(titleBar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    IWXAPI msgApi;
    @OnClick({R.id.btn_sure_pay,R.id.re_alpay,R.id.re_wx,R.id.re_title_left_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure_pay://确认支付
                String paymentType = "";
                if("1".equals(payType)){
                    paymentType = "alipay";// weixin,alipay
                }else {
                    paymentType = "weixin";
                }
                ((PayDao)createRequestData).payPre(this, vipLevel, paymentType,amount, new RxNetCallback<PreOrderBean>() {
                    @Override
                    public void onSuccess(final PreOrderBean result) {
                        if (result != null) {//预下单成功
                            if(result != null){
                                 if("1".equals(payType)){//支付宝
                                    if(result.data != null){
                                        Log.d("jsh","data:"+result.data);
                                        PreOrderBean.AliPay orderInfo = result.data;
                                        Activity activity = PayActivity.this;
                                        AliPay payDemoActivity = new AliPay(activity, new AliPay.AliyPayCallback() {
                                            @Override
                                            public void onReponse(String resultInfo, String resultStatus) {
                                                // 判断resultStatus 为9000则代表支付成功
                                                if (TextUtils.equals(resultStatus, "9000")) {
                                                    String alipayresult = resultInfo.toString();
                                                    successStartActivity(result);
                                                }else if (TextUtils.equals(resultStatus, "6001")) {//支付取消
                                                }else {
                                                    ToastUtil.showShortToast( "支付失败");
                                                }
                                            }
                                        });
                                        payDemoActivity.aliPay(orderInfo.alipay );
                                    }
                                }else if("2".equals(payType)){//微信
                                    if (result !=null) {
                                        try {
                                            PayReq req = new PayReq();
                                            req.appId = result.data.appid;
                                            req.partnerId =  result.data.partnerid;
                                            req.prepayId = result.data.prepayid;
                                            req.nonceStr = result.data.noncestr;
                                            req.timeStamp = result.data.timestamp;
                                            req.packageValue = result.data.package1;
                                            req.sign = result.data.sign;
                                            req.extData = "app data"; // optional
                                            msgApi.sendReq(req);
                                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                                            ResponseCache.saveData(this, Constants.WXPAY,"amount",depositPrice);
//                                            ResponseCache.saveData(this, Constants.WXPAY,"payType",PAY_TYPE_PROJECT);
//                                            ResponseCache.saveData(this, Constants.WXPAY,"orderId",orderId);
                                        }catch(Exception e){

                                        }
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showShortToast(e.getMessage());
                    }
                });
                break;
            case R.id.re_wx://微信
                setPayPage();
                ivWx.setImageResource(R.mipmap.icon_image_select);
                payType = "2";
                break;
            case R.id.re_alpay://支付宝
                payType = "1";
                setPayPage();
                ivAlpay.setImageResource(R.mipmap.icon_image_select);
                break;
            case R.id.re_title_left_icon:
                finish();
                break;
        }
    }

    private void successStartActivity(PreOrderBean result){
        if(result != null){
            Bundle bundle = new Bundle();
            bundle.putString("amount",result.data.price);
            bundle.putString("vipLevel",vipLevel);
            startActivity(PaySuccessActivity.class,bundle);
            finish();
        }
    }
}
