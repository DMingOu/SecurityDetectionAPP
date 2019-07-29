package com.example.odm.securitydetectionapp.module.history.model;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;
import com.example.odm.securitydetectionapp.module.history.contract.historyContract;
import com.example.odm.securitydetectionapp.module.history.ui.historyAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class historyModel implements historyContract.Model {

    private DaoSession historyDaoSession;
    private List<historyErrorMsg> mList = new ArrayList<>();
    public historyModel(){
        historyDaoSession = GreenDaoManager.getInstance().getDaoSession();
    }
    @Override
    public void loadHistoryList(historyAdapter adapter) {
        QueryBuilder<historyErrorMsg> qb = historyDaoSession.queryBuilder(historyErrorMsg.class);
        //先将之前的历史记录清空，再将最新的加入
        mList.clear();
        adapter.setNewData(mList);
        adapter.notifyDataSetChanged();
        mList.addAll(qb.list());
        adapter.addData(mList);
        adapter.notifyDataSetChanged();
    }
}
