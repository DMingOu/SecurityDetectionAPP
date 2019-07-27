package com.example.odm.securitydetectionapp.module.history.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: ODM
 * @date: 2019/7/27
 */
@Entity
public class historyErrorMsg {

    @Id(autoincrement = true)
    Long ID;
    private String time;
    private String  errorMsg;
    private String  address;

    public historyErrorMsg(String time ,String errorMsg ,String address) {
        this.time = time;
        this.errorMsg  = errorMsg;
        this.address = address;
    }

    @Generated(hash = 511369551)
    public historyErrorMsg(Long ID, String time, String errorMsg, String address) {
        this.ID = ID;
        this.time = time;
        this.errorMsg = errorMsg;
        this.address = address;
    }

    @Generated(hash = 1743685529)
    public historyErrorMsg() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }



}
