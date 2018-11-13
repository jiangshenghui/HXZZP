package com.hx.zzp;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.bg.baseutillib.BgApplication;
import com.bg.baseutillib.tool.FileUploadUtil;
import com.hx.zzp.net.ApiManager;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.LinkedList;
import java.util.List;


public class MyApplication extends BgApplication {

    private static MyApplication mInstance = null;
    private List<Activity> enterList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FileUploadUtil.init(getApplicationContext(), ApiManager.HOST);
//        UMShareAPI.get(this);
    }
    public static MyApplication getInstance() {
        return mInstance;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //添加Activity到容器中
    public void addEnterActivity(Activity activity)
    {
        enterList.add(activity);
    }
    //遍历所有Activity并finish
    public void exitEnterAllActivity()
    {
        for(Activity activity:enterList)
        {
            activity.finish();
        }
    }



    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin(ApiManager.APP_ID,ApiManager.WX_APP_SECRET);
        //新浪微博
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        // qq空间
        PlatformConfig.setQQZone(ApiManager.QQ_APP_ID,ApiManager.QQ_WX_APP_KEY);

    }
}
