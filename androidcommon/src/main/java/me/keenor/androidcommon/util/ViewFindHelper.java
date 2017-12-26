package me.keenor.androidcommon.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Author:      chenliuchun
 * Date:        16/8/8
 * Description: 视图辅助工具类，代替findViewById
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class ViewFindHelper {

    /**
     * 当前布局的视图集合
     */
    private final SparseArray<View> mViewMap = new SparseArray<>();

    /**
     * 当前页面的根视图，布局文件的父视图，对于activity而言，本质为FrameLayout
     */
    private View mContentView;

    /**
     * 上下文环境
     */
    private Context mContext;

    /**
     * 适用于activity
     * 在setContentView后可以使用
     * @param activity
     */
    public ViewFindHelper(AppCompatActivity activity) {
        mContext = activity.getBaseContext();
        mContentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

    }

    /**
     * 适用于activity
     * 代替setContentView
     * @param activity
     * @param layoutId
     */
    public ViewFindHelper(AppCompatActivity activity, int layoutId) {
        mContext = activity.getBaseContext();
        activity.setContentView(layoutId);
        mContentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    /**
     * 适用于fragment
     * @param inflater
     * @param container
     * @param layoutId
     */
    public ViewFindHelper(LayoutInflater inflater, ViewGroup container, int layoutId) {
        mContext = inflater.getContext();
        mContentView = inflater.inflate(layoutId, container, false);
    }

    /**
     * 适用于其他组件内
     * @param context
     * @param layoutId
     */
    public ViewFindHelper(Context context, int layoutId) {
        mContext = context;
        mContentView = LayoutInflater.from(context).inflate(layoutId, null);
    }

    /**
     * 获取根布局View，一般是是activity根布局的父布局
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViewMap.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view == null) {
                throw new NullPointerException("findViewById id: " + viewId + " 未找到");
            }
            mViewMap.put(viewId, view);
        }
        return (T) view;
    }


    // 以下为辅助方法

    /**
     * TextView相关
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewFindHelper setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ViewFindHelper setText(int viewId, int resid) {
        TextView textView = getView(viewId);
        textView.setText(resid);
        return this;
    }

    public ViewFindHelper setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewFindHelper setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * TextView, EditText
     * @param viewId
     * @return
     */
    public String getText(int viewId) {
        TextView textView = getView(viewId);
        return textView.getText().toString();
    }

    /**
     * EditText相关
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewFindHelper setEditText(int viewId, CharSequence text) {
        EditText editText = getView(viewId);
        editText.setText(text);
        return this;
    }

    public ViewFindHelper setEditText(int viewId, int resid) {
        EditText editText = getView(viewId);
        editText.setText(resid);
        return this;
    }

    public ViewFindHelper setEditHint(int viewId, CharSequence text) {
        EditText editText = getView(viewId);
        editText.setHint(text);
        return this;
    }

    public ViewFindHelper setEditHint(int viewId, int resid) {
        EditText editText = getView(viewId);
        editText.setHint(resid);
        return this;
    }

    /**
     * ImageView
     * @param viewId
     * @param resId
     * @return
     */
    public ViewFindHelper setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewFindHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewFindHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public Drawable getImageDrawable(int viewId) {
        return ContextCompat.getDrawable(mContext, viewId);
    }

    public ViewFindHelper setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewFindHelper setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    @SuppressLint("NewApi")
    public ViewFindHelper setAlpha(int viewId, float value) {
        getView(viewId).setAlpha(value);
        return this;
    }

    public ViewFindHelper setVisible(int viewId, boolean visible) {
        getView(viewId).setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewFindHelper linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewFindHelper setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewFindHelper setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public int setProgress(int viewId) {
        ProgressBar view = getView(viewId);
        return view.getProgress();
    }

    public ViewFindHelper setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewFindHelper setProgressMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public int getProgressMax(int viewId) {
        ProgressBar view = getView(viewId);
        return view.getMax();
    }

    public ViewFindHelper setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public float getRating(int viewId) {
        RatingBar view = getView(viewId);
        return view.getRating();
    }

    public ViewFindHelper setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewFindHelper setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public Object getTag(int viewId) {
        return getView(viewId).getTag();
    }

    public ViewFindHelper setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewFindHelper setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public boolean getChecked(int viewId) {
        Checkable view = getView(viewId);
        return view.isChecked();
    }

    /**
     * 关于事件的
     */
    public ViewFindHelper setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewFindHelper setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewFindHelper setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}
