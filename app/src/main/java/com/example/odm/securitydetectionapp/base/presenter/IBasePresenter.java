package com.example.odm.securitydetectionapp.base.presenter;

import com.example.odm.securitydetectionapp.base.view.IBaseView;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public interface IBasePresenter <V extends IBaseView> {

    /**
     * 判断 presenter 是否与 view 建立联系，防止出现内存泄漏状况
     *
     * @return true:联系已经建立  false:联系已断开
     */
    boolean isAttachView();

    /**
     * 断开 presenter 与 view 之间的联系
     */
    void  detachView();
}
