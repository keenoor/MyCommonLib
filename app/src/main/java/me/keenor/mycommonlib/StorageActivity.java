package me.keenor.mycommonlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import me.keenor.androidcommon.ui.ToastUtil;
import me.keenor.androidcommon.util.LogUtil;
import me.keenor.androidcommon.util.ViewFindHelper;

/**
 * Author:      chenliuchun
 * Date:        17/3/22
 * Description: 分清楚内部存储、外部存储之间的关系，调用方法，文件夹权限，卸载残留情况；
 *          https://developer.android.com/guide/topics/data/data-storage.html#filesInternal
 *          https://developer.android.com/training/basics/data-storage/files.html#InternalVsExternalStorage
 * 内部存储：
     1. 它始终可用。
     2. 只有您的应用可以访问此处保存的文件。
     3. 当用户卸载您的应用时，系统会从内部存储中移除您的应用的所有文件。
     4. 当您希望确保用户或其他应用均无法访问您的文件时，内部存储是最佳选择。

 * 外部存储：
     1. 它并非始终可用，因为用户可采用 USB 存储设备的形式装载外部存储，并在某些情况下会从设备中将其移除。
     2. 它是全局可读的，因此此处保存的文件可能不受您控制地被读取。
     3. 当用户卸载您的应用时，只有在您通过 getExternalFilesDir() 将您的应用的文件保存在目录中时，系统才会从此处移除您的应用的文件。
     4. 对于无需访问限制以及您希望与其他应用共享或允许用户使用计算机访问的文件，外部存储是最佳位置。

 * 注    意：
     1. 当用户卸载您的应用时，Android 系统会删除以下各项：
     2. 您保存在内部存储中的所有文件
     3. 您使用 getExternalFilesDir() 保存在外部存储中的所有文件。
     4. 但是，您应手动删除使用 getCacheDir() 定期创建的所有缓存文件并且定期删除不再需要的其他文件。

 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */
public class StorageActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewFindHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new ViewFindHelper(this, R.layout.activity_storage);
        ViewGroup contentView = (ViewGroup) ((ViewGroup) helper.getContentView()).getChildAt(0);

        for (int i = 0; i < contentView.getChildCount(); i++) {
            View btn =  contentView.getChildAt(i);
            btn.setOnClickListener(this);
        }

        helper.setText(R.id.btn_01, "getInnerFileDir");
        helper.setText(R.id.btn_02, "getInnerCacheDir");
        helper.setText(R.id.btn_03, "writeInnerFile");
        helper.setText(R.id.btn_04, "createTempFile");
        helper.setText(R.id.btn_05, "getExternalPublicStorageDir");
        helper.setText(R.id.btn_06, "getExternalFileDir");
        helper.setText(R.id.btn_07, "checkSpace");
        helper.setText(R.id.btn_08, "testOtherDir");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_01:
                getInnerFileDir();
                break;
            case R.id.btn_02:
                getInnerCacheDir();
                break;
            case R.id.btn_03:
                writeInnerFile();
                break;
            case R.id.btn_04:
                createTempFile();
                break;
            case R.id.btn_05:
                getExternalPublicStorageDir();
                break;
            case R.id.btn_06:
                getExternalFileDir();
                break;
            case R.id.btn_07:
                checkSpace();
                break;
            case R.id.btn_08:
                testOtherDir();
                break;
        }
    }

    /**
     * /data/user/0/me.keenor.mycommonlib/files
     */
    public void getInnerFileDir(){
        File filesDir = getFilesDir();
        String path = filesDir.getPath();
        String absolutePath = filesDir.getAbsolutePath();
        String[] list = filesDir.list();

        ToastUtil.show(path);
        LogUtil.i2(path);

        ToastUtil.show(absolutePath);
        LogUtil.i2(absolutePath);

        LogUtil.i2(Arrays.toString(list));
    }

    /**
     * /data/user/0/me.keenor.mycommonlib/cache
     */
    public void getInnerCacheDir(){
        File cacheDir = getCacheDir();
        String path = cacheDir.getPath();
        String absolutePath = cacheDir.getAbsolutePath();
        String[] list = cacheDir.list();

        ToastUtil.show(path);
        LogUtil.i2(path);

        ToastUtil.show(absolutePath);
        LogUtil.i2(absolutePath);

        LogUtil.i2(Arrays.toString(list));
    }

    /**
     * 写入的是 /data/user/0/me.keenor.mycommonlib/files 路径下
     */
    public void writeInnerFile(){
        String filename = "my_inner_file";
        String string = "Hello world! my_inner_file";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入的是 /data/user/0/me.keenor.mycommonlib/cache 路径下
     */
    public void createTempFile() {
        String url = "http://bj.bcebos.com/weitu-img/1-233112952669.jpg";
        File file = new File("nullname");
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            ToastUtil.show("createTempFile: " + fileName);
            LogUtil.i2("createTempFile: " + fileName);
            file = File.createTempFile(fileName, null, getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.i2("createTempFile: " + file.getPath());
    }

    /**
     * /storage/emulated/0/Pictures/album
     */
    public void getExternalPublicStorageDir() {
        String albumName = "album";
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            LogUtil.i2("Directory not created");
        }
        LogUtil.i2("getExternalPublicStorageDir: " + file.getPath());
    }

    /**
     * /storage/emulated/0/Android/data/me.keenor.mycommonlib/files/Pictures/album
     */
    public void getExternalFileDir() {
        String albumName = "album";
        // Get the directory for the user's public pictures directory.
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            LogUtil.i2("Directory not created");
        }
        LogUtil.i2("getExternalStorageDir: " + file.getPath());
    }

    public void checkSpace(){
        float freeSpace = Environment.getExternalStorageDirectory().getFreeSpace()/1024.0f/1024/1024;
        float totalSpace = Environment.getExternalStorageDirectory().getTotalSpace()/1024.0f/1024/1024;

        ToastUtil.show("freeSpace: " + freeSpace);
        LogUtil.i2("freeSpace: " + freeSpace);
        ToastUtil.show("totalSpace: " + totalSpace);
        LogUtil.i2("totalSpace: " + totalSpace);
    }

    public void testOtherDir(){

        // 内部指定文件夹存储
        String innerDir = getDir("test", Context.MODE_PRIVATE).getPath();

        // 外部存储的 cache 目录
        String exterCacheDir = getExternalCacheDir().getAbsolutePath();
        // 外部存储的文件目录
        String exterFileDirMusic = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        // 外部存储的不带包名存储路径
        String envExternalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        // 外部存储的不带包名存储路径
        String envExternalStoragePubDirMusic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();

        LogUtil.i2("innerDir: " + innerDir);

        LogUtil.i2("exterCacheDir: " + exterCacheDir);
        LogUtil.i2("exterFileDirMusic: " + exterFileDirMusic);
        LogUtil.i2("envExternalStorageDir: " + envExternalStorageDir);
        LogUtil.i2("envExternalStoragePubDirMusic: " + envExternalStoragePubDirMusic);
    }


    public static void show(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, StorageActivity.class);
        context.startActivity(intent);
    }
}
