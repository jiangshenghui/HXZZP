package com.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.alipay.sdk.app.PayTask;
import java.util.Map;

/**
 *  重要说明:
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class AliPay {

	private static final int SDK_PAY_FLAG = 1;

	private Context context;
	private  AliyPayCallback aliyPayCallback;
	public AliPay(Context context, AliyPayCallback aliyPayCallback){
		this.context = context;
		this.aliyPayCallback = aliyPayCallback;
	}

	public interface AliyPayCallback{
		public void onReponse(String resultInfo, String resultStatus);
	}
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				aliyPayCallback.onReponse(resultInfo,resultStatus);
				break;
			}
			}
		};
	};

	/**
	 * 支付宝支付业务
	 *
	 */
	public void aliPay(final String orderInfo) {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				Activity activity = (Activity)context;
				PayTask alipay = new PayTask(activity);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
}
