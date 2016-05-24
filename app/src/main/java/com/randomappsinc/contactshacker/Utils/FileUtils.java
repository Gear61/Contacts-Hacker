package com.randomappsinc.contactshacker.Utils;

import android.Manifest;
import android.os.Environment;

import com.randomappsinc.contactshacker.Contact;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class FileUtils {
    public static final String CONTACTS_KEY = "contacts";
    public static final String ID_KEY = "id";
    public static final String DISPLAY_NAME_KEY = "displayName";

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

    public static boolean createBackup(List<Contact> contacts) {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File backup = new File(Environment.getExternalStorageDirectory().getPath() + "/ContactsHacker", "backup.txt");
            if (!backup.exists()) {
                try {
                    JSONObject contactsJson = new JSONObject();

                    JSONArray contactsArray = new JSONArray();
                    for (Contact contact : contacts) {
                        JSONObject contactJson = new JSONObject();
                        contactJson.put(ID_KEY, contact.getId());
                        contactJson.put(DISPLAY_NAME_KEY, contact.getDisplayName());
                        contactsArray.put(contactJson);
                    }
                    contactsJson.put(CONTACTS_KEY, contactsArray);

                    FileWriter fw = new FileWriter(backup);
                    fw.write(contactsJson.toString());
                    fw.close();
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
