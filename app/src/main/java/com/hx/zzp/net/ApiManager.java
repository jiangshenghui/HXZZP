package com.hx.zzp.net;

public class ApiManager {

//      ### 测试环境地址 http://192.168.31.241:7001
//            ### 生产环境地址 http://pai.foxxgame.com
    /**
     * app的域名
     */
    //public static final String HOST = "http://www.lvdaotaxi.com/";//正式环境
//    public static final String HOST = "http://47.107.72.255/";//正式环境
//    public static final String HOST = "https://daorv-rent.icebartech.com/";//测试环境
//    public static final String HOST = "http://192.168.31.241:7001/";//测试环境
    public static final String HOST =  "https://pai.foxxgame.com/";//正式环境

//    public static final String HOST = "http://yk2w5t.natappfree.cc/";//测试环境




    public final static String APP_ID= "wx199ba0e2e9362687" ;
    public final static String WX_APP_SECRET= "e657944cb7ca233ceeebdf9448974877";
    public final static String QQ_APP_ID= "1106968884" ;
    public final static String QQ_WX_APP_KEY= "09krWW4i91ZQuCec";
    public final static String WX_TYPE = "02" ;

    /**
     * app登录
     */
    public static final String LOGIN = "api/user/login";


    /**
     * app用户详情信息
     */
    public static final String FINDDETAIL = "api/user";

    /**
     * 扫码确认登陆
     */
    public static final String AGREELOGIN = "api/app/usercore/agreeLogin";
    /**
     * app注册
     */
    public static final String REGISTER = "api/app/usercore/register";
    /**
     * 校验是否注册
     */
    public static final String CHECKREGISTER = "api/app/usercore/checkRegister";
    /**
     * 获取验证码
     */
    public static final String FIND_MOBILE_CODE = "api/captcha";
    /**
     * app重置密码
     */
    public static final String RESETPASSWORD = "api/user/forgotPassword";
    /**
     * 支付订单
     */
    public static final String PAY_URL = "api/pay/createOrder";

    /**
     * 成为合伙人
     */
    public static final String PARTNER_URL = "api/user/bePartner";

    /**
     * 合伙人
     */
    public static final String UPGRADE_PARTNER_URL = "api/user/partnerLevelUp";


}
