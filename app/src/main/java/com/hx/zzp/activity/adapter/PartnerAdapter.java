package com.hx.zzp.activity.adapter;

import android.widget.TextView;
import com.bg.baseutillib.base.BaseListAdapter;
import com.hx.zzp.R;
import com.hx.zzp.bean.Partner;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PartnerAdapter extends BaseListAdapter<Partner> {

    @BindView(R.id.tv_vip_level)
    TextView tvVipLevel;
    @BindView(R.id.tv_vip_desc)
    TextView tvDesc;

    private List<Partner> mDataList;

    public PartnerAdapter(List<Partner> mDataList) {
            this.mDataList = mDataList;
        }

        @Override
        protected int[] setItemLayouts() {
            return new int[]{R.layout.item_partner};
        }

        @Override
        public int setItemViewType(int position) {
            return 0;
        }

        @Override
        protected List<Partner> setDataList() {
            return mDataList;
        }

        @Override
        protected void bindData(int itemViewType, final int position, BgViewHolder viewHolder, List<Partner> dataList) {
            ButterKnife.bind(this, viewHolder.itemView);
            tvVipLevel.setText(mDataList.get(position).vipLevel);
            tvDesc.setText(mDataList.get(position).desc);
        }

        private OnBtnClickListener mOnBtnClickListener;

        public interface OnBtnClickListener {
            void onDetails(int position);

            void onDoRent(int position);

            void onAgain(int position);

            void onComment(int position);

            void onDoing(int position);

            void onDoPay(int position);
        }

        public void setOnBtnClickListener(OnBtnClickListener listener) {
            mOnBtnClickListener = listener;
        }
    }
