package me.keenor.mycommonlib;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
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
                .setOnClickListener(R.id.btn_06, this);

    }

    @Override
    public void onClick(View v) {
        ContentLoadingProgressBar progressBar = viewHelper.getView(R.id.progress02);

        switch (v.getId()) {
            case R.id.btn_01:
                progressBar.hide();

                break;
            case R.id.btn_02:
                progressBar.show();

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

                break;
            case R.id.btn_05:

                break;
            case R.id.btn_06:

                break;
            case R.id.btn_07:

                break;
            case R.id.btn_08:

                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, CharSequence tag, int which) {
        ToastUtil.show(tag + " this is from activity " + which);
    }

}
