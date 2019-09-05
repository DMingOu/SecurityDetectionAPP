package com.example.odm.securitydetectionapp.bean;

/**
 * @author: ODM
 * @date: 2019/9/4
 */
public class BaseStation {

    double relativePathAB;
    double relativePathAC;
    double relativePathBC;

    public BaseStation(double relativePathAB ,double relativePathAC ,double relativePathBC) {
        this.relativePathAB = relativePathAB ;
        this.relativePathAC = relativePathAC;
        this.relativePathBC = relativePathBC;
    }

    public BaseStation() {

    }

    public double getRelativePathAB() {
        return relativePathAB;
    }

    public void setRelativePathAB(double relativePathAB) {
        this.relativePathAB = relativePathAB;
    }

    public double getRelativePathAC() {
        return relativePathAC;
    }

    public void setRelativePathAC(double relativePathAC) {
        this.relativePathAC = relativePathAC;
    }

    public double getRelativePathBC() {
        return relativePathBC;
    }

    public void setRelativePathBC(double relativePathBC) {
        this.relativePathBC = relativePathBC;
    }
}
