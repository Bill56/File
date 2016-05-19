package com.bill.file.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bill.file.adapter.FileAdapter;
import com.bill.file.R;
import com.bill.file.util.FileUtil;
import com.bill.file.util.LogUtil;
import com.bill.file.util.ToastUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    // 承载主活动的根布局对象
    CoordinatorLayout layout;
    // 显示当前路径的Text
    private TextView textFilePath;
    // 视图
    private ListView listView;
    // 数据
    private List<File> filesData;
    // 适配器
    private FileAdapter fileAdapter;
    // 保存当前文件列表所在目录的变量
    private File currentDir;
    // 该活动弹出对话框时所共用的dialog对象
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载布局
        setContentView(R.layout.activity_main);
        // 初始化视图
        initView();
        // 初始化Action，让其显示返回键
        initActionBar();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        layout = (CoordinatorLayout) findViewById(R.id.rl_layout);
        listView = (ListView) findViewById(R.id.listView);
        textFilePath = (TextView) findViewById(R.id.text_file_path);
        // TODO 加载SD卡中的文件
        // 外部存储中的文件,即获取sd卡路径
        File sdPath = Environment.getExternalStorageDirectory();
        loadFilesData(sdPath);
        // 创建适配器
        fileAdapter = new FileAdapter(this, filesData);
        listView.setAdapter(fileAdapter);
        // 设置文件列表为空时候的显示视图
        listView.setEmptyView(findViewById(R.id.ll_file_list_empt));
        listView.setOnItemClickListener(new OnFileItemClickListener());
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
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
            case android.R.id.home:
                // 调用返回方法
                onBackPressed();
                break;
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
        return true;
    }

    /**
     * 当用户点击菜单后执行的方法代码
     */
    /**
     * 新建文件夹的方法
     */
    private void doNew() {
        // 执行操作
        showDialogWhenDoNew();
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
     * 点击新建后弹出的对话框
     */
    private void showDialogWhenDoNew() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置标题
        builder.setTitle(R.string.dialog_title_new_file);
        // 设置不可取消
        builder.setCancelable(false);
        // 加载对话框资源
        View v = getLayoutInflater().inflate(R.layout.dialog_new_file, null);
        // 获取v中的编辑器
        final EditText editNewFileName = (EditText) v.findViewById(R.id.edit_new_file_name);
        // 将视图装载到对话框
        builder.setView(v);
        // 设置对话框按钮和对应的事件监听器
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取编辑器内容
                String newFileName = editNewFileName.getText().toString();
                if (TextUtils.isEmpty(newFileName)) {
                    ToastUtil.show(MainActivity.this, R.string.dialog_new_file_empty_toast);
                    return;
                }
                // 当前目录不为空的之后执行新建操作
                if (currentDir != null) {
                    File createDir = new File(currentDir, newFileName);
                    // 当创建的文件夹不存在的时候执行
                    if (!createDir.exists()) {
                        createDir.mkdir();
                        // 将创建的文件夹添加到文件列表
                        filesData.add(createDir);
                        // 通知文件适配器列表发生改变
                        fileAdapter.notifyDataSetChanged();
                        ToastUtil.show(MainActivity.this, newFileName + getString(R.string.dialog_new_file_success_toast));
                    } else {
                        ToastUtil.show(MainActivity.this, R.string.dialog_new_file_fail_toast);
                    }
                }
                // 事件执行完毕后关闭对话框
                dismissDialogWhenDoNew();
            }
        });
        if (dialog == null) {
            dialog = builder.create();
        }
        dialog.show();
    }

    /**
     * 点击新建执行完毕后消失对话框
     */
    private void dismissDialogWhenDoNew() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 加载文件数据
     */
    private void loadFilesData(File parentDir) {
        // 创建数据列表
        if (filesData == null)
            filesData = new ArrayList<>();
        // 更新将当前文件目录的引用
        currentDir = parentDir;
        // 更新Text显示的路径
        textFilePath.setText(getString(R.string.activity_main_file_path) + currentDir.getAbsolutePath());
        // 获得目录中的所有文件
        File[] files = parentDir.listFiles();
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

    /**
     * 文件列表每一项被点击的时候所需的监听器
     */
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
                String suffix = FileUtil.getFileExtension(file);
                LogUtil.d(LogUtil.TAG, suffix);
                String fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
                if (fileType != null) {
                    intent.setDataAndType(data, fileType);
                    startActivity(intent);
                }
            } else {
                // 清除列表数据
                filesData.clear();
                // 进入，获得的file即为当前被点击的文件夹目录，也是下一要展开的文件夹父目录
                loadFilesData(file);
                // 通知文件适配器
                fileAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 当用户按下back键的时候执行
     */
    @Override
    public void onBackPressed() {
        // 如果当前目录为sd卡根目录，则不返回上一级，否则返回上一即目录
        // 外部存储中的文件,即获取sd卡路径
        File sdPath = Environment.getExternalStorageDirectory();
        LogUtil.d(LogUtil.TAG,sdPath.getAbsolutePath().toString());
        LogUtil.d(LogUtil.TAG,currentDir.getAbsolutePath().toString());
        if (!currentDir.getAbsolutePath().equals(sdPath.getAbsolutePath())) {
            filesData.clear();
            loadFilesData(currentDir.getParentFile());
            fileAdapter.notifyDataSetChanged();
        } else {
            finish();
        }
    }

}
