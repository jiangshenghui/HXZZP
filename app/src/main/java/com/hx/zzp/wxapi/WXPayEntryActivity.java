package com.hx.zzp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.hx.zzp.net.ApiManager;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
/**
 * 微信回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_withdrawals_success);
		api = WXAPIFactory.createWXAPI(this, ApiManager.APP_ID);
		api.handleIntent(getIntent(), this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {

//		String amount = ResponseCache.getDataString(this, Constants.WXPAY,"amount","");
//		int payType = ResponseCache.getDataInt(this, Constants.WXPAY,"payType",-1);
//		String orderId  = ResponseCache.getDataString(this, Constants.WXPAY,"orderId","");
//		LogUtil.d("jsh","errCode1:"+resp.errCode);
//		if (resp.errCode ==0 ) {
//			if(payType == Constants.PAY_TYPE_RECHARGE){//微信充值
//				Intent intent = new Intent(this, RechargeDetailActivity.class);
//				intent.putExtra("payType",2);
//				intent.putExtra("amount",amount);
//				startActivity(intent);
//			}else if(payType==Constants.PAY_TYPE_OFFER ){//悬赏成功
//				ToastMakeUtils.showToast(this, getString(R.string.pay_success));
//				Intent intent = new Intent(this, MyAdviceActivity.class);
//				startActivity(intent);
//			}else if(payType==Constants.PAY_TYPE_PROJECT ){//发布项目
//				Intent intent = new Intent(this,TrusteeshipActivity.class);
//				intent.putExtra("orderId",orderId);
//				startActivity(intent);
//			}else if(payType==Constants.PAY_TYPE_BOND ){//缴纳保证金
//				ToastMakeUtils.showToast(this, "缴纳保证金成功");
//				ZZWApplication.getInstance().exitEnterAllActivity();
//				Intent intent = new Intent();
//				intent.setAction("recharege.update");
//				sendBroadcast(intent);
//			}else if(payType == PAY_TRADE_TYPE_PROJECT_COMPLETE){
//				Intent intent = new Intent();
//				intent.setAction("mybid.update");
//				sendBroadcast(intent);
//
//				intent = new Intent();
//				intent.setAction("mywork.update");
//				sendBroadcast(intent);
//				ZZWApplication.getInstance().exitEnterAllActivity();
//			}else if(Constants.PAY_TYPE_PROJECT==payType){//增加托管金额
//				ToastMakeUtils.showToast(this,"增加托管金额成功");
//				Intent intent = new Intent();
//				intent.setAction("mybid.update");
//				sendBroadcast(intent);
//			}else if(Constants.PAY_TYPE_PHONE == payType|| Constants.PAY_TYPE_VIDEO == payType){  //语音支付
//				LoggerUtil.i("WeChat支付成功");
//				EventBusUtil.sendEvent(new EventBean(EventContants.EventCode.WECHAT_PAY));
//			}else if(Constants.PAY_TYPE_NINE == payType){  //立即支付
//				LoggerUtil.i("WeChat立即支付成功");
//				EventBusUtil.sendEvent(new EventBean(EventContants.EventCode.PAY_TYPE_NINE));
//			}else if(Constants.PAY_TYPE_RED == payType){
//				LoggerUtil.i("WeChat发红包支付成功");
//				EventBusUtil.sendEvent(new EventBean(EventContants.EventCode.PAY_TYPE_RED));
//			}
//		}else if(resp.errCode ==-2){
//			ToastMakeUtils.showToast(this,getString(R.string.action_cancel));
//		}
		finish();
	}
}