package me.keenor.androidcommon.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Author:      chenliuchun
 * Date:        17/3/30
 * Description: 视图相关工具类
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class ViewUtil {

    /**
     * 隐藏虚拟键盘
     * @param view 当前焦点的 view 或者根布局
     */
    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) AppUtil.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
