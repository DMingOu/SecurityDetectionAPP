package com.aserbao.aserbaosandroid.functions.database.greenDao.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.odm.securitydetectionapp.bean.capInfo;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.capInfoDao;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.historyErrorMsgDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig capInfoDaoConfig;
    private final DaoConfig historyErrorMsgDaoConfig;

    private final capInfoDao capInfoDao;
    private final historyErrorMsgDao historyErrorMsgDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        capInfoDaoConfig = daoConfigMap.get(capInfoDao.class).clone();
        capInfoDaoConfig.initIdentityScope(type);

        historyErrorMsgDaoConfig = daoConfigMap.get(historyErrorMsgDao.class).clone();
        historyErrorMsgDaoConfig.initIdentityScope(type);

        capInfoDao = new capInfoDao(capInfoDaoConfig, this);
        historyErrorMsgDao = new historyErrorMsgDao(historyErrorMsgDaoConfig, this);

        registerDao(capInfo.class, capInfoDao);
        registerDao(historyErrorMsg.class, historyErrorMsgDao);
    }
    
    public void clear() {
        capInfoDaoConfig.clearIdentityScope();
        historyErrorMsgDaoConfig.clearIdentityScope();
    }

    public capInfoDao getCapInfoDao() {
        return capInfoDao;
    }

    public historyErrorMsgDao getHistoryErrorMsgDao() {
        return historyErrorMsgDao;
    }

}
