package com.hx.zzp.net.login;

import com.bg.baseutillib.net.CommonNetBean;
import com.bg.baseutillib.net.base.BaseResponse;
import com.hx.zzp.net.ApiManager;
import com.hx.zzp.net.login.request.CodeBody;
import com.hx.zzp.net.login.request.RegisterBody;
import com.hx.zzp.net.login.response.CodeBean;
import com.hx.zzp.net.login.response.SessionBean;
import com.hx.zzp.net.login.response.UserBean;
import com.hx.zzp.utils.AppUserData;

import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lijie on 2018/6/29
 * Description:
 * Email: 731098696@qq.com
 * Version：1.0
 */
public interface LoginNetService {
    /**
     * 登录
     */
    @POST(ApiManager.LOGIN)
    Observable<BaseResponse<SessionBean>> login(@Body Map<String, String> paramsMap);


    /**
     * app用户详情信息
     */
    @GET(ApiManager.FINDDETAIL)
    Observable<BaseResponse<UserBean>> findDetail();
    /**
     * 注册
     */
    @POST(ApiManager.REGISTER)
    Observable<BaseResponse<SessionBean>> register(@Body RegisterBody body);

    /**
     * 获取验证码
     */
    @POST(ApiManager.FIND_MOBILE_CODE)
    Observable<CodeBean> findMobileCode(@Body Map<String, String> paramsMap);

    /**
     * app重置密码
     */
    @POST(ApiManager.RESETPASSWORD)
    Observable<BaseResponse<SessionBean>> resetPassword(@Body Map<String, String> paramsMap);

    /**
     * 校验是否注册
     */
    @POST(ApiManager.CHECKREGISTER)
    Observable<BaseResponse<CommonNetBean>> checkRegister(@Query("mobile") String mobile);
    /**
     * 扫码确认登陆
     */
    @POST(ApiManager.AGREELOGIN)
    Observable<BaseResponse<CommonNetBean>> agreeLogin(@Query("code") String code,
                                                       @Query("sessionId") String sessionId);

    /**
     * 成为合伙人
     */
    @POST(ApiManager.PARTNER_URL)
    Observable<SessionBean> bePartner(@Body Map<String, String> paramsMap);
    /**
     * 合伙人升级
     */
    @POST(ApiManager.UPGRADE_PARTNER_URL)
    Observable<SessionBean> partnerLevelUp(@Body Map<String, String> paramsMap);

}
