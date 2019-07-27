package com.example.odm.securitydetectionapp.module.home.bean;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class callBackInfo {

    /**
     * 手机反馈给服务器(->帽子)的消息
     * address : 020A
     * command : loacte
     */

    private String address;
    private String command;

    public callBackInfo(String addres ,String command) {
        this.address = addres;
        this.command = command;
    }
    //command 字段 属性规定为 "rescue" "locate"

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
