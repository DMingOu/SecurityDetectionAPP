package com.example.odm.securitydetectionapp.core;

import android.graphics.Point;
import android.util.ArrayMap;

import com.example.odm.securitydetectionapp.bean.BaseStation;
import com.example.odm.securitydetectionapp.module.map_location.ui.normalMarkerView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/30
 */
public  class PointManager  {

    private static List<normalMarkerView.currentPoint> mPointList = new ArrayList<>();

    private  static List<String>  mCoordinatesList = new ArrayList<>();

    private  static List<String>  mAbnormalList = new ArrayList<>();

    private static BaseStation baseStation ;

    //存放 三角定位后算法得出的模块的 x y
    private static double[] point = new double[2];
     public static List<normalMarkerView.currentPoint> getPointList() {
            if(mPointList == null) {
                mPointList = new ArrayList<>();
                return  mPointList;
            }
            return mPointList;
    }

    public static List<String> getCoordinatesLis() {
        if(mCoordinatesList == null) {
            mCoordinatesList = new ArrayList<>();
            return  mCoordinatesList;
        }
        return mCoordinatesList;
    }

    public static List<String> getAbnormalList() {
        if(mAbnormalList == null) {
            mAbnormalList = new ArrayList<>();
            return  mAbnormalList;
        }
        return mAbnormalList;
    }


    public static void setPointList(List<normalMarkerView.currentPoint>  mList) {
        mPointList.clear();
        mPointList.addAll(mList);
    }

    public static void setCoordinatesList(List<String> mList) {
        mCoordinatesList.clear();
        mCoordinatesList.addAll(mList);
    }


    public static void setAbnormalList(List<String> mList) {
        mAbnormalList.clear();
        mAbnormalList.addAll(mList);
    }

    public static BaseStation getBaseStation() {
        return baseStation;
    }

    public static void setBaseStation(BaseStation baseStation) {
        PointManager.baseStation = baseStation;
    }


    public static boolean  checkPointListCount() {
        if (PointManager.getPointList().size() > 0) {
            //normalMarkerView会在画图时将坐标存PointManger,如果size大于0
            //说明已经开标注模式，标注过点了，那么已经在加载页面时将上次离开页面时的点加载出来
            return true;
        } else {
            return false;
        }
    }






}
