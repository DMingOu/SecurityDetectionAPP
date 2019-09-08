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
import com.example.odm.securitydetectionapp.core.PageStatusManager;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.bean.capInfo;
import com.example.odm.securitydetectionapp.module.watch.contract.watchContract;
import com.example.odm.securitydetectionapp.module.watch.presenter.watchPresenter;
import com.example.odm.securitydetectionapp.util.GsonUtil;
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
 * 监控页面 View 层
 *
 * author: ODM
 * date: 2019/7/25
 */
public class watchFragment<P extends IBasePresenter> extends BaseFragment<watchPresenter> implements watchContract.View {

    private static final String TAG = "watchFragment";
    @BindView(R.id.status)
    StatusView view_Status;
    @BindView(R.id.tb_home)
    CommonTitleBar tb_Home;
    @BindView(R.id.rv_module)
    RecyclerView rv_Module;
    private capInfoAdapter mAdapter;
    private List<capInfo> mCapList;
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
        ButterKnife.bind(this, view);
        initViews();
        initToolbar();
        return view;
    }


    private void initViews() {
        builder  = new MaterialDialog.Builder(getContext());
        mCapList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_Module.setLayoutManager(layoutManager);
        mAdapter = new capInfoAdapter(R.layout.item_cap ,mCapList);
        rv_Module.setAdapter(mAdapter);
        //列表子项点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //如果子模块的属性为异常，则会呼出一个信息弹窗
                String errorData = mAdapter.getData().get(position).getData();
                if(! "".equals(errorData)){
                    showSimpleTipDialog(position , errorData);
                }
            }
        });
        //滚动列表子项长按事件
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                String errorData = mAdapter.getData().get(position).getData();
                //非异常状态不能发送反馈
                if(! "".equals(errorData)) {
                    //触发效果：长按呼出弹窗，点击确认按钮后发送反馈
                    showSimpleConfirmDialog(position , mAdapter.getData().get(position).getAddress());
                    //触发效果:长按呼出跟随子项的按钮列表，列表，列表
//                    View itemview = rv_Module.getChildAt(position);
//                    showCallButton(itemview , mAdapter.getData().get(position).getAddress());
                }
                return false;
            }
        });
        //为Adaper加载空布局
        getAdapter().setEmptyView(new emptyView(getContext() ,null));
        showLoading();
        //手动点击可以关闭 "连接失败"弹窗
        view_Status.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_Status.dismiss();
            }
        });
    }

    /**
     * 状态布局 设置为 连接中
     */
    @Override
    public void showLoading() {
        super.showLoading();

        view_Status.setStatus(Status.LOADING);

    }

    /**
     * 隐藏 状态布局
     */
    @Override
    public void hideLoading() {
        super.hideLoading();
        view_Status.dismiss();
    }

    private void initToolbar() {
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

    @Subscribe(threadMode = ThreadMode.MAIN ,sticky = true)
    @Override
    public void handleEvent(BaseEvent baseEvent) {
        super.handleEvent(baseEvent);
        //对 事件类型为 请求状态 处理事件
        //监控页面出现的模块与定位功能的模块是同一个的，同时正常和异常
        if (Constant.STATUS.equals(baseEvent.type)) {
            if (view_Status != null) {

                switch (baseEvent.content) {

                    case Constant.SUCCESS:
                        if (view_Status.getStatus() == Status.LOADING) {
                            hideLoading();
                            view_Status.setStatus(Status.COMPLETE);
                        }
                        break;
                    case Constant.FAILURE:
                        if (view_Status.getStatus() != Status.ERROR) {
                            hideLoading();
                            view_Status.setStatus(Status.ERROR);
                        }
                        break;
                    default:
                }
            } else {
                Logger.d("监控页面状态栏还没初始化");
            }
        }
            if (Constant.CAP.equals(baseEvent.type)) {
                //子模块信息到了，要进行处理，把它加入列表里面
                if (PageStatusManager.getPageStatus() == PageStatusManager.PAGE_WATCH_CURRENT) {
                if (baseEvent.content.startsWith("嵌")) {
                        CookieBar.builder(getActivity())
                                .setTitle("嵌入式设备已下线")
                                .setIcon(R.mipmap.warning_yellow)
                                .setMessage("子模块信息初始化")
                                .setLayoutGravity(Gravity.BOTTOM)
                                .setAction(R.string.known, null)
                                .show();
                        getPresenter().handleModuleOffline(getAdapter());
                    } else {
                        if (getAdapter() != null) {
                            //准备将子模块加入列表中
                            getPresenter().checkCapInfo(getAdapter());
                        } else {
                            Logger.d("adapter还没初始化" + "消息为" + baseEvent.content);
                        }
                    }
                }
            }

    }

    @Override
    protected void lazyLoadData() {
        //需要每次进入页面才加载的内容

        if(getAdapter() != null ) {
            getPresenter().checkCapInfo(getAdapter());
            Logger.d("懒加载监控页面数据");
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
                .content("当前服务器地址: " + "\n" + SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK,"等待连接服务器ing") + "\n您确定要切换吗?")
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
                        if (! "".equals(newUrl) && newUrl.startsWith("w")) {
                            getPresenter().switchWebSocket(newUrl, getAdapter());
                            showLoading();
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
    private void showSimpleTipDialog(int position ,String  warnString) {
//        new MaterialDialog.Builder(getContext())
                builder.iconRes(R.mipmap.warning_yellow)
                .title(R.string.warning)
                .content(warnString)
                .positiveText(R.string.known)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                getPresenter().handledAbnormalModule(position , getAdapter());
                            }
                        })
                .negativeText("")
                .show();
    }



    /**
     * 简单的确认对话框
     */
    public void showSimpleConfirmDialog(int position ,String address) {
//        new MaterialDialog.Builder(getContext())
                builder.content("当前选中模块地址为：" + address)
                .title("发送反馈")
                .positiveText("确认发送")
                .negativeText("取消发送")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getPresenter().sendCallBack(address);
                        getPresenter().handledAbnormalModule(position , getAdapter());
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
