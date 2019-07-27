package com.example.odm.securitydetectionapp.core;

import android.content.Context;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoMaster;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class GreenDaoManager {
    private static final String TAG = "GreenDaoManager";

    public static final  String DATABASE_NAME = "historyErrorMsg.db";


     //全局保持一个DaoSession
    private DaoSession historyDaoSession;

    //判断是否初始化
    private boolean isInited;

    private static final class GreenDaoManagerHolder {
        private static final GreenDaoManager mInstance = new GreenDaoManager();
    }

    public static GreenDaoManager getInstance() {
        return GreenDaoManagerHolder.mInstance;
    }

    private GreenDaoManager() {

    }

    /**
     * 初始化DaoSession
     *
     * @param context
     */
    public void init(Context context) {
        if (!isInited) {
            DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(
                    context.getApplicationContext(), DATABASE_NAME, null);
            DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
            historyDaoSession = daoMaster.newSession();
            isInited = true;
        }
    }

    public DaoSession getDaoSession() {
        return historyDaoSession;
    }
}
