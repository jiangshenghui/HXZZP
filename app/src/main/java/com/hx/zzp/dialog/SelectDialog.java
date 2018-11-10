package com.hx.zzp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bg.baseutillib.tool.ToastUtil;
import com.hx.zzp.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by xuweihua on 2016/12/8.
 * 选择对话框，可以自定义3个选项信息
 */

public class SelectDialog implements View.OnClickListener {
    private Context mContext;
    private int mReqCode ;
    private String recommendUrl = null;
    private Dialog dlg = null;
    private UMWeb web;
    private UMShareListener mShareListener;

    private String title,desc;
    public SelectDialog(Context context, int requestCode, String recommendUrl, String option2, String cancelString){
        this.mContext = context;
        this.mReqCode = requestCode;
        this.recommendUrl = recommendUrl ;
        mShareListener = new CustomShareListener((Activity) mContext);
        web = new UMWeb(recommendUrl);
//        if(Constants.ENGINEERROLE.equals(role)){//工程师
//            title = getString(R.string.share_title_business);
//            desc = getString(R.string.share_text_business);
//        }else {
//            title = getString(R.string.share_title_wx);
//            desc = getString(R.string.share_text_member);
//        }
        title = "赚赚拍";;
        desc = "赚赚拍";
        web.setTitle(title);
        web.setDescription(desc);
//        if(ZZWApplication.mUserInfo != null && !TextUtils.isEmpty(ZZWApplication.mUserInfo.imageUrl)){
//            web.setThumb(new UMImage((Activity) mContext, getBitmap(ZZWApplication.mUserInfo.imageUrl)));
//        }else {
//            web.setThumb(new UMImage((Activity) mContext, R.mipmap.ic_share));
//        }
        web.setThumb(new UMImage(mContext, R.mipmap.ic_zzp_launcher));
    }



    public interface DialogButtonClickListener{
        public void onSelectDialogButtonClick(int requestCode, int resultCode);
    }

    public void showDialog(final DialogButtonClickListener listener){
        dlg = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_selector, null);
        dlg.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        dlg.getWindow().setGravity(Gravity.BOTTOM);
        dlg.setCanceledOnTouchOutside(true);
//        dlg.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dlg.show();
        contentView.findViewById(R.id.ll_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectDialogButtonClick(mReqCode,0);
                dlg.dismiss();
            }
        });
        contentView.findViewById(R.id.layout_wx).setOnClickListener(this);
        contentView.findViewById(R.id.layout_pengyou).setOnClickListener(this);
        contentView.findViewById(R.id.layout_qq).setOnClickListener(this);
        contentView.findViewById(R.id.layout_weibo).setOnClickListener(this);
        contentView.findViewById(R.id.layout_copy).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_wx:
                new ShareAction((Activity) mContext)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(mShareListener).share();
                break;
            case R.id.layout_pengyou:
                new ShareAction((Activity) mContext)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(mShareListener).share();
                break;
            case R.id.layout_qq:
                new ShareAction((Activity) mContext)
//                        .withText((type==0)?getString(R.string.share_text_member):getString(R.string.share_text_member))
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(mShareListener).share();
                break;
            case R.id.layout_weibo:
                new ShareAction((Activity) mContext)
//                        .withText((type==0)?getString(R.string.share_text_member):getString(R.string.share_text_member))
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .setCallback(mShareListener).share();
                break;
            case R.id.layout_copy:
                ClipboardManager mClipboardManager = (ClipboardManager)  mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("copy from 赚赚拍",recommendUrl);
                mClipboardManager.setPrimaryClip(clipData);
                ToastUtil.showShortToast("复制成功");
                break;
        }
    }

    private static class CustomShareListener implements UMShareListener {

//        private WeakReference<Activity> mActivity;

        private CustomShareListener(Activity activity) {
//            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {

            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.d("jsh","t:"+t.getMessage());
//            if("QQ".equals(platform.name())){
//                ToastUtil.showShortToast("请安装QQ客户端");
//            }else if("WEIXIN".equals(platform.name())||"WEIXIN_CIRCLE".equals(platform.name())){
//                ToastUtil.showShortToast("请安装微信客户端");
//            }
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {

                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    }

}
