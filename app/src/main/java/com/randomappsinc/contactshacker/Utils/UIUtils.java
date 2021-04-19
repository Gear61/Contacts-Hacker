package com.randomappsinc.contactshacker.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.randomappsinc.contactshacker.R;

public class UIUtils {

    public static void showSnackbar(View parent, String message) {
        Context context = MyApplication.getAppContext();
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.app_gray));
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showChangedSnackbar(View parent, final Context context) {
        Snackbar snackbar = Snackbar.make(parent, context.getString(R.string.contacts_success_verbose),
                Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.app_gray));
        snackbar.setAction(android.R.string.yes, v -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.android.contacts",
                    "com.android.contacts.DialtactsContactsEntryActivity"));
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addCategory("android.intent.category.DEFAULT");
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.view_contacts)));
        });
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }
}