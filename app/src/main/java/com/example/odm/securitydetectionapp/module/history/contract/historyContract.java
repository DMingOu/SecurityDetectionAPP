package com.example.odm.securitydetectionapp.module.history.contract;

import com.example.odm.securitydetectionapp.base.model.IBaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.IBaseView;
import com.example.odm.securitydetectionapp.module.history.ui.historyAdapter;

/**
 * The interface History contract.
 */
public interface historyContract {
    /**
     * The interface Model.
     */
    interface Model extends IBaseModel {

        /**
         * Load history list.
         *
         * @param adapter the adapter
         */
        void  loadHistoryList(historyAdapter adapter);
    }

    /**
     * The interface View.
     */
    interface View  extends IBaseView {

        /**
         * Gets adapter.
         *
         * @return the adapter
         */
        historyAdapter  getAdapter();
    }

    /**
     * The interface Presenter.
     */
    interface Presenter extends IBasePresenter {
        /**
         * Load history list.
         *
         * @param adapter the adapter
         */
        void  loadHistoryList(historyAdapter adapter);
    }
}
