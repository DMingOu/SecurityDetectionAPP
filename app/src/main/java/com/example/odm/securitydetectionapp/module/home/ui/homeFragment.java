package com.example.odm.securitydetectionapp.module.home.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.application.SecurityDetectionAPP;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseView;
import com.example.odm.securitydetectionapp.common.Constant;

import com.example.odm.securitydetectionapp.common.emptyView;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.module.home.bean.callBackInfo;
import com.example.odm.securitydetectionapp.module.home.bean.capInfo;
import com.example.odm.securitydetectionapp.module.home.contract.homeContract;
import com.example.odm.securitydetectionapp.module.home.presenter.homePresenter;
import com.example.odm.securitydetectionapp.util.GsonUtil;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
import com.xuexiang.xui.widget.popupwindow.status.Status;
import com.xuexiang.xui.widget.popupwindow.status.StatusView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class homeFragment<P extends IBasePresenter> extends BaseView<homePresenter> implements homeContract.View {


    @BindView(R.id.status)
    StatusView view_Status;
    @BindView(R.id.tb_home)
    CommonTitleBar tb_Home;
    Unbinder unbinder;
    @BindView(R.id.rv_module)
    RecyclerView rv_Module;

    private capInfoAdapter mAdapter;
    private List<capInfo> mCapList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        Logger.d("onCreateView");
        initViews();

        for(int i = 0 ; i < 10; i ++) {

            getPresenter().checkCapInfo("{\"address\":\"010A\",\"data\":gas\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"020A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"030A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"040A\",\"data\":334\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"050A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"050A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"060A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"070A\",\"data\":dsadas\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"080A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"090A\",\"data\":asad\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"100A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"110A\",\"data\":sdsa\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"120A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"130A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"140A\",\"data\":das\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"150A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"160A\",\"data\":das\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"170A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"180A\",\"data\":\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"180A\",\"data\":sad\"\",\"status\":true}" ,getAdapter());
            getPresenter().checkCapInfo("{\"address\":\"200A\",\"data\":\"\",\"status\":true}" ,getAdapter());
        }
        initToolbar();
        return view;
    }


    public void initViews() {

        mCapList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_Module.setLayoutManager(layoutManager);
        mAdapter = new capInfoAdapter(R.layout.item_cap ,mCapList);
        rv_Module.setAdapter(mAdapter);
        Logger.d("adapter初始化了" + TimeUtil.showCurrentTime(System.currentTimeMillis()));
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
                    showSimpleConfirmDialog(mAdapter.getData().get(position).getAddress());
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
                //弹出一个可输入弹窗，用于切换服务器
                showSwitchDialog();
            }
        });
    }


    @Override
    public homePresenter onBindPresenter() {
        return new homePresenter(this);
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
                        Logger.d("WebSocket连接失败" + TimeUtil.showCurrentTime(System.currentTimeMillis()));
                        Logger.d("连接失败的url:" + SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK));
                        break;
                    default:
                }
            }
        }
        if (baseEvent != null && Constant.CAP.equals(baseEvent.type) && getAdapter()!= null) {
            //子模块信息到了，要进行处理，把它加入列表里面
            if(baseEvent.content.startsWith("嵌")) {

                CookieBar.builder(getActivity())
                        .setTitle("嵌入式设备已下线")
                        .setIcon(R.mipmap.warning_yellow)
                        .setMessage("子模块信息初始化")
                        .setLayoutGravity(Gravity.BOTTOM)
                        .setAction(R.string.known, null)
                        .show();
            }

            //准备将子模块加入列表中
              getPresenter().checkCapInfo(baseEvent.content  , getAdapter() );
            } else {
                Logger.d("adapter还没初始化"+ "消息为"+ baseEvent.content);
            }
        }

    @Override
    protected void lazyLoadData() {
        //需要每次进入页面才加载的内容

    }

    @Override
    public capInfoAdapter getAdapter() {
        return this.mAdapter;
    }

    /**
     * 弹出输入对话框，切换服务器.
     */
    public void showSwitchDialog() {
        new MaterialDialog.Builder(getContext())
                .iconRes(R.mipmap.warning_yellow)
                .title("提示")
                .content("当前服务器地址: " + "\n" + SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK) + "    确定要切换吗")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
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
                .positiveText(R.string.lab_continue)
                //左下角的按钮--取消按钮
                .negativeText(R.string.lab_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newUrl = dialog.getInputEditText().getText().toString();
                        Logger.d(newUrl);
                        if (! "".equals(newUrl)) {
                            getPresenter().switchWebSocket(newUrl, getAdapter());
                        } else {
                            ToastUtil.showShortToastCenter("你输入的服务器地址为空");
                        }
                    }
                })
                .cancelable(true)
                .show();
    }

    /**
     * 简单的提示性对话框
     */
    private void showSimpleTipDialog(String  warnString) {
        new MaterialDialog.Builder(getContext())
                .iconRes(R.mipmap.warning_red)
                .title(R.string.warning)
                .content(warnString)
                .positiveText(R.string.known)
                .show();
    }



    /**
     * 简单的确认对话框
     */
    private void showSimpleConfirmDialog(String address) {
        new MaterialDialog.Builder(getContext())
                .content("当前选中模块地址为：" + address)
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


}
