package com.example.odm.securitydetectionapp.bean;

import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: ODM
 * @date: 2019/7/25
 */

@Entity
public class capInfo {

    @Id(autoincrement = true)
    Long ID;
    /**
     * address : 020A  模块的地址
     * data : 摔倒了   当data 为空，则此模块处于正常状态，当data 非空字符串，则此模块处于异常状态，异常信息为 data值
     * status : true  当status 为true说明此模块信息为真实模块(具有正常或异常状态)
     */

    private String address;
    private String data;
    private boolean status;

    public capInfo(String address ,String data ,boolean status) {
        this.address = address;
        this.data =data;
        this.status = status;
    }
    @Generated(hash = 447490699)
    public capInfo(Long ID, String address, String data, boolean status) {
        this.ID = ID;
        this.address = address;
        this.data = data;
        this.status = status;
    }

    @Generated(hash = 571532872)
    public capInfo() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    //status为false时为子模块数据异常 ,true 为子模块处于正常状态
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "  address:  " + address +
                " data:   " +  data +
                " status:  " + status;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public boolean getStatus() {
        return this.status;
    }
}
