package me.keenor.mycommonlib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.keenor.androidcommon.ui.ToastUtil;
import me.keenor.androidcommon.util.LogUtil;
import me.keenor.androidcommon.util.ViewFindHelper;

/**
 * Author:      chenliuchun
 * Date:        17/3/22
 * Description: 1. 6.0 以上系统的权限处理的官方 demo，还可使用第三方库；
 *              2. 相机调用 demo；
 *
 * 参    考：https://developer.android.com/training/permissions/requesting.html#perm-request
 *          https://developer.android.com/guide/topics/security/permissions.html#normal-dangerous
 *          http://blog.csdn.net/self_study/article/details/50186435
 *
 * 注    意：
 *          1. 拍照后回调里的照片，默认的是 thumbnail，不适合完整展示，用的不多，
 *          更多的是发送 intent 时候传入 URI 对象，然后得到回调后从原位置取回；
 *          2. 照片压缩默认的方法是 BitmapFactory 的经典方法；
 *          3.
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class CameraPermissionActivity extends AppCompatActivity {

    private static final int CODE_IMAGE = 0x100;
    private static final int CODE_VIDEO = 0x200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private ViewFindHelper helper;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new ViewFindHelper(this, R.layout.activity_camera_permission);
        helper.setOnClickListener(R.id.btn_camera, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                openCamera4Raw(CODE_IMAGE, fileUri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i2("onActivityResult: requestCode: " + requestCode
                + ", resultCode: " + requestCode + ", data: " + data);
        // 如果是拍照
        if (CODE_IMAGE == requestCode) {
            if (RESULT_OK == resultCode) {
                // Check if the result includes a thumbnail Bitmap
                if (data != null) {
                    // 没有指定特定存储路径的时候
                    LogUtil.i2("data is NOT null, file on default position.");
                    ToastUtil.show("Image saved to:\n" + data.getData());

                    // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
                    // Image captured and saved to fileUri specified in the Intent
                    if (data.hasExtra("data")) {
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        int width = thumbnail.getWidth();
                        int height = thumbnail.getHeight();
                        LogUtil.i2("width: " + width + "  height: " + height);
                        helper.setImageBitmap(R.id.img, thumbnail);
                    }
                } else {
                    // If there is no thumbnail image data, the image
                    // will have been stored in the target output URI.
                    LogUtil.i2("data IS null, file saved on target position.");

                    // Resize the full image to fit in out image view.
                    int width = helper.getView(R.id.img).getWidth();
                    int height = helper.getView(R.id.img).getHeight();
                    LogUtil.i2("width: " + width + "  height: " + height);

                    BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                    factoryOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

                    int imageWidth = factoryOptions.outWidth;
                    int imageHeight = factoryOptions.outHeight;

                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(imageWidth / width, imageHeight
                            / height);

                    // Decode the image file into a Bitmap sized to fill the
                    // View
                    factoryOptions.inJustDecodeBounds = false;
                    factoryOptions.inSampleSize = scaleFactor;

                    Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            factoryOptions);

                    helper.setImageBitmap(R.id.img, bitmap);
                }
            } else if (resultCode == RESULT_CANCELED) {
                ToastUtil.show("拍照取消了...");
                // User cancelled the image capture
            } else {
                ToastUtil.show("拍照挂了...");
                // Image capture failed, advise user
            }
        }

        // 如果是录像
        if (requestCode == CODE_VIDEO) {
            LogUtil.i2("CAPTURE_VIDEO");

            if (resultCode == RESULT_OK) {
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

    /**
     * 打开摄像头拍照，回调获取照片缩略图
     * 缩略图从回调 data 的 键名"data" 取得
     * @param code 拍照指令码
     */
    public void openCamera4Thumbnail(int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, code);
    }

    /**
     * 打开摄像头拍照，回调后取图片
     * 回调中的 data 参数为null
     * @param code 拍照指令码
     * @param image 指定图片的存储位置
     */
    public void openCamera4Raw(int code, Uri image) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file to save the image
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image);
        startActivityForResult(intent, code);
    }


   /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try {
            // This location works best if you want the created images to be
            // shared between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
            LogUtil.i2("Successfully created mediaStorageDir: " + mediaStorageDir);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i2( "Error in Creating mediaStorageDir: " + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtil.i2("failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public static void show(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CameraPermissionActivity.class);
        context.startActivity(intent);
    }
}
