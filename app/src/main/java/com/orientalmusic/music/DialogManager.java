package com.orientalmusic.music;

import android.content.Context;
import android.view.View;

import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

/**
 * Created by kameloov on 12/24/2017.
 */

public class DialogManager {
    public static void infoDialog(Context context, String message) {
        new LovelyInfoDialog(context)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.mipmap.dialog_info)
                .setTitle("Info")
                .setMessage(message)
                .show();

    }

    public static void yesNoDialog(Context context, String message, View.OnClickListener yesListener) {
        new LovelyStandardDialog(context)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setTitle(context.getString(R.string.title_info))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.btn_yes), yesListener)
                .show();
    }
}
