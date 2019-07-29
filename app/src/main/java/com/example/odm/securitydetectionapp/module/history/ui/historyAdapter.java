package com.example.odm.securitydetectionapp.module.history.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;

import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/28
 */
public class historyAdapter extends BaseQuickAdapter<historyErrorMsg , BaseViewHolder> {

    public historyAdapter(int layoutResId , List data) {
        super(layoutResId , data);
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, historyErrorMsg item) {
        helper.setText(R.id.tv_history_address , item.getAddress());
        helper.setText(R.id.tv_history_msg ,item.getErrorMsg());
        helper.setText(R.id.tv_history_time ,item.getTime());

    }


}
