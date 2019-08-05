package com.example.odm.securitydetectionapp.module.watch.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseFragment;
import com.example.odm.securitydetectionapp.common.Constant;

import com.example.odm.securitydetectionapp.common.PopupWindowList;
import com.example.odm.securitydetectionapp.common.emptyView;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.bean.capInfo;
import com.example.odm.securitydetectionapp.module.watch.contract.watchContract;
import com.example.odm.securitydetectionapp.module.watch.presenter.watchPresenter;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.orhanobut.logger.Logger;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;
import com.xuexiang.xui.widget.popupwindow.status.Status;
import com.xuexiang.xui.widget.popupwindow.status.StatusView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class watchFragment<P extends IBasePresenter> extends BaseFragment<watchPresenter> implements watchContract.View {


    @BindView(R.id.status)
    StatusView view_Status;
    @BindView(R.id.tb_home)
    CommonTitleBar tb_Home;
    Unbinder unbinder;
    @BindView(R.id.rv_module)
    RecyclerView rv_Module;

    private capInfoAdapter mAdapter;
    private List<capInfo> mCapList;
    private boolean  currentVisity;
    MaterialDialog.Builder builder;
    private PopupWindowList mPopupWindowList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        currentVisity = true;
        initViews();
        initToolbar();
        return view;
    }


    public void initViews() {
        builder  = new MaterialDialog.Builder(getContext());
        mCapList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_Module.setLayoutManager(layoutManager);
        mAdapter = new capInfoAdapter(R.layout.item_cap ,mCapList);
        rv_Module.setAdapter(mAdapter);
        //子项点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //如果子模块的属性为异常，则会呼出一个信息弹窗
                String errorData = mAdapter.getData().get(position).getData();
                if(! "".equals(errorData)){
                    showSimpleTipDialog(errorData);
                }
            }
        });
        //子项长按事件
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                String errorData = mAdapter.getData().get(position).getData();
                //非异常状态不能发送反馈
                if(! "".equals(errorData)) {
                    //触发效果：长按呼出弹窗，点击确认按钮后发送反馈
                    showSimpleConfirmDialog(mAdapter.getData().get(position).getAddress());
                    //触发效果:长按呼出跟随子项的按钮列表，列表，列表
//                    View itemview = rv_Module.getChildAt(position);
//                    showCallButton(itemview , mAdapter.getData().get(position).getAddress());
                }
                return false;
            }
        });
        //为Adaper加载空布局
        getAdapter().setEmptyView(new emptyView(getContext() ,null));
        //加载布局
        view_Status.setStatus(Status.LOADING);
        //手动点击关闭 "连接失败"弹窗
        view_Status.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_Status.dismiss();
            }
        });
    }



    public void initToolbar() {
        tb_Home.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //弹出弹窗切换服务器
                showSwitchDialog();
            }
        });
    }


    @Override
    public watchPresenter onBindPresenter() {
        return new watchPresenter(this);
    }


    //注册绑定EventBus
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    /*
      *  处理服务器发过来的信息
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void handleEvent(BaseEvent baseEvent) {
        super.handleEvent(baseEvent);
        //对 事件类型为 请求状态 处理事件
        //若当前页面为隐藏页面，则不接收服务器发来的信息
        if (currentVisity) {
            if (baseEvent != null && Constant.STATUS.equals(baseEvent.type)) {
                if (view_Status != null) {
                    switch (baseEvent.content) {
                        case Constant.SUCCESS:
                            view_Status.dismiss();
                            view_Status.setStatus(Status.COMPLETE);
                            Logger.d("WebSocket连接成功了" + TimeUtil.showCurrentTime(System.currentTimeMillis()));
                            break;
                        case Constant.FAILURE:
                            view_Status.dismiss();
                            view_Status.setStatus(Status.ERROR);
                            Logger.d("连接失败的url:" + SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK));
                            break;
                        default:
                    }
                }
            }
            if (baseEvent != null && Constant.CAP.equals(baseEvent.type) && getAdapter() != null) {
                //子模块信息到了，要进行处理，把它加入列表里面
                if (baseEvent.content.startsWith("嵌")) {
                    CookieBar.builder(getActivity())
                            .setTitle("嵌入式设备已下线")
                            .setIcon(R.mipmap.warning_yellow)
                            .setMessage("子模块信息初始化")
                            .setLayoutGravity(Gravity.BOTTOM)
                            .setAction(R.string.known, null)
                            .show();
                }
                //准备将子模块加入列表中
                getPresenter().checkCapInfo(baseEvent.content, getAdapter());
            } else {
                Logger.d("adapter还没初始化" + "消息为" + baseEvent.content);
            }
        }
    }

    @Override
    protected void lazyLoadData() {
        //需要每次进入页面才加载的内容
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            currentVisity = false;
        } else {
            // 可视
            currentVisity = true;
        }
    }

    @Override
    public capInfoAdapter getAdapter() {
        return this.mAdapter;
    }

    /**
     * 弹出输入对话框，切换服务器.
     */
    public void showSwitchDialog() {
        //new MaterialDialog.Builder(getContext())
        builder.iconRes(R.drawable.ic_watch_dialog_warning_red)
                .title("提示")
                .content("当前服务器地址: " + "\n" + SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK) + "\r确定要切换吗")
                .inputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(
                        getString(R.string.hint_please_input_password),
                        "",
                        false,
                        (new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                //String newUrl = input.toString();
                            }
                        }))
                //右下角的按钮--确认按钮
                .positiveText(R.string.lab_switch)
                //左下角的按钮--取消按钮
                .negativeText(R.string.lab_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newUrl = dialog.getInputEditText().getText().toString();
                        Logger.d(newUrl);
                        if (! "".equals(newUrl) || newUrl.startsWith("w")) {
                            getPresenter().switchWebSocket(newUrl, getAdapter());
                        } else {
                            ToastUtil.showShortToastCenter("你输入的服务器地址不合法");
                        }
                    }
                })
                .cancelable(true)
                .show();
    }

    /**
     * 简单的提示性对话框
     */
    public void showSimpleTipDialog(String  warnString) {
//        new MaterialDialog.Builder(getContext())
                builder.iconRes(R.mipmap.warning_yellow)
                .title(R.string.warning)
                .content(warnString)
                .positiveText(R.string.known)
                .show();
    }



    /**
     * 简单的确认对话框
     */
    public void showSimpleConfirmDialog(String address) {
//        new MaterialDialog.Builder(getContext())
                builder.content("当前选中模块地址为：" + address)
                .positiveText("确认发送反馈")
                .negativeText("取消发送")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getPresenter().sendCallBack(address);
                    }
                })
                .show();
    }


    /*
    * 显示跟随View的列表--仿微信长按效果
    */

    public void showCallButton(View  view ,String  address) {
        List<String> callBackList = new ArrayList<>();
        callBackList.add("发送反馈");
        if (mPopupWindowList == null){
            mPopupWindowList = new PopupWindowList(view.getContext());
        }
        mPopupWindowList.setAnchorView(view);
        mPopupWindowList.setItemData(callBackList);
        mPopupWindowList.setModal(true);
        mPopupWindowList.show();
        mPopupWindowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().sendCallBack(address);
                mPopupWindowList.hide();
            }
        });
    }


}
