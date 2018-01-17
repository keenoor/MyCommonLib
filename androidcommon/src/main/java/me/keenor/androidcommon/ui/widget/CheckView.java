package me.keenor.androidcommon.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import me.keenor.androidcommon.R;


/**
 * Author:      chenliuchun
 * Date:        2017/12/22
 * Description: 对勾动画demo，只需一张对勾切图即可完整类似帧动画的效果
 * 注意：粗糙版
 * 由于图片长度可能无法整除帧数，造成误差累计，导致绘制不全，需要均匀化切片长度
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class CheckView extends View {

    private static final int ANIM_NULL = 0;         //动画状态-没有
    private static final int ANIM_CHECK = 1;        //动画状态-开启
    private static final int ANIM_UNCHECK = 2;      //动画状态-结束

    private int mWidth, mHeight;        // 宽高
    private Handler mHandler;           // handler

    private Paint mPaint;
    private Bitmap okBitmap;

    private int currentFrame = 0;       // 当前页码
    private int maxFrame = 12;           // 总帧数, 可能有无法整除的问题，造成绘制不全
    private int duration = 500;         // 动画时长
    private int animState = ANIM_NULL;      // 动画状态

    private boolean isCheck = false;        // 是否只选中状态

    public CheckView(Context context) {
        super(context);

    }

    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    @SuppressLint("HandlerLeak")
    private void init() {

        mPaint = new Paint();
        mPaint.setColor(0x669e9e9e);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        okBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_tick);

        // TODO: 2017/12/27 考虑 postInvalidateDelayed
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (currentFrame >= 0 && currentFrame < maxFrame) {
                    Log.i("***CLC***", "currentFrame: " + currentFrame);
                    if (animState == ANIM_NULL)
                        return;
                    if (animState == ANIM_CHECK) {
                        currentFrame++;
                    } else if (animState == ANIM_UNCHECK) {
                        currentFrame--;
                    }
                    invalidate();
                    sendEmptyMessageDelayed(0, duration / maxFrame);
                } else {
                    animState = ANIM_NULL;
                }
            }
        };
    }

    /**
     * View大小确定
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    /**
     * 绘制内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 移动坐标系到画布中央
        canvas.translate(mWidth / 2, mHeight / 2);

        // 绘制背景圆形
        canvas.drawCircle(0, 0, 240, mPaint);

        // 得出图像边长
        int sideLength = okBitmap.getWidth();
        // 竖向切片每等分的宽度
        int slice = (int) (1.0f * sideLength / maxFrame);

        // 得到图像选区 和 实际绘制位置
        Log.i("***CLC***", "slice: "+slice + " currentPage: "+ currentFrame );
        // 源图片
        Rect src = new Rect(0, 0, slice * currentFrame, sideLength);
        // 目标绘制区域
        RectF dst = new RectF(src);
        dst.offset(-sideLength / 2.0f, -sideLength / 2.0f);

        // 绘制
        canvas.drawBitmap(okBitmap, src, dst, null);
    }

    /**
     * 选择
     */
    public void check() {
        if (animState != ANIM_NULL || isCheck)
            return;
        animState = ANIM_CHECK;
        currentFrame = 0;
        mHandler.sendEmptyMessageDelayed(0, duration / maxFrame);
        isCheck = true;
    }

    /**
     * 取消选择
     */
    public void unCheck() {
        if (animState != ANIM_NULL || (!isCheck))
            return;
        animState = ANIM_UNCHECK;
        currentFrame = maxFrame -1;
        mHandler.sendEmptyMessageDelayed(0, duration / maxFrame);
        isCheck = false;
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    public void setDuration(int duration) {
        if (duration <= 0)
            return;
        this.duration = duration;
    }

    /**
     * 设置背景圆形颜色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
    }
}

