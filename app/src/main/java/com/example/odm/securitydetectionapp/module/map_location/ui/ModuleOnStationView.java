package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.bean.LocateInfo;
import com.example.odm.securitydetectionapp.core.CapModuleInfoManager;
import com.example.odm.securitydetectionapp.core.PointManager;
import com.example.odm.securitydetectionapp.util.CoordinateCalculateUtil;
import com.orhanobut.logger.Logger;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: ODM
 * @date: 2019/7/30
 */
public class ModuleOnStationView extends View {

    private Bitmap bmp_normal;
    private Bitmap bmp_abnormal;
    private Bitmap bmp_base_station;
    private int mWidth, mHeight;
    float[] baseStation3Th;
    float[] moduleCoodinate;
    //三个基站的坐标
    float baseStation_1_x;
    float baseStation_1_y;
    float baseStation_2_x;
    float baseStation_2_y;
    float baseStation_0_x ;
    float baseStation_0_y ;
    float baseStation_distance_01;
    float baseStation_distance_12;
    float baseStation_distance_02;
    float tagToStationA0;
    float tagToStationA1;
    float tagToStationA2;
    //模块的具体坐标
    float module_x ;
    float module_y ;
    boolean isLocate;

//    //包含所有跟定位有关的数据 的实体类对象，由LocationFragment提供
//    LocateInfo locateInfo;

    Paint mPaint;
    Paint mPaint_red;
    Paint mPaintLine;
    private static final String TAG = "normalMarkerView";

    float offY;


    public ModuleOnStationView(Context context) {
        this(context, null);
    }

    public ModuleOnStationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        init();
    }

    /**
     * 初始化画笔
     */
    public void initPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(42);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint_red = new Paint();
        mPaint_red.setStrokeWidth(10);
        mPaint_red.setTextSize(42);
        mPaint_red.setTextAlign(Paint.Align.CENTER);
        mPaintLine = new Paint(0);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(10f);
        mPaintLine.setColor(getResources().getColor(R.color.purple));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    //初始化需要显示的光标样式，会在页面初始化时执行一次
    private void init() {
        if (bmp_normal == null || bmp_abnormal == null || bmp_base_station == null) {
            bmp_normal = BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_marker_normal);
            bmp_abnormal = BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_marker_abnormal);
            bmp_base_station = BitmapFactory.decodeResource(getResources() ,R.mipmap.warning_yellow);
        }
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        offY = fontTotalHeight / 2 - fontMetrics.bottom;
    }

    @Override
    public void onDraw(Canvas canvas) {

        //用背景图是否存在阻隔了自动画的点
        if (bmp_normal != null && bmp_abnormal != null && bmp_base_station != null) {
                if(isLocate) {
                    // 画上基站的标记
                    canvas.drawBitmap(bmp_base_station, baseStation_0_x, baseStation_0_y, mPaint);
                    //如果让第二个基站默认在屏幕右顶点会被挡住，所以要左平移一点
                    canvas.drawBitmap(bmp_base_station, baseStation_1_x - bmp_base_station.getWidth(), baseStation_1_y, mPaint);
                    canvas.drawBitmap(bmp_base_station, baseStation_2_x - (float) bmp_base_station.getWidth() / 2, baseStation_2_y - (float) bmp_base_station.getHeight() / 2, mPaint);
                    //将三个基站连线,为了美观，调整了位置
                    canvas.drawLine(baseStation_0_x + (float) bmp_base_station.getWidth() / 2, baseStation_0_y + (float) bmp_base_station.getHeight() / 2, baseStation_1_x - (float) bmp_base_station.getWidth() / 2, baseStation_1_y + (float) bmp_base_station.getHeight() / 2, mPaintLine);
                    canvas.drawLine(baseStation_0_x + (float) bmp_base_station.getWidth() / 2, baseStation_0_y + (float) bmp_base_station.getHeight() / 2, baseStation_2_x, baseStation_2_y, mPaintLine);
                    canvas.drawLine(baseStation_1_x - (float) bmp_base_station.getWidth() / 2, baseStation_1_y + (float) bmp_base_station.getHeight() / 2, baseStation_2_x, baseStation_2_y, mPaintLine);

                    //根据模块转换后的坐标画出模块，若当前模块信息为异常，则画出异常的模块样式，否则画出正常样式
                    if (CapModuleInfoManager.getCapInfoList().size() > 0) {
                        if ("".equals(CapModuleInfoManager.getCapInfoList().get(0).getData())) {
                            canvas.drawBitmap(bmp_normal, module_x - (float) bmp_normal.getWidth() / 2, module_y - (float) bmp_normal.getHeight() / 2, mPaint);
                        } else {
                            canvas.drawBitmap(bmp_abnormal, module_x - (float) bmp_normal.getWidth() / 2, module_y - (float) bmp_normal.getHeight() / 2, mPaint);
                        }
                    }
                }

        }
        super.onDraw(canvas);
    }




