package com.example.odm.securitydetectionapp.base.view;

import android.app.Activity;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public interface IBaseView {



    /**
     * 显示正在加载 view
     */
    void showLoading();

    /**
     * 关闭正在加载 view
     */
    void hideLoading();
}
