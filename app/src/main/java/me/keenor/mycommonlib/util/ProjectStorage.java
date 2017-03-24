package me.keenor.mycommonlib.util;

import android.os.Environment;

import java.io.File;

import me.keenor.androidcommon.util.AppUtil;
import me.keenor.androidcommon.util.LogUtil;

/**
 * Author:      chenliuchun
 * Date:        17/3/24
 * Description: 项目文件存储位置
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class ProjectStorage {

    private static File innerFileDir;
    private static File innerCacheDir;
    private static File externalFileDir;
    private static File externalCacheDir;
    private static File externalRootDir;
    private static File externalPublicDir;


    /**
     * 检查外部存储是否可读写
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 检查外部存储是否可读
     * @return
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * 获取内部存储路径对象
     * @return
     */
    public static File getInnerFileDir(){
        if (innerFileDir == null) {
            innerFileDir = AppUtil.getContext().getFilesDir();
        }
        return innerFileDir;
    }

    /**
     * 获取内部存储缓存文件路径对象
     * @return
     */
    public static File getInnerCacheDir(){
        if (innerCacheDir == null) {
            innerCacheDir = AppUtil.getContext().getCacheDir();
        }
        return innerCacheDir;
    }

    /**
     * 获得外部存储的文件路径对象，如音乐、图片等，
     * 传空字符串，则表示获得上一级目录
     * 该文件夹随着卸载而被清空
     * @param type The type of files directory to return. May be {@code null}
     *            for the root of the files directory or one of the following
     *            constants for a subdirectory:
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *            {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @param child 子文件夹名称
     * @return the absolute path to application-specific directory. May return
     *         {@code null} if shared storage is not currently available.
     */
    public static File getExternalFileDir(String type, String child){
        if (!isExternalStorageWritable()) {
            return null;
        }
        if (externalFileDir == null) {
            externalFileDir = new File(AppUtil.getContext().getExternalFilesDir(type), child);
            if (!externalFileDir.mkdirs()) {
                LogUtil.i1("externalFileDir not created, it's exsits");
            }
        }
        return externalFileDir;
    }

    /**
     * 获得外部存储的缓存路径，
     * 传空字符串，则表示获得根目录
     * 该文件夹随着卸载而被清空
     * @param child 子文件夹名称
     * @return the absolute path to application-specific directory. May return
     *         {@code null} if shared storage is not currently available.
     */
    public static File getExternalCacheDir(String child){
        if (!isExternalStorageWritable()) {
            return null;
        }
        if (externalCacheDir == null) {
            externalCacheDir = new File(AppUtil.getContext().getExternalCacheDir(), child);
            if (!externalCacheDir.mkdirs()) {
                LogUtil.i1("externalCacheDir not created, it's exsits");
            }
        }
        return externalCacheDir;
    }

    /**
     * 获得外部存储的根路径
     * @return
     */
    public static File getExternalRootDir(){
        if (!isExternalStorageWritable()) {
            return null;
        }
        if (externalRootDir == null) {
            externalRootDir = Environment.getExternalStorageDirectory();
        }
        return externalRootDir;
    }

    /**
     * 获取外部存储文件存储路径
     * @param type The type of files directory to return. May be {@code null}
     *            for the root of the files directory or one of the following
     *            constants for a subdirectory:
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *            {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @param child 子文件夹名称
     * @return Returns the File path for the directory. Note that this directory
     *         may not yet exist, so you must make sure it exists before using
     *         it such as with {@link File#mkdirs File.mkdirs()}.
     */
    public static File getExternalPublicDir(String type, String child){
        if (!isExternalStorageWritable()) {
            return null;
        }
        if (externalPublicDir == null) {
            externalPublicDir = new File(Environment.getExternalStoragePublicDirectory(type), child);
            if (!externalPublicDir.mkdirs()) {
                LogUtil.i1("externalPublicDir not created, it's exsits");
            }
        }
        return externalPublicDir;
    }


}
