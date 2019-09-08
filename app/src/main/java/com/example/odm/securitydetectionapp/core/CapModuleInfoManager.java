package com.example.odm.securitydetectionapp.core;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;
import com.example.odm.securitydetectionapp.bean.capInfo;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;
import com.example.odm.securitydetectionapp.module.watch.ui.capInfoAdapter;
import com.example.odm.securitydetectionapp.util.GsonUtil;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.orhanobut.logger.Logger;


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

    private static DaoSession historyDaoSession = GreenDaoManager.getInstance().getDaoSession();


    /**
     * 将帽子模块加入列表
     * @param capInfoData 帽子模块json字符串
     */
    public static void addCapInfo(String capInfoData ) {
        //先将JSon数据转换成 子模块对象
        capInfo mCapInfo = GsonUtil.GsonToBean(capInfoData, capInfo.class);
        if (mCapInfo.getData() == null) {
            Logger.d("类型不符合capInfo");
            return;
        }else if(mCapInfo.getData().startsWith("我")) {
            mCapInfo.setData("受到撞击");
        } else if(mCapInfo.getData().startsWith("摔")) {
            mCapInfo.setData("摔倒");
        } else if(mCapInfo.getData().startsWith("求")) {
            mCapInfo.setData("求救");
        }
        //当子模块的异常信息非空时。需要添加进历史消息对应的数据库,而且是新的子模块消息
        if(! "".equals(mCapInfo.getData()) && mCapInfo.getStatus() ) {
            historyErrorMsg msg = new historyErrorMsg();
            msg.setTime(TimeUtil.showCurrentTime(System.currentTimeMillis()));
            msg.setAddress(mCapInfo.getAddress());
            String errorInfo = mCapInfo.getData();
            errorInfo = errorInfo.replaceAll("\r | \n" ,"");
            msg.setErrorMsg(errorInfo);
            historyDaoSession.insert(msg);
        }

        if (capInfoList != null) {

            boolean isDuplicated = false;
            //若有相同的地址，则修改；没有相同的地址，则直接添加进列表
            for (int i = 0; i < capInfoList.size(); i++) {
                if (mCapInfo.getAddress().equals(capInfoList.get(i).getAddress())) {
                    //子模块的在线状态为正常，则放在列表最上面，若为不正常，则在列表中查找并删除它
                    if (mCapInfo.isStatus()) {
                        //当 找到相同地址的帽子模块，判断异常信息是否为空，如果有异常，则无法更改，放弃本条信息
                        if("".equals(capInfoList.get(i).getData()) && ! "".equals(mCapInfo.getData())) {
                            capInfoList.get(i).setData(mCapInfo.getData());
                        } else if(! mCapInfo.getData().equals(capInfoList.get(i).getData()) && ! "".equals(mCapInfo.getData())) {
                            capInfoList.get(i).setData(mCapInfo.getData());
                        } else {
                            return;
                        }

                    } else {
                        capInfoList.remove(i);
                    }
                    isDuplicated = true;
                    break;
                }
            }
            //不重复，说明初次出现的子模块，需要加入子模块列表
            if (!isDuplicated) {
                capInfoList.add(mCapInfo);
            }
        }

    }



    /**
     * 清空帽子模块列表里面的数据
     */
    public void clearAllCapInfo() {
        if(capInfoList != null) {
            capInfoList.clear();
        }
    }

    public static void setCapInfoList(List<capInfo> List) {
        capInfoList.clear();
        capInfoList.addAll(List);
    }

    public static List<capInfo> getCapInfoList() {
        if (capInfoList != null) {
            return capInfoList;
        } else {
            return  new ArrayList<>();
        }
    }

}
