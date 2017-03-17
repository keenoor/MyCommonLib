package me.keenor.androidcommon.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.keenor.androidcommon.util.ViewFindHelper;

/**
 * Author:      chenliuchun
 * Date:        17/3/17
 * Description: 自定义 DialogFragment 的基类
 * 注    意：暂不支持页面销毁重建
 * 使    用：1. 创建newInstance方法，确定入参
 *          2. 复写抽象方法，传入布局文件，处理数据
 *          3. 确定点击监听
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public abstract class BaseDialogFragment extends DialogFragment {

    // 监听器数组对应的索引值
    private static final int INDEX_BUTTON1 = 1;
    private static final int INDEX_BUTTON2 = 2;
    private static final int INDEX_BUTTON3 = 3;

    private View.OnClickListener[] clickListenerArray = new View.OnClickListener[3];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        initData();

        ViewFindHelper viewHelper = new ViewFindHelper(inflater, container, getCustomLayout());

        initView(viewHelper);

        return viewHelper.getContentView();
    }

    /**
     * 获取对话框布局ID
     * @return
     */
    protected abstract int getCustomLayout();

    /**
     * 取出 bundle 内的参数值
     */
    protected abstract void initData();

    /**
     * 初始化对话框视图，传入监听器
     * @param viewHelper
     */
    protected abstract void initView(ViewFindHelper viewHelper);

    /**
     * 设置点击监听器
     * @param listener
     * @return
     */
    protected BaseDialogFragment setButton1(View.OnClickListener listener) {
        clickListenerArray[INDEX_BUTTON1] = listener;
        return this;
    }

    protected BaseDialogFragment setButton2(View.OnClickListener listener) {
        clickListenerArray[INDEX_BUTTON2] = listener;
        return this;
    }

    protected BaseDialogFragment setButton3(View.OnClickListener listener) {
        clickListenerArray[INDEX_BUTTON3] = listener;
        return this;
    }

}
