package com.bill.file.util;

import java.io.File;

/**
 * Created by Bill56 on 2016/5/19.
 */
public class FileUtil {

    /**
     * 获得文件的后缀名
     * @param file  文件
     * @return  文件的后缀名
     */
    public static String getFileExtension(File file) {
        // 获得文件后缀名
        // 获得最后一个.的索引
        int index = file.getName().lastIndexOf(".");
        String suffix = file.getName().substring(index + 1);
        return suffix;
    }

}
