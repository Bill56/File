package com.bill.file.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Bill56 on 2016/5/19.
 */
public class ToastUtil {

    public static void show(Context context,String text) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context,int text) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}
