package com.bill.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.List;

/**
 * Created by Bill56 on 2016/4/16.
 */
public class FileAdapter extends BaseAdapter {

    // 上下文
    private Context mContext;
    // 数据
    private List<File> mFilesData;
    // 布局加载服务器
    LayoutInflater layoutInflater;

    /**
     * 构造方法
     *
     * @param context   上下文
     * @param filesData 数据
     */
    public FileAdapter(Context context, List<File> filesData) {
        this.mContext = context;
        this.mFilesData = filesData;
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获得数据的总数
     *
     * @return 数据的总数
     */
    @Override
    public int getCount() {
        return mFilesData.size();
    }

    /**
     * 获得特定位置的数据项
     *
     * @param position int 位置
     * @return File    数据项
     */
    @Override
    public Object getItem(int position) {
        return mFilesData.get(position);
    }

    /**
     * 获得特定位置的数据编号
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 获得特定位置的视图项
     *
     * @param position    int 位置
     * @param convertView View    可重用的视图项
     * @param parent      父元素容器，即ListView对象
     * @return View    加载了数据的视图项
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        // 加载模板，创建视图项——将布局创建成一个View对象
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_file, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File file = mFilesData.get(position);
        viewHolder.bindData(file);
        return convertView;
    }

    /**
     * 暂存视图对象的类
     */
    private class ViewHolder {
        ImageView imageFileIcon;
        TextView textFileName;
        TextView textFileInfo;
        ImageButton imageButtonMore;
        PopupMenuClickListener popupMenuClickListener;

        /**
         * 构造方法，获得布局中各个视图的引用
         *
         * @param v 视图对象
         */
        public ViewHolder(View v) {
            imageFileIcon = (ImageView) v.findViewById(R.id.image_file_icon);
            textFileName = (TextView) v.findViewById(R.id.text_file_name);
            textFileInfo = (TextView) v.findViewById(R.id.text_file_info);
            imageButtonMore = (ImageButton) v.findViewById(R.id.imageButton_more);
            popupMenuClickListener = new PopupMenuClickListener();
            // 为按钮设置点击监听器
            imageButtonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹出菜单
                    PopupMenu popupMenu = new PopupMenu(mContext, v);
                    // 加载菜单布局文件
                    popupMenu.inflate(R.menu.item_list_file_more);
                    // 为菜单注册监听器
                    popupMenu.setOnMenuItemClickListener(popupMenuClickListener);
                    // 显示菜单
                    popupMenu.show();
                }
            });
        }

        /**
         * 绑定数据
         *
         * @param file 文件操作对象
         */
        public void bindData(File file) {
            // 设置图标，如果是文件则显示文件图标，否则显示文件夹图标
            imageFileIcon.setImageResource(
                    file.isFile() ?
                            R.drawable.ic_attachment_24dp :
                            R.drawable.ic_folder_open_24dp);
            textFileName.setText(file.getName());
            int fileInDirCount = file.list() == null ? 0 : file.list().length;
            textFileInfo.setText(
                    file.isFile() ?
                            String.format("文件：%,d 字节", file.length()) :
                            String.format("目录：%d 文件", fileInDirCount));
            // 设置要操作的文件
            popupMenuClickListener.setFile(file);
        }

    }

    /**
     * 菜单监听器
     */
    class PopupMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        File file;

        /**
         * 设置要操作的文件
         *
         * @param file 要操作的文件引用
         */
        public void setFile(File file) {
            this.file = file;
        }

        /**
         * 菜单项点击后执行
         *
         * @param item 菜单项，即时间源头
         * @return true表示该控件已经处理完该事件，不用交给上层控件处理，否则需交给上层控件处理
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // 根据点击的菜单项的id进行不同的处理
            switch (item.getItemId()) {
                case R.id.action_copy:
                    doCopy();
                    break;
                case R.id.action_remove:
                    doRemove();
                    break;
                case R.id.action_rename:
                    doRename();
                    break;
            }
            return true;
        }

        /**
         * 执行文件的重命名操作
         */
        private void doRename() {
            showToast("重命名：" + file.getName());
        }

        /**
         * 执行文件的删除操作
         */
        private void doRemove() {
            showToast("删除：" + file.getName());
        }

        /**
         * 执行文件的复制操作
         */
        private void doCopy() {
            showToast("复制：" + file.getName());
        }

        private void showToast(String msg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }

    }

}
