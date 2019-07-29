package com.aserbao.aserbaosandroid.functions.database.greenDao.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;
import com.example.odm.securitydetectionapp.module.home.bean.capInfo;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.historyErrorMsgDao;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.capInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig historyErrorMsgDaoConfig;
    private final DaoConfig capInfoDaoConfig;

    private final historyErrorMsgDao historyErrorMsgDao;
    private final capInfoDao capInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        historyErrorMsgDaoConfig = daoConfigMap.get(historyErrorMsgDao.class).clone();
        historyErrorMsgDaoConfig.initIdentityScope(type);

        capInfoDaoConfig = daoConfigMap.get(capInfoDao.class).clone();
        capInfoDaoConfig.initIdentityScope(type);

        historyErrorMsgDao = new historyErrorMsgDao(historyErrorMsgDaoConfig, this);
        capInfoDao = new capInfoDao(capInfoDaoConfig, this);

        registerDao(historyErrorMsg.class, historyErrorMsgDao);
        registerDao(capInfo.class, capInfoDao);
    }
    
    public void clear() {
        historyErrorMsgDaoConfig.clearIdentityScope();
        capInfoDaoConfig.clearIdentityScope();
    }

    public historyErrorMsgDao getHistoryErrorMsgDao() {
        return historyErrorMsgDao;
    }

    public capInfoDao getCapInfoDao() {
        return capInfoDao;
    }

}
