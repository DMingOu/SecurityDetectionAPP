package com.example.odm.securitydetectionapp.module.history.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseView;
import com.example.odm.securitydetectionapp.common.noHistoryView;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;
import com.example.odm.securitydetectionapp.module.history.contract.historyContract;
import com.example.odm.securitydetectionapp.module.history.presenter.historyPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.statelayout.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class historyFragment<P extends IBasePresenter> extends BaseView<historyPresenter> implements historyContract.View {

    @BindView(R.id.rv_history)
    RecyclerView rv_History;
    Unbinder unbinder;
    @BindView(R.id.fb_updown)
    FloatingActionButton fb_Updown;
    private historyAdapter mAdapter;
    private List<historyErrorMsg> msgList;
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtils.setStatusBarLightMode(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }


    public void initViews() {
        msgList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(true);
        mAdapter = new historyAdapter(R.layout.item_history_error_msg, msgList);
        rv_History.setLayoutManager(layoutManager);
        rv_History.setAdapter(mAdapter);
        mAdapter.setEmptyView(new noHistoryView(getContext(), null));
        if( mAdapter.getItemCount() > 1) {
            fb_Updown.setVisibility(View.VISIBLE);
            Logger.d("设置悬浮按钮可见");
        } else {
            fb_Updown.setVisibility(View.INVISIBLE);
        }
        fb_Updown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothMoveToPosition(rv_History , 0);
            }
        });
    }

    @Override
    protected void lazyLoadData() {
        Logger.d("调用数据库加载历史记录");
        getPresenter().loadHistoryList(mAdapter);
        rv_History.scrollToPosition(mAdapter.getItemCount() - 1 );
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rv_History.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1  , 0);
    }

    @Override
    public historyAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override
    public historyPresenter onBindPresenter() {
        return new historyPresenter(this);
    }


    //目标项是否在最后一个可见项之后 private boolean mShouldScroll; //记录目标项位置 private int mToPosition;
    //滑动到指定位置
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) { // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }
}
