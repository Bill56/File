package com.bill.file;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 视图
    private ListView listView;
    // 数据
    private List<File> filesData;
    // 适配器
    private FileAdapter fileAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载布局
        setContentView(R.layout.activity_main);
        // 初始化视图
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        // TODO 加载SD卡中的文件
        loadFilesData();
        // 创建适配器
        fileAdapter = new FileAdapter(this, filesData);
        listView.setAdapter(fileAdapter);
        listView.setOnItemClickListener(new OnFileItemClickListener());
    }

    /**
     * 加载文件数据
     */
    private void loadFilesData() {
        // 创建数据列表
        filesData = new ArrayList<>();
        // 外部存储中的文件,即获取sd卡路径
        File sdPath = Environment.getExternalStorageDirectory();
        // 获得目录中的所有文件
        File[] files = sdPath.listFiles();
        // Log.d("MainActivity",String.valueOf(files.length));
        for (File f : files) {
            filesData.add(f);
        }
    }

    class MyFilter1 implements FileFilter {

        /**
         * 文件过滤器
         *
         * @param pathname 文件
         * @return true 则出现在数组中，否则不出现数组中
         */
        @Override
        public boolean accept(File pathname) {
            return !pathname.isFile();
        }
    }

    class OnFileItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            File file = filesData.get(position);
            if (file.isFile()) {
                // 打开文件
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri data = Uri.fromFile(file);
                // 获得文件后缀名
                // 获得最后一个.的索引
                int index = file.getName().lastIndexOf(".");
                String suffix = file.getName().substring(index+1);
                Log.d("MainActivity",suffix);
                String fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
                if (fileType != null) {
                    intent.setDataAndType(data, fileType);
                    startActivity(intent);
                }
            } else {
                // 进入
                // 清除列表数据
                // 获得目录中的内容，计入列表中
                //
            }
        }
    }

}
