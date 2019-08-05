package com.example.odm.securitydetectionapp.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.odm.securitydetectionapp.R;

/**
 * @author: ODM
 * @date: 2019/7/28
 */
public class noHistoryView extends RelativeLayout {
    public noHistoryView(Context context) {

        super(context);
    }

    public noHistoryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View inflate = inflate(getContext(), R.layout.view_history_empty, this);
    }

    public noHistoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
