package com.hx.zzp.net.update;

import com.hx.zzp.net.ApiManager;
import com.hx.zzp.net.pay.response.PreOrderBean;
import com.hx.zzp.net.update.response.UpdateBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UpdateNetService {

    /**
     * 升级
     */
    @POST(ApiManager.PAY_URL)
    Observable<UpdateBean> update(@Body Map<String, String> paramsMap);
}
