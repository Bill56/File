package com.bill.file.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bill.file.adapter.FileAdapter;
import com.bill.file.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 承载主活动的根布局对象
    CoordinatorLayout layout;
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
        layout = (CoordinatorLayout) findViewById(R.id.rl_layout);
        listView = (ListView) findViewById(R.id.listView);
        // TODO 加载SD卡中的文件
        loadFilesData();
        // 创建适配器
        fileAdapter = new FileAdapter(this, filesData);
        listView.setAdapter(fileAdapter);
        listView.setOnItemClickListener(new OnFileItemClickListener());
    }

    /**
     * 创建选项菜单
     *
     * @param menu 承载选项菜单资源的Menu对象
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载选项菜单
        getMenuInflater().inflate(R.menu.activity_main_option, menu);
        // 获得菜单中的某一项
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("请输入文件名");
        // 设置输入框的事件监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * 提交的时候执行
             * @param query 搜索框中输入的文字
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            /**
             * 当输入框中的文本变化的时候执行
             * @param newText   变化后的文本
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    /**
     * 当选项菜单被点击后执行的操作
     *
     * @param item 被点击的选项菜单
     * @return true表示本层已处理完毕，无需交由上层处理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.group_sort) {
            // 设为选中
            item.setChecked(true);
        }

        // TODO 考虑多选的处理


        switch (item.getItemId()) {
            case R.id.action_new:
                doNew();
                break;
            case R.id.action_sort_size:

                break;
            case R.id.action_sort_date:

                break;
            case R.id.action_sort_type:

                break;
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doNew() {
        // 执行操作

        // 给用户可以交互的提示
        Snackbar.make(layout, "已添加", Snackbar.LENGTH_INDEFINITE).
                setAction("撤销",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO

                            }
                        }).show();
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
                String suffix = file.getName().substring(index + 1);
                Log.d("MainActivity", suffix);
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
