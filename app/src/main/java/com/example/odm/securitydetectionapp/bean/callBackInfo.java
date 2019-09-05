package com.example.odm.securitydetectionapp.bean;

import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: ODM
 * @date: 2019/7/25
 */

public class callBackInfo {

    /**
     * 手机反馈给服务器(->帽子)的消息  && 服务器反馈给地图页面的信息
     */

    private String address;
    private String message;
    private boolean locate;
    private boolean rescue;

    public callBackInfo(boolean locate,boolean rescue,String addres ,String message) {
        this.address = addres;
        this.locate = locate;
        this.rescue = rescue;
        this.message = message;
    }


    public callBackInfo() {
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLocate() {
        return locate;
    }

    public void setLocate(boolean locate) {
        this.locate = locate;
    }

    public boolean isRescue() {
        return rescue;
    }

    public void setRescue(boolean rescue) {
        this.rescue = rescue;
    }

    @NonNull
    @Override
    public String toString() {
        return  "地址: " + address +
                "   locate: " + locate +
                "   rescue: " + rescue +
                "   message: " + message;
    }

    public boolean getLocate() {
        return this.locate;
    }

    public boolean getRescue() {
        return this.rescue;
    }
}
