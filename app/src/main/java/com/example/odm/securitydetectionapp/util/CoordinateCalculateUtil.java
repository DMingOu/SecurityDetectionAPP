package com.example.odm.securitydetectionapp.util;

import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 现实坐标(距离)转换为屏幕坐标
 *
 * @author: ODM
 * @date: 2019/9/5
 */
public class CoordinateCalculateUtil {

     static float[] point = new float[2];



    /**
     * 将 现实空间坐标 折算成 屏幕坐标系坐标
     *
     * @param x              the x
     * @param realDistance   the real distance
     * @param screenDistance the screen distance
     * @return the double
     */
    public static double  realConvertScreen(double x , double realDistance , double screenDistance)  {
        double screenX = x * (screenDistance / realDistance) ;

        return  screenX;
    }

    /**
     * 设定 点B 和 点C 连线 与屏幕成90度
     * 由点C坐标和 三角形三条边 abc 距离计算 点A 的 x y值
     *
     * @param a  the a
     * @param b  the b
     * @param c  the c
     * @param xC the x c
     * @param yC the y c
     * @return the double [ ]
     */
    public static double[] calcaulatePoint3TH(double a , double b ,double c ,double xC ,double yC) {
        //设定角A为（0 ，0），三角形为三边0.5
        double  cosC = (a * a + b*b - c* c)  / (2 * a * b) ;
        double sinC = Math.sqrt( 1.0 - cosC * cosC);
        double xA = b * cosC + xC;
        double yA= b * sinC + yC;
        double[] A = new double[2];
        A[0] = xA;
        A[1] = yA;
        return  A;
    }

    public static float[] calcaulate3ThPoint(double a ,double b ,double c ,double xA ,double yA ,double xB ,double yB ) {
        double timeCut1 = System.currentTimeMillis();
        double thetaA;
        thetaA = Math.acos((b * b + c * c - a * a) / (2 *b * c));
        double    thetaAB = Math.atan((yB - yA)/(xB - xA));
        double   thetaAC = thetaA - thetaAB;
        double  xC ;
        if(thetaAC * 180 / Math.PI == 90) {
            //c点在A点正上方
            xC = xA;
        } else {
            xC = xA + b * Math.cos(thetaAC);
        }
        double  yC = yA + b * Math.sin(Math.PI - thetaAC);
        double timeCut2 = System.currentTimeMillis();
        double  time = timeCut2 - timeCut1;
        point[0] = (float) xC;
        point[1] = (float)yC;
        return point;
    }

    /**
     * 需要参数，需要三个基站的坐标x、y 和 基站与模块的位置 r
     */
    public static float[] evaluateCoordinates(float _xa, float _ya, float _xb, float _yb, float _xc, float _yc, float _ra, float _rb, float _rc){
        BigDecimal xa = new BigDecimal(_xa);
        BigDecimal ya = new BigDecimal(_ya);
        BigDecimal xb= new BigDecimal(_xb);
        BigDecimal yb= new BigDecimal(_yb);
        BigDecimal xc= new BigDecimal(_xc);
        BigDecimal yc= new BigDecimal(_yc);
        BigDecimal ra= new BigDecimal(_ra);
        BigDecimal rb= new BigDecimal(_rb);
        BigDecimal rc = new BigDecimal(_rc);
        BigDecimal a2 = ra.multiply(ra);
        BigDecimal b2 = rb.multiply(rb);
        BigDecimal c2 = rc.multiply(rc);
        BigDecimal xa2 = xa.multiply(xa);
        BigDecimal xb2 = xb.multiply(xb);
        BigDecimal xc2 = xc.multiply(xc);
        BigDecimal ya2 = ya.multiply(ya);
        BigDecimal yb2 = yb.multiply(yb);
        BigDecimal yc2 = yc.multiply(yc);
        BigDecimal rbc = b2.subtract(c2);
        BigDecimal xbc = xb2.subtract(xc2);
        BigDecimal ybc = yb2.subtract(yc2);
        BigDecimal va = rbc.subtract(xbc).subtract(ybc).divide(new BigDecimal("2.0"));
        BigDecimal rba = b2.subtract(a2);
        BigDecimal xba = xb2.subtract(xa2);
        BigDecimal yba = yb2.subtract(ya2);
        BigDecimal vb = rba.subtract(xba).subtract(yba).divide(new BigDecimal("2.0"));
        BigDecimal xcb = xc.subtract(xb);
        BigDecimal xab = xa.subtract(xb);
        BigDecimal yab = ya.subtract(yb);
        BigDecimal ycb = yc.subtract(yb);
        BigDecimal va1 = va.multiply(xab);
        BigDecimal vb1 = vb.multiply(xcb);
        BigDecimal aa = yab.multiply(xcb);
        BigDecimal ab = ycb.multiply(xab);
        BigDecimal y1 = vb1.subtract(va1);
        BigDecimal y2 = aa.subtract(ab);
        BigDecimal y = y1.divide(y2,10, RoundingMode.DOWN);
        BigDecimal x;
        if (xcb.signum() != 0) {
            x = va.subtract((y.multiply(ycb))).divide(xcb,10,RoundingMode.DOWN);
        } else {
            x = vb.subtract(y.multiply(yab)).divide(xab,10,RoundingMode.DOWN);
        }
        point[0] = x.floatValue();
        point[1] = y.floatValue();
        return point;
    }

    /**
     * 简单三边算法，需要 需要三个基站的坐标x、y 和 基站与模块的位置距离 d
     * @param x1
     * @param y1
     * @param d1
     * @param x2
     * @param y2
     * @param d2
     * @param x3
     * @param y3
     * @param d3
     * @return
     */
    public static float[] trilateration(double x1,double y1,double d1, double x2, double y2,double d2, double x3, double y3, double d3)
    {
        double a11 = 2*(x1-x3);
        double a12 = 2*(y1-y3);
        double b1 = Math.pow(x1,2)-Math.pow(x3,2) +Math.pow(y1,2)-Math.pow(y3,2) +Math.pow(d3,2)-Math.pow(d1,2);
        double a21 = 2*(x2-x3);
        double a22 = 2*(y2-y3);
        double b2 = Math.pow(x2,2)-Math.pow(x3,2) +Math.pow(y2,2)-Math.pow(y3,2) +Math.pow(d3,2)-Math.pow(d2,2);
        double x= (b1*a22-a12*b2)/(a11*a22-a12*a21);
        point[0] = (float) x;
        double y = (a11*b2-b1*a21)/(a11*a22-a12*a21);
        point[1] = (float) y;
        return point;
    }
}
