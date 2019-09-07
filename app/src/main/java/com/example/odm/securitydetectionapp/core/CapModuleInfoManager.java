package com.example.odm.securitydetectionapp.core;

import com.example.odm.securitydetectionapp.bean.capInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局类，获取帽子模块的信息
 *
 * @author: ODM
 * @date: 2019/9/7
 */
public class CapModuleInfoManager {

    private static List<capInfo>  capInfoList = new ArrayList<>();

    public static List<capInfo> getCapInfoList() {
        return capInfoList;
    }

    public static void setCapInfoList(List<capInfo> capInfoList) {
        CapModuleInfoManager.capInfoList = capInfoList;
    }
}
