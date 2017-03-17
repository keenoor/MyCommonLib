package me.keenor.androidcommon.ui.dialog;

import android.content.DialogInterface;

/**
 * Author:      chenliuchun
 * Date:        17/3/13
 * Description: 对 DialogInterface 的包装，增加了 tag 判断
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public interface DialogListener extends DialogInterface {

    interface OnClickListener {
        /**
         *
         * @param dialog
         * @param tag 当前 DialogFragment 的 tag
         * @param which
         */
        public void onClick(DialogInterface dialog, CharSequence tag, int which);
    }

}
