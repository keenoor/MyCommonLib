package me.keenor.androidcommon.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Method;

import me.keenor.androidcommon.ContextUtil;

/**
 * Author:      chenliuchun
 * Date:        17/3/13
 * Description: 对 DialogFragment 和 AlertDialog 的封装，用于标准对话框创建；
 * 优    势：1. 可以像使用 AlertDialog 一样使用 DialogFragment；
 *          2. 防止屏幕旋转后页面重建导致监听器失效；
 *          3. 监听器回调提供 tag 判断；
 * 注意事项： 1. setPositiveBtn （模式 1）和 setPositiveBtnRef（模式 2） 不可同时使用，否则后者不生效，
 *          2. 模式 1 不支持DialogFragment重建，模式 2 支持；
 *          3. 如果当前activity没有设置 android:configChanges="orientation|screenSize" 导致 activity 重建，
 *          那么，建议使用 setPositiveBtnRef 方法，否则旋转屏幕后，监听器失效；
 *          4. 通过 setPositiveBtnRef 方式，并且使用匿名内部类的方式创建多个监听器，复杂情况下有可能会出现监听器不正确的情况，仅仅猜测，暂未复现；
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class CommonDialogFragment extends DialogFragment {

    // bundle 存值得 key
    static final String TITLE = "title";
    static final String MESSAGE = "message";
    public static final String ICON_ID = "icon_id";
    public static final String POSITIVE_BTN_TEXT = "positive_btn_text";
    public static final String NEGATIVE_BTN_TEXT = "negative_btn_text";
    public static final String NEUTRAL_BTN_TEXT = "neutral_btn_text";
    public static final String POSITIVE_BTN_REF = "positive_btn_ref";
    public static final String NEGATIVE_BTN_REF = "negative_btn_ref";
    public static final String NEUTRAL_BTN_REF = "neutral_btn_ref";
    public static final String ITEMS = "items";
    public static final String LIST_REF = "list_ref";

    // 监听器数组对应的索引值
    private static final int INDEX_POSITIVE = 0;
    private static final int INDEX_NEGATIVE = 1;
    private static final int INDEX_NEUTRAL = 2;
    private static final int INDEX_LIST = 3;

    private CharSequence title;
    private CharSequence message;
    private int iconId;
    private CharSequence positiveBtnText;
    private String positiveBtnRef;
    private CharSequence negativeBtnText;
    private String negativeBtnRef;
    private CharSequence neutralBtnText;
    private String neutralBtnRef;
    private CharSequence[] items;
    private String listRef;

    // 模式1 下使用的监听器数组
    private DialogListener.OnClickListener[] clickListenerArray = new DialogListener.OnClickListener[4];

    /**
     * 标准对话框实例创建
     * @return
     */
    public static CommonDialogFragment newInstance() {
        return new CommonDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();

        // 基本数据类型的传递在此完成
        builder.setTitle(bundle.getCharSequence(TITLE))
                .setMessage(bundle.getCharSequence(MESSAGE))
                .setIcon(bundle.getInt(ICON_ID));

        // 模式 1 的监听器设置
        if (indexOfNotNull(clickListenerArray) != -1) {
            builder
                    .setPositiveButton(bundle.getCharSequence(POSITIVE_BTN_TEXT), convertListener(clickListenerArray[INDEX_POSITIVE]))
                    .setNegativeButton(bundle.getCharSequence(NEGATIVE_BTN_TEXT), convertListener(clickListenerArray[INDEX_NEGATIVE]))
                    .setNeutralButton(bundle.getCharSequence(NEUTRAL_BTN_TEXT), convertListener(clickListenerArray[INDEX_NEUTRAL]))
                    .setItems(bundle.getCharSequenceArray(ITEMS), convertListener(clickListenerArray[INDEX_LIST]));
            return builder.create();

            // 模式 2 的监听器设置
        } else {
            positiveBtnRef = bundle.getString(POSITIVE_BTN_REF);
            negativeBtnRef = bundle.getString(NEGATIVE_BTN_REF);
            neutralBtnRef = bundle.getString(NEUTRAL_BTN_REF);
            listRef = bundle.getString(LIST_REF);

            Object object = null;
            // 当前页面是 activity
            if (getParentFragment() == null) {
                if (getActivity() instanceof DialogListener.OnClickListener) {
                    object = getActivity();
                }
                // 当前页面是 fragment
            } else {
                if (getParentFragment() instanceof DialogListener.OnClickListener) {
                    object = getParentFragment();
                }
            }
            builder
                    .setPositiveButton(bundle.getCharSequence(POSITIVE_BTN_TEXT), convertListener(object, positiveBtnRef))
                    .setNegativeButton(bundle.getCharSequence(NEGATIVE_BTN_TEXT), convertListener(object, negativeBtnRef))
                    .setNeutralButton(bundle.getCharSequence(NEUTRAL_BTN_TEXT), convertListener(object, neutralBtnRef))
                    .setItems(bundle.getCharSequenceArray(ITEMS), convertListener(object, listRef));
            return builder.create();
        }
    }

    /**
     * 模式 1 的监听器转换
     * @param listener
     * @return
     */
    private DialogInterface.OnClickListener convertListener(final DialogListener.OnClickListener listener) {
        if (listener != null ){
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onClick(dialog, getTag(), which);
                }
            };
        } else {
            return null;
        }
    }

    /**
     * 模式 2 的监听器转换，通过监听器全名
     * @param object
     * @param className
     * @return
     */
    private DialogInterface.OnClickListener convertListener(final Object object, final String className) {
        if (className != null) {
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        final Class<?> clazz = Class.forName(className);
                        final Method onClick = clazz.getMethod("onClick", DialogInterface.class, CharSequence.class, int.class);
                        onClick.invoke(object, dialog, getTag(), which);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } else {
            return null;
        }
    }

    /**
     * 返回给出数组的第一个非空索引值
     * @param array
     * @param <T>
     * @return
     */
    public <T> int indexOfNotNull(T [] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                return i;
            }
        }
        return -1;
    }

    public CommonDialogFragment setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public CommonDialogFragment setTitle(int title) {
        this.title = ContextUtil.getContext().getText(title);
        return this;
    }

    public CommonDialogFragment setMessage(CharSequence message) {
        this.message = message;
        return this;
    }

    public CommonDialogFragment setMessage(int message) {
        this.message = ContextUtil.getContext().getText(message);
        return this;
    }

    public CommonDialogFragment setIconId(int iconId) {
        this.iconId = iconId;
        return this;
    }

    public CommonDialogFragment setPositiveBtn(CharSequence positiveBtnText, DialogListener.OnClickListener listener) {
        this.positiveBtnText = positiveBtnText;
        clickListenerArray[INDEX_POSITIVE] = listener;
        return this;
    }

    public CommonDialogFragment setPositiveBtnRef(CharSequence positiveBtnText, DialogListener.OnClickListener listener) {
        this.positiveBtnText = positiveBtnText;
        if (listener != null) {
            positiveBtnRef = listener.getClass().getName();
        }
        return this;
    }

    public CommonDialogFragment setNegativeBtn(CharSequence negativeBtnText, DialogListener.OnClickListener listener) {
        this.negativeBtnText = negativeBtnText;
        clickListenerArray[INDEX_NEGATIVE] = listener;
        return this;
    }

    public CommonDialogFragment setNegativeBtnRef(CharSequence negativeBtnText, DialogListener.OnClickListener listener) {
        this.negativeBtnText = negativeBtnText;
        if (listener != null) {
            negativeBtnRef = listener.getClass().getName();
        }
        return this;
    }

    public CommonDialogFragment setNeutralBtn(CharSequence neutralBtnText, DialogListener.OnClickListener listener) {
        this.neutralBtnText = neutralBtnText;
        clickListenerArray[INDEX_NEUTRAL] = listener;
        return this;
    }

    public CommonDialogFragment setNeutralBtnRef(CharSequence neutralBtnText, DialogListener.OnClickListener listener) {
        this.neutralBtnText = neutralBtnText;
        if (listener != null) {
            neutralBtnRef = listener.getClass().getName();
        }
        return this;
    }

    public CommonDialogFragment setItems(CharSequence[] items, DialogListener.OnClickListener listener) {
        this.items = items;
        clickListenerArray[INDEX_LIST] = listener;
        return this;
    }

    public CommonDialogFragment setItemsRef(CharSequence[] items, DialogListener.OnClickListener listener) {
        this.items = items;
        if (listener != null) {
            listRef = listener.getClass().getName();
        }
        return this;
    }

    /**
     * 将设置的参数存入bundle
     */
    private void apply() {
        Bundle bundle = new Bundle();

        if (title != null) {
            bundle.putCharSequence(TITLE, title);
        }
        if (message != null) {
            bundle.putCharSequence(MESSAGE, message);
        }
        if (iconId != 0) {
            bundle.putInt(ICON_ID, iconId);
        }
        if (positiveBtnText != null) {
            bundle.putCharSequence(POSITIVE_BTN_TEXT, positiveBtnText);
        }
        if (negativeBtnText != null) {
            bundle.putCharSequence(NEGATIVE_BTN_TEXT, negativeBtnText);
        }
        if (neutralBtnText != null) {
            bundle.putCharSequence(NEUTRAL_BTN_TEXT, neutralBtnText);
        }
        if (positiveBtnRef != null) {
            bundle.putString(POSITIVE_BTN_REF, positiveBtnRef);
        }
        if (negativeBtnRef != null) {
            bundle.putString(NEGATIVE_BTN_REF, negativeBtnRef);
        }
        if (neutralBtnRef != null) {
            bundle.putString(NEUTRAL_BTN_REF, neutralBtnRef);
        }
        if (items != null) {
            bundle.putCharSequenceArray(ITEMS, items);
        }
        if (listRef != null) {
            bundle.putString(LIST_REF, listRef);
        }

        setArguments(bundle);
    }

    public void show(AppCompatActivity activity, String tag) {
        apply();
        show(activity.getFragmentManager(), tag);
    }

    public void show(Fragment fragment, String tag) {
        apply();
        show(fragment.getFragmentManager(), tag);
    }

}

