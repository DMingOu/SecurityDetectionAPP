package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.core.CapModuleInfoManager;
import com.example.odm.securitydetectionapp.core.PointManager;
import com.example.odm.securitydetectionapp.util.CoordinateCalculateUtil;
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
    float baseStation_3_x = 100;
    float baseStation_3_y = 100;
    float baseStation_distance_12;
    float baseStation_distance_13;
    float baseStation_distance_23;
    //模块的坐标
    float module_x = 100f;
    float module_y = 750f;
    float module_distance_1;
    float module_distance_2;
    float module_distance_3;
    Paint mPaint;
    Paint mPaint_red;
    Paint mPaintLine;
    private static final String TAG = "normalMarkerView";
    float offY;
    MaterialDialog.Builder builder;
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
        builder  = new MaterialDialog.Builder(getContext());
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
            //初始化第三个基站在屏幕坐标系的坐标 ，将基站坐标和基站距离折算成屏幕坐标系
            baseStation_1_x = 0;
            baseStation_1_y = 0;
            baseStation_2_x = mWidth;
            baseStation_2_y = 0;
            baseStation_distance_12 = (float) CoordinateCalculateUtil.realConvertScreen(PointManager.getBaseStation().getRelativePathAB(),PointManager.getBaseStation().getRelativePathAB(),mWidth);
            baseStation_distance_13 = (float) CoordinateCalculateUtil.realConvertScreen(PointManager.getBaseStation().getRelativePathAC() , PointManager.getBaseStation().getRelativePathAB(),mWidth);
            baseStation_distance_23 = (float)CoordinateCalculateUtil.realConvertScreen(PointManager.getBaseStation().getRelativePathBC() ,PointManager.getBaseStation().getRelativePathAB(),mWidth);
            if(PointManager.getBaseStation() != null) {
                //输入未换算的坐标--得到第三个基站未换算的坐标
                baseStation3Th =  CoordinateCalculateUtil.calcaulate3ThPoint
                        (baseStation_distance_23, baseStation_distance_13, baseStation_distance_12
                                ,baseStation_1_x ,baseStation_1_y,
                                baseStation_2_x,baseStation_2_y );
            }
            baseStation_3_x = (float)baseStation3Th[0];
            baseStation_3_y = (float)baseStation3Th[1];

            if(bmp_base_station != null && bmp_normal != null) {
                // 画上基站的标记
                canvas.drawBitmap(bmp_base_station , baseStation_1_x, baseStation_1_y, mPaint);
                //如果让第二个基站默认在屏幕右顶点会被挡住，所以要左平移一点
                canvas.drawBitmap(bmp_base_station ,baseStation_2_x - bmp_base_station.getWidth() ,baseStation_2_y ,mPaint);
                canvas.drawBitmap(bmp_base_station ,baseStation_3_x - bmp_base_station.getWidth()/2, baseStation_3_y-bmp_base_station.getHeight()/2,mPaint);
                //将三个基站连线,为了美观，调整了位置
                canvas.drawLine(baseStation_1_x + bmp_base_station.getWidth()/2,baseStation_1_y+bmp_base_station.getHeight()/2,baseStation_2_x - bmp_base_station.getWidth() /2,baseStation_2_y + bmp_base_station.getHeight() /2,mPaintLine);
                canvas.drawLine(baseStation_1_x+ bmp_base_station.getWidth()/2,baseStation_1_y+bmp_base_station.getHeight()/2 ,baseStation_3_x,baseStation_3_y,mPaintLine);
                canvas.drawLine(baseStation_2_x- bmp_base_station.getWidth() /2 ,baseStation_2_y+bmp_base_station.getHeight() /2,baseStation_3_x,baseStation_3_y,mPaintLine);
                //画出模块的标记
//                module_distance_1 = (float) CoordinateCalculateUtil.realConvertScreen(132.2859,PointManager.getBaseStation().getRelativePathAB(),mWidth);
//                module_distance_2 = (float) CoordinateCalculateUtil.realConvertScreen(86.6 , PointManager.getBaseStation().getRelativePathAB(),mWidth);
//                module_distance_3 = (float)CoordinateCalculateUtil.realConvertScreen(50,PointManager.getBaseStation().getRelativePathAB(),mWidth);
//                moduleCoodinate = CoordinateCalculateUtil.trilateration(baseStation_1_x,baseStation_1_y,module_distance_1,baseStation_2_x,baseStation_2_y,module_distance_2,baseStation_3_x,baseStation_3_y,module_distance_3);
////                moduleCoodinate = CoordinateCalculateUtil.evaluateCoordinates(baseStation_1_x,baseStation_1_y,baseStation_2_x,baseStation_2_y,baseStation_3_x,baseStation_3_y,module_distance_1,module_distance_2,module_distance_3);
//                module_x = moduleCoodinate[0];
//                module_y = moduleCoodinate[1];
//                Logger.d("折算后模块的 (x，y) : " + module_x +" , "+module_y);
                module_x = getModule_x();
                module_y = getModule_y();

                if(CapModuleInfoManager.getCapInfoList().size() > 0) {
//                    Logger.d("当前帽子列表的第0项的 异常data ： " + CapModuleInfoManager.getCapInfoList().get(0).getData());
                    if( "".equals(CapModuleInfoManager.getCapInfoList().get(0).getData() ) ) {
                        canvas.drawBitmap(bmp_normal, module_x - bmp_normal.getWidth()/2, module_y - bmp_normal.getHeight() /2, mPaint);
                  }  else {
                        canvas.drawBitmap(bmp_abnormal, module_x - bmp_normal.getWidth()/2, module_y - bmp_normal.getHeight() /2, mPaint);
                    }
                }
            }
        }
        super.onDraw(canvas);
    }

    public float getModule_x() {
        return module_x;
    }

    public float getModule_y() {
        return module_y;
    }

    public void setModule_x(float module_x) {
        this.module_x = module_x;
    }

    public void setModule_y(float module_y) {
        this.module_y = module_y;
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

