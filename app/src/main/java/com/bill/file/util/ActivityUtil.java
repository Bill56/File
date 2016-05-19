package com.bill.file.util;

import com.bill.file.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理工具类
 * Created by Bill56 on 2016/5/19.
 */
public class ActivityUtil {

    // 存储活动的活动列表
    private static List<BaseActivity> activities = new ArrayList<>();

    /**
     * 将活动添加到活动管理列表
     *
     * @param activity 待添加的活动对象
     */
    public static void add(BaseActivity activity) {
        activities.add(activity);
    }

    /**
     * 将活动从活动管理列表移除
     *
     * @param activity 待移除的活动对象
     */
    public static void remove(BaseActivity activity) {
        activities.remove(activity);
    }

}
