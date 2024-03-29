package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseFragment;
import com.example.odm.securitydetectionapp.bean.LocateInfo;
import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.PageStatusManager;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.module.map_location.contract.locationContract;
import com.example.odm.securitydetectionapp.module.map_location.presenter.locationPresenter;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.orhanobut.logger.Logger;
import com.tuyenmonkey.mkloader.MKLoader;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * The type Location fragment.
 *
 * @param <P> the type parameter
 * @author: ODM
 * @date: 2019 /7/26
 */
public class locationFragment<P extends IBasePresenter> extends BaseFragment<locationPresenter> implements locationContract.View {


    private static final String TAG = "locationFragment";

    @BindView(R.id.loading)
    MKLoader loadingbar;
    @BindView(R.id.rmv_locate)
    RadarMapView rmv_Locate;
    @BindView(R.id.tb_location)
    CommonTitleBar tbLocation;
    @BindView(R.id.iv_background_location)
    ImageView iv_background;
    @BindView(R.id.view_ModuleOnStation)
    ModuleOnStationView moduleOnStationView;
    @BindView(R.id.fab_load_background)
    FloatingActionButton fabLoading;
    @BindView(R.id.fab_open_location)
    FloatingActionButton fabOpenLocation;
    @BindView(R.id.fab_loaction_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.fab_clearAll)
    FloatingActionButton fabClearAll;
    @BindView(R.id.tv_show_location_info)
    TextView tvShowLocationInfo;

    private static final int REQUEST_CODE_GALLERY = 0x10;           // 图库选取图片标识请求码
    private static final int CROP_PHOTO = 0x12;                     // 裁剪图片标识请求码
    private static final int STORAGE_PERMISSION = 0x20;              // 动态申请存储权限标识

