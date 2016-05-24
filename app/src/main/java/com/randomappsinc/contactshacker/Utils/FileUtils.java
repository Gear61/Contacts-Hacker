package com.randomappsinc.contactshacker.Utils;

import android.Manifest;
import android.os.Environment;

import java.io.File;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class FileUtils {
    // Create external storage directory for our app if it doesn't exist
    public static void createExternalDirectory() {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                File ourDirectory = new File(android.os.Environment.getExternalStorageDirectory(), "ContactsHacker");
                if (!ourDirectory.exists()) {
                    ourDirectory.mkdirs();
                }
            }
        }
    }

    public static boolean doesBackupExist() {
        File backup = new File(Environment.getExternalStorageDirectory().getPath() + "/ContactsHacker", "backup.txt");
        return backup.exists();
    }
}
