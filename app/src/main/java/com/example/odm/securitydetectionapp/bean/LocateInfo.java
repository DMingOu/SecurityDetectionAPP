package com.example.odm.securitydetectionapp.bean;

/**
 * description: 定位页面标签位置实体类
 * author: ODM
 * date: 2019/9/27
 */
public class LocateInfo {

    /**
     * tagToStationA0 : 230     模块到基站A0的距离
     * tagToStationA1 : 650     模块到基站A1的距离
     * tagToStationA2 : 350     模块到基站A2的距离
     * stationA0ToStationA1 : 650       基站A0到基站A1的距离
     * stationA0ToStationA2 : 650       基站A0到基站A2的距离
     * stationA1ToStationA2 : 260       基站A1到基站A2的距离
     * modelName : 2A06   模块的具体名字
     */

    private double tagToStationA0;
    private double tagToStationA1;
    private double tagToStationA2;
    private double stationA0ToStationA1;
    private double stationA0ToStationA2;
    private double stationA1ToStationA2;
    private double modelName;

    public double getTagToStationA0() {
        return tagToStationA0;
    }

    public void setTagToStationA0(double tagToStationA0) {
        this.tagToStationA0 = tagToStationA0;
    }

    public double getTagToStationA1() {
        return tagToStationA1;
    }

    public void setTagToStationA1(double tagToStationA1) {
        this.tagToStationA1 = tagToStationA1;
    }

    public double getTagToStationA2() {
        return tagToStationA2;
    }

    public void setTagToStationA2(double tagToStationA2) {
        this.tagToStationA2 = tagToStationA2;
    }

    public double getStationA0ToStationA1() {
        return stationA0ToStationA1;
    }

    public void setStationA0ToStationA1(double stationA0ToStationA1) {
        this.stationA0ToStationA1 = stationA0ToStationA1;
    }

    public double getStationA0ToStationA2() {
        return stationA0ToStationA2;
    }

    public void setStationA0ToStationA2(double stationA0ToStationA2) {
        this.stationA0ToStationA2 = stationA0ToStationA2;
    }

    public double getStationA1ToStationA2() {
        return stationA1ToStationA2;
    }

    public void setStationA1ToStationA2(double stationA1ToStationA2) {
        this.stationA1ToStationA2 = stationA1ToStationA2;
    }

    public double getModelName() {
        return modelName;
    }

    public void setModelName(double modelName) {
        this.modelName = modelName;
    }




}
