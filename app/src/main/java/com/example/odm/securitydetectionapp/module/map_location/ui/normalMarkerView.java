package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.common.PointManager;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.orhanobut.logger.Logger;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.xuexiang.xui.utils.ResUtils.getString;

/**
 * @author: ODM
 * @date: 2019/7/30
 */
public class normalMarkerView  extends View {

    public class currentPoint {
        float currentX;
        float currentY;

        currentPoint(float currentX, float currentY) {
            this.currentX = currentX;
            this.currentY = currentY;
        }
    }

    public float currentX = 40;
    public float currentY = 50;
    private Bitmap bmp_normal;
    private Bitmap bmp_abnormal;
    Paint mPaint;
    Paint mPaint_red;
    // 宽高
    private int mWidth, mHeight;
    private List<currentPoint> points;
    private List<String> coordinatesList;
    private List<String> abnormalList;
    //让外部调用者控制的变量，ture为可绘制状态
    private boolean isEditted;
    private static final String TAG = "normalMarkerView";
    float offY;
    public normalMarkerView(Context context) {
        this(context, null);
    }

    public normalMarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        init();
    }

    public void initPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(42);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint_red = new Paint();
        mPaint_red.setStrokeWidth(10);
        mPaint_red.setTextSize(42);
        mPaint_red.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    //初始化你需要显示的光标样式 和 存储点
    private void init() {
        points = new ArrayList<>();
        points.addAll(PointManager.getPointList());
        coordinatesList = new ArrayList<>();
        coordinatesList.addAll(PointManager.getCoordinatesLis());
        abnormalList = new ArrayList<>();
        if (bmp_normal == null || bmp_abnormal == null) {
            bmp_normal = BitmapFactory.decodeResource(getResources(), R.drawable.mark_normal);
            bmp_abnormal = BitmapFactory.decodeResource(getResources(), R.drawable.mark_abnormal);
        }
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
         offY = fontTotalHeight / 2 - fontMetrics.bottom;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //用背景图是否存在阻隔了自动画的点
        if (bmp_normal != null && bmp_abnormal != null) {
            //将坐标列表去重再使用，减少遍历次数
            abnormalList = PointManager.getAbnormalList().stream().distinct().collect(Collectors.toList());
            if (points.size() == coordinatesList.size()) {
                for (int i = 0; i < points.size(); i++) {
                    float x = points.get(i).currentX - (bmp_normal.getWidth() / 2);
                    float y = points.get(i).currentY - (bmp_normal.getHeight() / 2);
                    float newY = y + offY;
                    boolean isError = false;
                    //遍历异常坐标列表，若
                    for(int k = 0 ; k < abnormalList.size(); k++) {
                        if(abnormalList.get( k ).equals(coordinatesList.get( i ) ) ) {
                            isError = true;
                            break;
                        }
                    }
                    //如果 变量 isError 为true 说明 该坐标无异常，绘制红色标记，否则绿色标记
                    if(isError) {
                        canvas.drawBitmap(bmp_abnormal, x, y, mPaint);
                        canvas.drawText(coordinatesList.get(i), x + 80f ,newY  , mPaint_red);
                    } else {
                        canvas.drawBitmap(bmp_normal, x, y, mPaint);
                        canvas.drawText(coordinatesList.get(i), x + 80f ,newY  , mPaint);
                    }

                }
            }
        }
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当前组件的currentX、currentY两个属性
        //event.getAction()判断事件的类型
        // isClickView = true;
        //抬起才标点，防止了滑动事件冲突
        if (event.getAction() == MotionEvent.ACTION_UP && bmp_normal != null) {
//            this.currentX = -bmp.getWidth();
//            this.currentY = -bmp.getHeight();
            this.currentX = event.getX();
            this.currentY = event.getY();
            if (isEditted) {
                points.add(new currentPoint(currentX, currentY));
                PointManager.setPointList(points);
                showConfirmDialog();
            } else {
                Logger.d("当前为非编辑状态");
            }
        }
        return true;
    }

        public void showConfirmDialog () {
            new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("请输入当前模块的坐标信息")
                    .inputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .input(
                            getString(R.string.hint_please_input_password),
                            "",
                            false,
                            (new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                }
                            }))
                    //右下角的按钮--确认按钮
                    .positiveText(R.string.lab_continue)
                    //左下角的按钮--取消按钮
                    .negativeText(R.string.lab_cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String coordinate = dialog.getInputEditText().getText().toString();
                            handleCoordinate(coordinate);

                        }
                    })
                    .cancelable(true)
                    .show();
        }


        public boolean isEditted () {
            return isEditted;
        }

        public void setEditted ( boolean editted){
            isEditted = editted;
        }

        public void handleCoordinate (String info)  {
            if (coordinatesList != null) {
                coordinatesList.add(info);
                PointManager.setCoordinatesList(coordinatesList);
            }
        }
    }

