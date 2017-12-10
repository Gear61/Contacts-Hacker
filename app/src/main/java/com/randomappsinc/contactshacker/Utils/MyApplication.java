package com.randomappsinc.contactshacker.Utils;

import android.app.Application;
import android.content.Context;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;

public final class MyApplication extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new IoniconsModule());
        instance = getApplicationContext();
        FileUtils.createExternalDirectory();
    }

    public static Context getAppContext() {
        return instance;
    }
}
