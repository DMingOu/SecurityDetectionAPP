package com.example.odm.securitydetectionapp.util;

import com.example.odm.securitydetectionapp.module.home.bean.capInfo;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/28
 */
public class DuplicateUtil {

    private static List<capInfo> removeDuplicate(List<capInfo> list) {
        LinkedHashSet<capInfo> set = new LinkedHashSet<capInfo>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
