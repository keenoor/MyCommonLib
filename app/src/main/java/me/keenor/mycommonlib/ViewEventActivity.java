package me.keenor.mycommonlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import me.keenor.androidcommon.util.LogUtil;
import me.keenor.androidcommon.util.ViewFindHelper;

/**
 * Author:      chenliuchun
 * Date:        17/3/29
 * Description: 一个全屏自由滑动的 View
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */
public class ViewEventActivity extends AppCompatActivity implements View.OnTouchListener{

    private int lastX;
    private int lastY;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewFindHelper helper = new ViewFindHelper(this, R.layout.activity_view_event);
        view = helper.getView(R.id.view);
        view.setOnTouchListener(this);


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastX;
                int deltaY = y - lastY;
                LogUtil.i1("move deltaX: " + deltaX + "  deltaY:" + deltaY);

                view.setTranslationX(view.getTranslationX() + deltaX);
                view.setTranslationY(view.getTranslationY() + deltaY);

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        lastX = x;
        lastY = y;
        // 表示消耗了此次事件，否则 View 不会响应
        return true;
    }

    public static void show(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ViewEventActivity.class);
        context.startActivity(intent);
    }
}
