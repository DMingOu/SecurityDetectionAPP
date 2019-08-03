package com.example.odm.securitydetectionapp.module.watch.ui;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.bean.capInfo;

import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class capInfoAdapter extends BaseQuickAdapter <capInfo, BaseViewHolder> {

    public capInfoAdapter (int layoutResId , List data) {
        super(layoutResId , data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, capInfo item) {
        //首先在进入List前会进行判断是否为异常，如果数据异常无法加入List。
        // 如果子模块的状态为正常(true),则赋值
        if(item.isStatus()) {
            if("".equals(item.getData())) {
                helper.setText(R.id.tv_cap_status, "正常");
                helper.setBackgroundRes(R.id.tv_cap_status ,R.drawable.status_normal );
                helper.setBackgroundRes(R.id.iv_cap ,R.drawable.head_normal);
            } else {
                helper.setText(R.id.tv_cap_status,"异常");
                //异常，将文字背景更换为红色
                helper.setBackgroundRes(R.id.tv_cap_status ,R.drawable.status_abnormal);
                helper.setBackgroundRes(R.id.iv_cap , R.drawable.head_abnormal);

            }
            helper.setText(R.id.tv_cap_location ,item.getAddress());

        }
    }
}
