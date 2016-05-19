package com.bill.file.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bill.file.util.ActivityUtil;

/**
 * 当前程序所有活动的基类，便于管理
 * Created by Bill56 on 2016/5/19.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 当活动被创建时，自动添加到活动管理列表
     *
     * @param savedInstanceState 保存活动意外销毁时的状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.add(this);
    }

    /**
     * 当活动被销毁时，自动从活动管理列表移除
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.remove(this);
    }
}
