package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseFragment;
import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.PointManager;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.module.map_location.contract.locationContract;
import com.example.odm.securitydetectionapp.module.map_location.presenter.locationPresenter;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.logger.Logger;
import com.tuyenmonkey.mkloader.MKLoader;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * The type Location fragment.
 *
 * @param <P> the type parameter
 * @author: ODM
 * @date: 2019 /7/26
 */
public class locationFragment<P extends IBasePresenter> extends BaseFragment<locationPresenter> implements locationContract.View {

    @BindView(R.id.loading)
    MKLoader loadingbar;
    @BindView(R.id.rmv_locate)
    RadarMapView rmv_Locate;
    @BindView(R.id.tb_location)
    CommonTitleBar tbLocation;
    @BindView(R.id.iv_background_location)
    ImageView iv_background;
    @BindView(R.id.view_normal)
    normalMarkerView marker_normal;
    @BindView(R.id.fab_loading)
    FloatingActionButton fabLoading;
    @BindView(R.id.fab_switch)
    FloatingActionButton fabSwitch;
    @BindView(R.id.fab_loaction_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.fab_clearAll)
    FloatingActionButton fabClearAll;
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
        return view;
    }

    /**
     * Init views.
     */
     private void initViews() {
        if (checkMarkerStatus()) {
            marker_normal.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 悬浮按钮的点击事件监听
     *
     * @param v the v
     */
    @OnClick({R.id.fab_loading, R.id.fab_switch, R.id.fab_clearAll})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_loading:
                selectBackGround();
                break;
            case R.id.fab_clearAll:
                marker_normal.clearAllMarker();
                break;
            case R.id.fab_switch:
                if (imageUri == null) {
                    ToastUtil.showLongToastCenter("\r当前未设置背景图，无法开启标注模式!");
                    break;
                }
                if (!isEditted) {
                    //准备开启标注模式
                    marker_normal.setEditted(true);
                    if (marker_normal.getVisibility() == View.INVISIBLE) {
                        marker_normal.setVisibility(View.VISIBLE);
                    }
                    fabSwitch.setLabelText("关闭标注模式");
                    isEditted = true;
                } else {
                    //准备关闭标注模式
                    marker_normal.setEditted(false);
                    fabSwitch.setLabelText("开启标注模式");
                    isEditted = false;
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
        if (baseEvent != null && Constant.CAP.equals(baseEvent.type)) {
            getPresenter().handleCallBack(baseEvent.content);
        }
    }

    @Override
    public locationPresenter onBindPresenter() {
        return new locationPresenter(this);
    }

    @Override
    protected void lazyLoadData() {
        //每次重新进入此页面才加载的内容
        if (imageUri != null) {
            displayImage(imageUri);
            hideLoading();
        } else {
            showLoading();
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        if(loadingbar.getVisibility() != View.VISIBLE){
            Logger.d("启动Loader");
            loadingbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if(loadingbar.getVisibility() != View.GONE){
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
                        deletePicture(imagePath,getContext());
                        Logger.d("再次执行文件删除");
                    }
                    if (imageUri != null) {
                        intent_gallery_crop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent_gallery_crop.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    }
                    startActivityForResult(intent_gallery_crop, CROP_PHOTO);
                    break;
                case CROP_PHOTO:
                    // 裁剪图片
                    try {
                        if (imageUri != null) {
                            displayImage(imageUri);
                            hideLoading();
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
     * 定位信息添加进异常列表后的调用方法
     * 重绘制页面
     */
    @Override
    public void updateAbnormalList() {

        marker_normal.invalidate();
    }

    /**
     * 创建File保存图片
     */
    private void createImageFile() {
        try {
            if (imageFile != null && imageFile.exists()) {
                imageFile.delete();
                imageFile.deleteOnExit();
                Logger.d("执行文件删除");
                deletePicture(imagePath,getContext());
            }
            // 新建文件
            imageName = System.currentTimeMillis()+"background.jpg";
            imageFile = new File(Environment.getExternalStorageDirectory(), imageName);
            imagePath = "/storage/emulated/0/"+imageName;
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
                    // 占位图设置：加载过程中显示的图片
                    .placeholder(R.mipmap.ic_launcher_round)
                    // 异常占位图
                    .error(R.mipmap.ic_launcher_round)
                    .transform(new CenterCrop())
                    .into(iv_background);
        } catch (Exception e) {
            e.printStackTrace();
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
    public  void deletePicture(String localPath, Context context) {
        if(!TextUtils.isEmpty(localPath)){
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
                if(!file.exists()) {
                    Logger.d(imageName+"已经为空");
                }
            }
        }
    }


    /**
     * Check marker status boolean.
     *
     * @return the boolean
     */
    public boolean checkMarkerStatus() {
        if (PointManager.getPointList().size() > 0) {
            //normalMarkerView会在画图时将坐标存PointManger,如果size大于0
            //说明已经开标注模式，标注过点了，那么已经在加载页面时将上次离开页面时的点加载出来
            return true;
        } else {
            return false;
        }
    }
}
