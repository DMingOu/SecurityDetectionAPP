package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.core.PointManager;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.orhanobut.logger.Logger;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xuexiang.xui.utils.ResUtils.getString;

/**
 * @author: ODM
 * @date: 2019/7/30
 */
public class normalMarkerView  extends View {

    /**
     * 用户触碰相对于手机的坐标类
     */
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
    //存储用户触碰坐标的列表
    private List<currentPoint> points;
    //存储用户输入信息的列表
    private List<String> coordinatesList;
    //存储异常信息的列表
    private List<String> abnormalList;
    //让外部调用者控制的变量，ture为可绘制View状态
    private boolean isEditted;
    private static final String TAG = "normalMarkerView";
    float offY;
    MaterialDialog.Builder builder;
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
    }

    //初始化你需要显示的光标样式 和 存储点
    private void init() {
        builder  = new MaterialDialog.Builder(getContext());
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


    /*
    * 触碰事件--监听用户对屏幕的动作
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && bmp_normal != null) {
            this.currentX = event.getX();
            this.currentY = event.getY();
            if (isEditted) {
                showConfirmDialog(currentX,currentY);
            } else {
                Logger.d("当前为非编辑状态");
            }
        }
        return true;
    }

        public void showConfirmDialog (float currentX ,float currentY) {
//            new MaterialDialog.Builder(getContext())
            builder.title("提示")
                    .content("请输入当前模块的坐标信息")
                    .inputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .input(
                            getString(R.string.hint_please_input_password), "", false, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                }
                            })
                    .positiveText(R.string.lab_continue)
                    .negativeText(R.string.lab_cancel)
                    //若用户主动取消了模块的信息输入，则该点不做标记，反之打上标记
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String coordinate = dialog.getInputEditText().getText().toString();
                            String pattern = "^[A-Za-z0-9]+$";
                            boolean isMatch = Pattern.matches(pattern ,coordinate);
                            // isMatch 为 true ，说明 用户输入只包含 字母和数字
                            if( isMatch) {
                                points.add(new currentPoint(currentX, currentY));
                                PointManager.setPointList(points);
                                handleCoordinate(coordinate);
                            } else {
                                ToastUtil.showShortToastCenter("只能输入英文字母以及数字！");
                            }

                        }
                    })
                    .cancelable(true)
                    .show();
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

        public void  clearAllMarker() {
            points.clear();
            coordinatesList.clear();
            abnormalList.clear();
            PointManager.setPointList(points);
            PointManager.setAbnormalList(abnormalList);
            PointManager.setCoordinatesList(coordinatesList);
            invalidate();
        }
    }