//    public LocateInfo getLocateInfo() {
//        return locateInfo;
//    }
//
//    public void setLocateInfo(LocateInfo locateInfo) {
//        this.locateInfo = locateInfo;
//        calculateLocationData(locateInfo);
//    }

    public void calculateLocationData(LocateInfo locateInfo) {
        if(locateInfo != null) {
            //初始化第三个基站在屏幕坐标系的坐标 ，将基站坐标和基站距离折算成屏幕坐标系
            baseStation_0_x = 0;
            baseStation_0_y = 0;
            baseStation_1_x = mWidth;
            baseStation_1_y = 0;
            baseStation_distance_01 = (float) CoordinateCalculateUtil.realConvertScreen(locateInfo.getStationA0ToStationA1(),locateInfo.getStationA0ToStationA1(),mWidth);
            baseStation_distance_12 = (float) CoordinateCalculateUtil.realConvertScreen(locateInfo.getStationA1ToStationA2() , locateInfo.getStationA0ToStationA1(),mWidth);
            baseStation_distance_02 = (float)CoordinateCalculateUtil.realConvertScreen(locateInfo.getStationA0ToStationA2() ,locateInfo.getStationA0ToStationA1(),mWidth);
            //输入已换算的坐标--得到第三个基站已换算的坐标
            baseStation3Th =  CoordinateCalculateUtil.calcaulate3ThPoint
                                (baseStation_distance_12, baseStation_distance_02, baseStation_distance_01,
                                baseStation_0_x ,baseStation_0_y,
                                baseStation_1_x,baseStation_1_y );
            baseStation_2_x = baseStation3Th[0];
            baseStation_2_y = baseStation3Th[1];

            //将 模块与三个基站的距离转换为 屏幕坐标系
            tagToStationA0 = (float) CoordinateCalculateUtil.realConvertScreen(locateInfo.getTagToStationA0(),locateInfo.getStationA0ToStationA1(),mWidth);
            tagToStationA1 = (float) CoordinateCalculateUtil.realConvertScreen(locateInfo.getTagToStationA1(),locateInfo.getStationA0ToStationA1(),mWidth);
            tagToStationA2 = (float) CoordinateCalculateUtil.realConvertScreen(locateInfo.getTagToStationA2(),locateInfo.getStationA0ToStationA1(),mWidth);
            //利用三边算法，计算出 模块 在手机屏幕坐标系的坐标
            moduleCoodinate  = CoordinateCalculateUtil.trilateration(baseStation_0_x,baseStation_0_y,tagToStationA0,
                    baseStation_1_x,baseStation_1_y,tagToStationA1,
                    baseStation_2_x ,baseStation_2_y ,tagToStationA2);
            module_x = moduleCoodinate[0];
            module_y = moduleCoodinate[1];
            Logger.d("屏幕坐标：  第三个基站坐标  x： "+ baseStation_2_x+"    y: "+ baseStation_2_y+"   模块坐标 x： "+module_x+"   y: "+module_y);
        }

        //数据出现较大波动会导致计算出的坐标为 NaN ，避免显示这种错误情况,暂时用这种简单方法规避
        if(!Float.isNaN(module_x) && ! Float.isNaN(module_y)) {
            isLocate = true;
            //根据转换好的坐标，画出来
            invalidate();
        } else {

            isLocate = true;
        }
    }





    /**
     * 已废弃！！！
     *
     * 清空地图页面所有的标记
     */
    public void  clearAllMarker() {
            invalidate();
        }

    public static List<String> removeDuplicate(List<String> list)
    {
        Set<String> set  = new LinkedHashSet<> (list);
        list.clear();
        list.addAll(set);
        return list;
    }


}