    private File imageFile = null;                                  // 声明File对象
    private Uri imageUri = null;                                    // 裁剪后的图片uri
    private String imagePath = "";
    private String imageName = "";
    //控制切换标注模式的变量，true为正在编辑，false为当前不可编辑
    private boolean isEditted;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStoragePermission();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        initViews();
        handleLiveEvent();
        return view;
    }


    /**
     * 初始化自定义View 模块可视化View先隐藏起来
     */
    private void initViews() {
        moduleOnStationView.setVisibility(View.GONE);
    }

    /**
     * 悬浮按钮菜单 按钮点击事件监听
     *
     * @param v 控件
     */
    @OnClick({R.id.fab_load_background, R.id.fab_open_location, R.id.fab_clearAll})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_load_background:
                //用户选择加载背景图
                selectBackGround();
                break;
            case R.id.fab_clearAll:
                Log.e(TAG, "onClick: 清空标记操作 已被废弃");
                break;
            case R.id.fab_open_location:

                //点击了 开启定位功能后，第二次点击 应该是 关闭定位功能
                if (getResources().getString(R.string.open_location).equals(fabOpenLocation.getLabelText())) {
                    //此时按钮文字为 开启定位功能
                    if (imageUri == null) {
                        ToastUtil.showLongToastCenter("当前暂未设置背景图，无法开启定位功能");
                        break;
                    } else {
                        //显示 当前的 基站和模块的位置
                        ToastUtil.showLongToastBottom("正在开启模块定位功能......");
                        tvShowLocationInfo.setVisibility(View.VISIBLE);
                        moduleOnStationView.setVisibility(View.VISIBLE);
                        moduleOnStationView.invalidate();
                    }
                    fabOpenLocation.setLabelText(getResources().getString(R.string.close_location));

                } else {
                    //此时按钮文字为 关闭定位功能
                    ToastUtil.showLongToastBottom("正在关闭模块定位功能......");
                    moduleOnStationView.setVisibility(View.GONE);
                    tvShowLocationInfo.setVisibility(View.GONE);
                    fabOpenLocation.setLabelText(getResources().getString(R.string.open_location));
                }
                break;
            default:
        }
        fabMenu.toggle(false);
    }

    //注册绑定EventBus
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void handleEvent(BaseEvent baseEvent) {
        super.handleEvent(baseEvent);
//        if (PageStatusManager.getPageStatus() == PageStatusManager.PAGE_LOCATION_CURRENT) {
//            if(Constant.CAP.equals(baseEvent.type)) {
//                if (baseEvent.content.startsWith("嵌")) {
//                    showOfflineBar();
//                } else {
//
//                    marker_normal.invalidate();
//                }
//            }
//        }
//        if(PageStatusManager.getPageStatus() == PageStatusManager.PAGE_LOCATION_CURRENT) {
//            if (baseEvent != null && Constant.CAP.equals(baseEvent.type)) {
//                getPresenter().handleCallBack(baseEvent.content);
//            }
//        }
    }

    /**
     * 处理 LiveEvent 事件
     */
    private void handleLiveEvent() {
        //接收到了 定位信息处理 的事件 ，处理它: 将定位可视化--自定义View
        LiveEventBus
                .get(Constant.LOCATION, String.class)
                .observe(this, content -> {
                    if (PageStatusManager.getPageStatus() == PageStatusManager.PAGE_LOCATION_CURRENT) {
                        getPresenter().handleLocationInfo(content);
                    }
                });
        //接收到了 嵌入式设备下线 的事件 ，处理它: 弹出离线的通知,关闭定位功能的可视化，修改开启定位按钮的标题
        LiveEventBus
                .get(Constant.OFFLINE, String.class)
                .observe(this, content -> {
                    if (PageStatusManager.getPageStatus() == PageStatusManager.PAGE_WATCH_CURRENT) {
                        showOfflineBar();
                    }
                    moduleOnStationView.setVisibility(View.GONE);
                    tvShowLocationInfo.setVisibility(View.GONE);
                    fabOpenLocation.setLabelText(getResources().getString(R.string.open_location));
                });

    }


    @Override
    public void moduleLocationChanged(LocateInfo locateInfo) {
        if (moduleOnStationView != null) {
            //为自定义View 设置定位信息数据源
            moduleOnStationView.calculateLocationData(locateInfo);
            showLocationInfoText(locateInfo);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showLocationInfoText(LocateInfo locateInfo) {
        if(tvShowLocationInfo != null) {
            tvShowLocationInfo.setText("基站A0到基站A1的距离:  "+locateInfo.getStationA0ToStationA1()+"\n"
                                        .concat("基站A1到基站A2的距离:  "+locateInfo.getStationA1ToStationA2()+"\n"
                                        .concat("基站A0到基站A2的距离:  "+locateInfo.getStationA0ToStationA2()+"\n"
                                        .concat("模块到基站A0的距离:    "+locateInfo.getTagToStationA0())+"\n"
                                        .concat("模块到基站A1的距离:    "+locateInfo.getTagToStationA1()+"\n"
                                        .concat("模块到基站A2的距离:    "+locateInfo.getTagToStationA2() )))));
        }
    }


    @Override
    public locationPresenter onBindPresenter() {
        return new locationPresenter(this);
    }

    @Override
    protected void lazyLoadData() {
        //每次重新进入此页面才加载的内容

        //加载SharedPrefences里的图片名，加载页面背景
        imageName = SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.IMAGENAME, "");
        if (imageUri != null) {
            displayImage(imageUri);
            hideLoading();
        } else if (!"".equals(imageName)) {
            imageFile = new File(Environment.getExternalStorageDirectory(), imageName);
            imageUri = Uri.fromFile(imageFile);
            displayImage(imageUri);
            hideLoading();
        } else {
            showLoading();
        }
    }


    @Override
    public void showLoading() {
        super.showLoading();
        if (loadingbar.getVisibility() != View.VISIBLE) {
            loadingbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (loadingbar.getVisibility() != View.GONE) {
            loadingbar.setVisibility(View.GONE);
        }
    }

    /**
     * Select back ground.
     * 用户选择本地图片设置成背景
     */
    private void selectBackGround() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    /**
     * 接收startActivityForResult(Intent, int)调用的结果
     *
     * @param requestCode 请求码 识别这个结果来自谁
     * @param resultCode  结果码
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // 操作完成了
            switch (requestCode) {
                // 图库选择图片
                case REQUEST_CODE_GALLERY:
                    // 获取图片的uri
                    Uri uri = data.getData();
                    Intent intent_gallery_crop = new Intent("com.android.camera.action.CROP");
                    intent_gallery_crop.setDataAndType(uri, "image/*");
                    // 设置裁剪
                    intent_gallery_crop.putExtra("crop", "true");
                    intent_gallery_crop.putExtra("scale", true);
                    // aspectX aspectY 是宽高的比例
                    intent_gallery_crop.putExtra("aspectX", 1);
                    intent_gallery_crop.putExtra("aspectY", 1.5);
                    // outputX outputY 是裁剪图片宽高,无需指定的话，Glide会自动将裁剪图片缩放显示
//                    intent_gallery_crop.putExtra("outputX",300);
//                    intent_gallery_crop.putExtra("outputY", 300);
                    intent_gallery_crop.putExtra("return-data", false);
                    // 创建文件保存裁剪的图片
                    createImageFile();
                    if (imageFile != null) {
                        imageUri = Uri.fromFile(imageFile);
                        //把裁剪后的图片从本地删除掉，原图不变
//                        deletePicture(imagePath,getContext());
//                        Logger.d("再次执行文件删除");
                    }
                    if (imageUri != null) {
                        intent_gallery_crop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent_gallery_crop.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    }
                    startActivityForResult(intent_gallery_crop, CROP_PHOTO);
                    break;
                case CROP_PHOTO:
                    // 裁剪图片后的回调
                    try {
                        if (imageUri != null && !"".equals(imageName)) {
                            displayImage(imageUri);
                            hideLoading();
                            saveImage(imageName);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        }
    }


    /**
     * 创建File保存图片
     */
    private void createImageFile() {
        try {
            if (imageFile != null && imageFile.exists()) {
                imageFile.delete();
                imageFile.deleteOnExit();
//                Logger.d("执行文件删除");
//                deletePicture(imagePath,getContext());
            }
            // 新建一张图片文件
            imageName = TimeUtil.transformToString(System.currentTimeMillis()) + "SecurityDetectionAPPBackground.jpg";
            imageFile = new File(Environment.getExternalStorageDirectory(), imageName);
            imagePath = "/storage/emulated/0/" + imageName;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示图片
     *
     * @param imageUri 图片的uri
     */
    private void displayImage(Uri imageUri) {
        try {
            // glide根据图片的uri加载图片
            Glide.with(this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                     占位图设置：加载过程中显示的图片
//                    .placeholder(R.mipmap.ic_map_load)
                    // 异常占位图
                    .error(R.mipmap.ic_launcher_round)
                    .transform(new CenterCrop())
                    .into(iv_background);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveImage(String imageName) {
        if (getPresenter().saveImage(imageName)) {
            Logger.d("存储图片成功");
            ToastUtil.showShortToastBottom("保存图片成功");
        } else {
            ToastUtil.showShortToastBottom("保存图片失败");
        }

    }


    /**
     * Android6.0后需要动态申请危险权限
     * 动态申请存储权限
     */
    private void requestStoragePermission() {

        int hasCameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 拥有权限，可以执行涉及到存储权限的操作

        } else {
            // 没有权限，向用户申请该权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            }
        }

    }

    /**
     * 动态申请权限的结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意，执行相应操作
                Logger.d("用户已经同意了存储权限");
            } else {
                // 用户不同意，向用户展示该权限作用
                Logger.d("用户不同意存储权限");
            }
        }

    }


    /**
     * 删除本地管理器指定图片（目前无效）
     *
     * @param localPath the local path
     * @param context   the context
     */
    public void deletePicture(String localPath, Context context) {
        if (!TextUtils.isEmpty(localPath)) {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            String url = MediaStore.Images.Media.DATA + "=?";
            int deleteRows = contentResolver.delete(uri, url, new String[]{localPath});
            if (deleteRows == 0) {
                //当生成图片时没有通知(插入到）媒体数据库，那么在图库里面看不到该图片，而且使用contentResolver.delete方法会返回0，此时使用file.delete方法删除文件
                File file = new File(localPath);
                if (file.exists()) {
                    file.delete();
                }
                if (!file.exists()) {
                    Logger.d(imageName + "已经为空");
                }
            }
        }
    }

}
