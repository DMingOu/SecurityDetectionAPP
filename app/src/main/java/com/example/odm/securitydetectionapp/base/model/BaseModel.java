package com.example.odm.securitydetectionapp.base.model;

import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;

import java.lang.ref.WeakReference;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public abstract class BaseModel <P extends IBasePresenter> implements IBaseModel{

    private WeakReference<P> mPresenterRef;

    public P getPresenter(){
        return mPresenterRef.get();
    }

}
