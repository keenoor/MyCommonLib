/*
 *   Copyright (C)  2016 android@19code.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package me.keenor.androidcommon.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:      chenliuchun
 * Date:        17/3/23
 * Description: 屏幕相关工具类
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class DensityUtil {

    public static float dp2px(float dip) {
        return dip * Resources.getSystem().getDisplayMetrics().density;
    }

    public static int px2dp(float pixel) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pixel / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(float spValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @return 像素
     */
    public static int getScreenW() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return 像素
     */
    public static int getScreenH() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取状态栏的高度
     * @return 像素
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获得 view 的宽高尺寸
     *
     * @param view
     * @return
     */
    public static Point getViewSize(View view) {
        Point size = new Point();
        ViewGroup.LayoutParams params = view.getLayoutParams();

        int width = view.getWidth();
        // 如果没有初始化显示，从所在布局中取
        if (width <= 0) {
            LogUtil.e1("不能直接获取View的宽度");
            width = params.width;
        }
        // 最后的是取屏幕尺寸
        if (width <= 0) {
            width = DensityUtil.getScreenW();
        }

        int height = view.getHeight();
        // 如果没有初始化显示，从所在布局中取
        if (height <= 0) {
            LogUtil.e1("不能直接获取View的高度");
            height = params.height;
        }
        // 最后的是取屏幕尺寸
        if (height <= 0) {
            height = DensityUtil.getScreenH();
        }

        size.set(width, height);
        return size;
    }


}
