package com.hx.zzp.activity.partner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.bg.baseutillib.net.RxNetCallback;
import com.bg.baseutillib.net.exception.ApiException;
import com.bg.baseutillib.tool.ToastUtil;
import com.hx.zzp.MyApplication;
import com.hx.zzp.R;
import com.hx.zzp.RvBaseActivity;
import com.hx.zzp.activity.adapter.PartnerAdapter;
import com.hx.zzp.bean.Partner;
import com.hx.zzp.net.login.LoginDao;
import com.hx.zzp.net.login.response.SessionBean;
import com.hx.zzp.utils.StringUtils;
import com.hx.zzp.utils.Utils;
import com.hx.zzp.widget.AddSpaceTextWatcher;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
/**
 * 加入合伙人说明
 */
public class PartnerStatementActivity extends RvBaseActivity {

    @BindView(R.id.et_partner_level)
    TextView tvParnerLevel;

    @BindView(R.id.et_user_name)
    EditText etUserName;


    @BindView(R.id.et_id_card)
    EditText etIdCard;

    @BindView(R.id.vip_recycler)
    RecyclerView recyclerView;

    private PartnerAdapter partnerAdapter;

    private String[]vipLevels;

    private String[]vipDescs;

    private List<Partner> mDataList;

    private AddSpaceTextWatcher[] asEditTexts=new AddSpaceTextWatcher[3];

    private EditText[] editTexts=new EditText[3];

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        MyApplication.getInstance().addEnterActivity(this);
        editTexts[2]= etIdCard;//身份证
        asEditTexts[2]=new AddSpaceTextWatcher(editTexts[2],21);
        asEditTexts[2].setSpaceType(AddSpaceTextWatcher.SpaceType.IDCardNumberType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_partner_statement;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        initDataList();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
//        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
//        layoutManager.setOrientation(OrientationHelper.VERTICAL);//设置为垂直布局，这也是默认的
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();

                if (count > 0) {
                    if(count>4){
                        count =4;
                    }
                    int realHeight = 0;
                    int realWidth = 0;
                    for(int i = 0;i < count; i++){
                        View view = recycler.getViewForPosition(0);
                        if (view != null) {
                            measureChild(view, widthSpec, heightSpec);
                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                            int measuredHeight = view.getMeasuredHeight();
                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                            realHeight += measuredHeight;
                        }
                        setMeasuredDimension(realWidth, realHeight);
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });
        partnerAdapter = new PartnerAdapter(mDataList);
        recyclerView.setAdapter(partnerAdapter);//设置Adapter
    }

    private void initDataList(){
        vipLevels = getResources().getStringArray(R.array.vip_levels);
        vipDescs = getResources().getStringArray(R.array.vip_descs);
        mDataList = new ArrayList<Partner>();
        for (int i = 0; i<vipLevels.length;i++){
            Partner partner = new Partner();
            partner.vipLevel = vipLevels[i];
            partner.desc = vipDescs[i];
            mDataList.add(partner);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public LoginDao onCreateRequestData() {
        return new LoginDao();
    }
    private Dialog mDialog;
    @OnClick({R.id.btn_join_parner,R.id.re_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_join_parner://加入合伙人
                String name = etUserName.getText().toString();
                String idCard = StringUtils.remove(etIdCard.getText().toString());
                String levelString = tvParnerLevel.getText().toString();

                if(checkData()){
                    mDialog = Utils.comitProgressDialog(this);
                    ((LoginDao)createRequestData).bePartner(this, name, idCard,getLevel(levelString), new RxNetCallback<SessionBean>() {
                        @Override
                        public void onSuccess(SessionBean userBean) {
                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("username",etUserName.getText().toString());
                            bundle.putString("idcard",StringUtils.remove(etIdCard.getText().toString()));
                            if(tvParnerLevel.getTag() != null){
                                bundle.putString("amount",tvParnerLevel.getTag().toString());
                            }
                            bundle.putString("vipLevel",tvParnerLevel.getText().toString());
                            startActivity(PayActivity.class,bundle);
                        }
                        @Override
                        public void onError(ApiException e) {
                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }
                            if(!TextUtils.isEmpty(e.getMessage())){
                                ToastUtil.showShortToast(e.getMessage());
                            }
                        }
                    });

                }
                break;
            case R.id.re_choose://登录
                showChoise();
                break;
        }
    }
    private String getLevel(String levelString){
        String level = "";
        if("vip1".equals(levelString)){
            level = "1";
            return level;
        } if("vip2".equals(levelString)){
            level = "2";
            return level;
        }
        if("vip3".equals(levelString)){
            level = "3";
            return level;
        }
        if("vip4".equals(levelString)){
            level = "4";
            return level;
        }
        if("vip5".equals(levelString)){
            level = "5";
            return level;
        }
        if("vip6".equals(levelString)){
            level = "6";
            return level;
        }
        if("vip7".equals(levelString)){
            level = "7";
            return level;
        }
        if("vip8".equals(levelString)){
            level = "8";
            return level;
        }
        if("vip9".equals(levelString)){
            level = "9";
            return level;
        }
        if("vip10".equals(levelString)){
            level = "10";
            return level;
        }
        return level;
    }

    private  boolean checkData() {
        boolean isValidate = true;
        if (TextUtils.isEmpty(etUserName.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入姓名");
            isValidate = false;
            return  isValidate;
        }
//        if (TextUtils.isEmpty(etIdCard.getText().toString().trim())) {
//            ToastUtil.showShortToast("请输入身份证号");
//            isValidate = false;
//            return  isValidate;
//        }
        if(TextUtils.isEmpty(tvParnerLevel.getText().toString())){
            ToastUtil.showShortToast("请选择代理人等级");
            isValidate = false;
            return  isValidate;
        }
        return  isValidate;
    }
    private void showChoise()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialog);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请选择代理人等级");
        //    指定下拉列表的显示数据
        final String[] levels = {"vip1", "vip2", "vip3", "vip4", "vip5","vip6", "vip7", "vip8", "vip9", "vip10"};
        final String[] levelMoneys = {"10000", "20000", "30000", "40000", "50000","60000", "70000", "80000", "90000", "100000"};
        //    设置一个下拉的列表选择项
        builder.setItems(levels, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                tvParnerLevel.setText(levels[which]);
                tvParnerLevel.setTag(levelMoneys[which]);
            }
        });
        builder.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
