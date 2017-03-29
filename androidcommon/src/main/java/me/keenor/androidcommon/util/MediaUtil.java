package me.keenor.androidcommon.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Author:      chenliuchun
 * Date:        17/3/29
 * Description: 调用系统功能的工具类
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class MediaUtil {
    /**
     * 打开摄像头拍照，回调获取照片缩略图
     * 缩略图从回调 data 的 键名"data" 取得
     *
     * @param code 拍照指令码
     * @param activity
     */
    public static void takePicture(int code, Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, code);
    }

    /**
     * 打开摄像头拍照，回调后取图片
     * 回调中的 data 参数为null
     *  @param code  拍照指令码
     * @param image 指定图片的存储位置
     * @param activity
     */
    public static void takePicture(int code, Uri image, Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image);
        activity.startActivityForResult(intent, code);
    }

    /**
     * 录制视频
     * @param code
     * @param activity
     */
    public static void takeVideo(int code, Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(AppUtil.getContext().getPackageManager()) != null) {
            activity.startActivityForResult(intent, code);
        }
    }

    /**
     * 相册图片选取
     * @param code
     * @param activity
     */
    public static void takeAlbum(int code, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        activity.startActivityForResult(intent, code);
    }

    /**
     * 图片剪裁
     * @param uri
     * @param width  剪裁宽度
     * @param height 剪裁高度
     * @param code   指令码
     * @param activity
     */
    public static void takeCropPicture(Uri uri, int width, int height, int code, Activity activity) {
        if (uri == null) {
            LogUtil.i1("uri 不得为空");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//从所有图片中进行选择
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        //如果为true,则通过 Bitmap bmap = data.getParcelableExtra("data")取出数据
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // no face detection
        intent.putExtra("noFaceDetection", false);
        activity.startActivityForResult(intent, code);
    }

    /**
     * 根据 uri 查询图片存储的绝对地址，用于相册图片的选择上
     * @param uri
     * @return
     */
    public static String getRealFilePath(Uri uri) {
        if (null == uri) return null;
        String scheme = uri.getScheme();
        String data = null;

        if (scheme == null) data = uri.getPath();

        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            try {
                Cursor cursor = AppUtil.getContext().getContentResolver()
                        .query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index > -1) {
                            data = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                data = uri.getPath();
            }
        }
        return data;
    }

}
