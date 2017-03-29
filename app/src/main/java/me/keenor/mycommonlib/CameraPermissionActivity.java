package me.keenor.mycommonlib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

import me.keenor.androidcommon.ui.ToastUtil;
import me.keenor.androidcommon.util.BitmapUtil;
import me.keenor.androidcommon.util.DensityUtil;
import me.keenor.androidcommon.util.LogUtil;
import me.keenor.androidcommon.util.MediaUtil;
import me.keenor.androidcommon.util.ViewFindHelper;
import me.keenor.mycommonlib.util.ProjectStorageUtil;


/**
 * Author:      chenliuchun
 * Date:        17/3/22
 * Description: 1. 6.0 以上系统的权限处理的官方 demo，还可使用第三方库；
 * 2. 相机调用 demo；
 * <p>
 * 参    考：https://developer.android.com/training/permissions/requesting.html#perm-request
 * https://developer.android.com/guide/topics/security/permissions.html#normal-dangerous
 * http://blog.csdn.net/self_study/article/details/50186435
 * <p>
 * 注    意：
 * 1. 拍照后回调里的照片，默认的是 thumbnail，不适合完整展示，用的不多，
 * 更多的是发送 intent 时候传入 URI 对象，然后得到回调后从原位置取回；
 * 2. 照片压缩默认的方法是 BitmapFactory 的经典方法；
 * 3.
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class CameraPermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CODE_IMAGE = 0x100;
    private static final int CODE_VIDEO = 0x200;
    private static final int CODE_ALBUM = 0x300;
    private static final int CODE_CROP = 0x400;

    private ViewFindHelper helper;

    /**
     * 拍照图片 URI
     */
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new ViewFindHelper(this, R.layout.activity_camera_permission);
        helper.setOnClickListener(R.id.btn_camera, this);
        helper.setOnClickListener(R.id.btn_video, this);
        helper.setOnClickListener(R.id.btn_album, this);
        helper.setOnClickListener(R.id.btn_crop, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i2("onActivityResult: requestCode: " + requestCode
                + ", resultCode: " + requestCode + ", data: " + data);

        // 如果是拍照
        if (CODE_IMAGE == requestCode) {
            if (RESULT_OK == resultCode) {
                if (data != null) {
                    // 没有指定特定存储路径的时候
                    LogUtil.i2("data is NOT null, file on default position.");

                    // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);）
                    // Image captured and saved to imageUri specified in the Intent
                    if (data.hasExtra("data")) {
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        LogUtil.i2("width: " + thumbnail.getWidth() + "  height: " + thumbnail.getHeight());
                        helper.setImageBitmap(R.id.img, thumbnail);
                    }
                } else {
                    Point size = DensityUtil.getViewSize(helper.getView(R.id.img));
                    Bitmap bitmap = BitmapUtil.compressBitmap(imageUri.getPath(), size.x, size.y);
                    helper.setImageBitmap(R.id.img, bitmap);
                }
            } else if (resultCode == RESULT_CANCELED) {
                ToastUtil.show("拍照取消了...");
            } else {
                ToastUtil.show("拍照挂了...");
            }
        }

        // 如果是录像
        if (requestCode == CODE_VIDEO) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                final VideoView videoView = helper.getView(R.id.video);
                videoView.setVideoURI(videoUri);
                videoView.setMediaController(new MediaController(this));
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.start();
                    }
                });
            } else if (resultCode == RESULT_CANCELED) {
                ToastUtil.show("录像取消了...");
            } else {
                ToastUtil.show("录像挂了...");
            }
        }

        // 如果是相册选取
        if (requestCode == CODE_ALBUM) {
            if (resultCode == RESULT_OK) {
                String path = MediaUtil.getRealFilePath(data.getData());
                Point size = DensityUtil.getViewSize(helper.getView(R.id.img));
                Bitmap bitmap = BitmapUtil.compressBitmap(path, size.x, size.y);
                helper.setImageBitmap(R.id.img, bitmap);
            } else if (resultCode == RESULT_CANCELED) {
                ToastUtil.show("取消了...");
            } else {
                ToastUtil.show("挂了...");
            }
        }
        // 如果是获取剪裁图片
        if (requestCode == CODE_CROP) {
            if (resultCode == RESULT_OK) {
                Point size = DensityUtil.getViewSize(helper.getView(R.id.img));
                Bitmap bitmap = BitmapUtil.compressBitmap(imageUri.getPath(), size.x, size.y);
                LogUtil.i2(imageUri.toString());
                helper.setImageBitmap(R.id.img, bitmap);
            } else if (resultCode == RESULT_CANCELED) {
                ToastUtil.show("取消了...");
            } else {
                ToastUtil.show("挂了...");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                imageUri = Uri.fromFile(getOutputImage());
                MediaUtil.takePicture(CODE_IMAGE, imageUri, this);
                helper.setVisible(R.id.img, true);
                helper.setVisible(R.id.video, false);
                break;
            case R.id.btn_video:
                MediaUtil.takeVideo(CODE_VIDEO, this);
                helper.setVisible(R.id.img, false);
                helper.setVisible(R.id.video, true);
                break;
            case R.id.btn_album:
                MediaUtil.takeAlbum(CODE_ALBUM, this);
                helper.setVisible(R.id.img, true);
                helper.setVisible(R.id.video, false);
                break;
            case R.id.btn_crop:
                imageUri = Uri.fromFile(getOutputImage());
                MediaUtil.takeCropPicture(imageUri, 600, 600, CODE_CROP, this);
                LogUtil.i2(imageUri.toString());
                helper.setVisible(R.id.img, true);
                helper.setVisible(R.id.video, false);
                break;
        }
    }

    /**
     * 创建一个存储图片的文件对象
     */
    private static File getOutputImage() {
        File mediaStorageDir = ProjectStorageUtil.getExternalPublicDir(Environment.DIRECTORY_PICTURES, "MyCameraApp");
        if (mediaStorageDir == null) {
            return null;
        }
        return new File(mediaStorageDir + File.separator + ProjectStorageUtil.getImageName());
    }

    public static void show(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CameraPermissionActivity.class);
        context.startActivity(intent);
    }
}
