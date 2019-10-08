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
     * 手机反馈给服务器(->帽子)的消息
     * address : 需要反馈的模块的地址
     * message : 反馈信息，固定为: "" 空字符串
     * rescue : 反馈状态，固定为: true
     */

    private String address;
    private String message;
    private boolean rescue;

    public callBackInfo(boolean rescue,String address ,String message) {
        this.address = address;
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


    public boolean isRescue() {
        return rescue;
    }

    public void setRescue(boolean rescue) {
        this.rescue = rescue;
    }

    @NonNull
    @Override
    public String toString() {
        return  "反馈模块地址: " + address +
                "   rescue: " + rescue +
                "   message: " + message;
    }


    public boolean getRescue() {
        return this.rescue;
    }
}
