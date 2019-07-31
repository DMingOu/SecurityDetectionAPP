package com.example.odm.securitydetectionapp.common;

import android.util.ArrayMap;

import com.example.odm.securitydetectionapp.module.map_location.ui.normalMarkerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/30
 */
public  class PointManager  {

    private static List<normalMarkerView.currentPoint> mPointList = new ArrayList<>();

    private static List<String>  mCoordinatesList = new ArrayList<>();

    private static List<String>  mAbnormalList = new ArrayList<>();

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


}
