package com.example.odm.securitydetectionapp.core;

/**
 * @author: ODM
 * @date: 2019/9/5
 */
public class PageStatusManager {

    public static final int PAGE_HISTORY_CURENT = 0;
    public static final int PAGE_WATCH_CURRENT = 1;
    public static final int PAGE_LOCATION_CURRENT = 2;

    private static int pageStatus;

    public static int getPageStatus() {
        return PageStatusManager.pageStatus;
    }

    public static void setPageStatus(int pageStatus) {
        PageStatusManager.pageStatus = pageStatus;
    }




}
