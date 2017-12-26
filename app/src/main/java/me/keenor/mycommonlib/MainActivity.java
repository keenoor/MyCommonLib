package me.keenor.mycommonlib;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.keenor.androidcommon.ui.ToastUtil;
import me.keenor.androidcommon.ui.dialog.CommonDialogFragment;
import me.keenor.androidcommon.ui.dialog.DialogListener;
import me.keenor.androidcommon.util.ViewFindHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogListener.OnClickListener {

    private ViewFindHelper viewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHelper = new ViewFindHelper(this, R.layout.activity_main);

        viewHelper
                .setOnClickListener(R.id.btn_01, this)
                .setOnClickListener(R.id.btn_02, this)
                .setOnClickListener(R.id.btn_03, this)
                .setOnClickListener(R.id.btn_04, this)
                .setOnClickListener(R.id.btn_05, this)
                .setOnClickListener(R.id.btn_06, this)
                .setOnClickListener(R.id.btn_07, this)
                .setOnClickListener(R.id.btn_08, this);


        // test

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_01:

                break;
            case R.id.btn_02:


                break;
            case R.id.btn_03:
                CommonDialogFragment.newInstance()
                        .setTitle("title")
                        .setMessage(R.string.app_name)
                        .setPositiveBtn("OKOK", new DialogListener.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, CharSequence tag, int which) {
                                ToastUtil.show(tag + " this is from innerClass " + which);
                            }
                        })
                        .setNegativeBtn("CANCEL", this)
                        .show(this, "dialog");

                break;
            case R.id.btn_04:
                StorageActivity.show(this);
                break;
            case R.id.btn_05:
                CameraPermissionActivity.show(this);
                break;
            case R.id.btn_06:
                ViewEventActivity.show(this);
                break;
            case R.id.btn_07:

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                startActivity(intent);
                break;
            case R.id.btn_08:
                String url="mqqwpa://im/chat?chat_type=wpa&uin=2101607976";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));//指定的QQ号只需要修改uin后的值即可。
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, CharSequence tag, int which) {
        ToastUtil.show(tag + " this is from activity " + which);
    }

}
