package com.example.odm.securitydetectionapp.util;

import android.text.InputType;

import com.example.odm.securitydetectionapp.application.SecurityDetectionAPP;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

/**
 * @author: ODM
 * @date: 2019/7/30
 */
public class DialogUtils  {

    static MaterialDialog.Builder  builder = new MaterialDialog.Builder(SecurityDetectionAPP.getContext());


    /**
     *  简单的提示性对话框
     *  可以带图标
     *
     *
     * @param iconRedId    the icon red id
     * @param title        the title
     * @param content      the content
     * @param positiveText the positive text
     */
    public static   void showSimpleTipDialog(int iconRedId ,String title ,String content ,String  positiveText){
        builder.iconRes(iconRedId)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .show();

    }

    public static void showSimpleConfirmDialog(int iconRedId ,String content ,String  positiveText,String negativeText,MaterialDialog.SingleButtonCallback callback){
        builder.iconRes(iconRedId)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .content(content)
                .onPositive(callback)
                .show();
    }

    public static void showEditDialog(int iconRedId ,String content ,String  positiveText,String negativeText,
                               MaterialDialog.SingleButtonCallback callback,int InputType,String hintString,MaterialDialog.InputCallback inputCallback) {

        builder.iconRes(iconRedId)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .inputType(InputType)
                .input(hintString , "" ,false ,inputCallback)
                .content(content)
                .onPositive(callback)
                .cancelable(true)
                .show();

    }
}
