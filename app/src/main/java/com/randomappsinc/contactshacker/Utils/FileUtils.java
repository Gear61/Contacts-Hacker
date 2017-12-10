package com.randomappsinc.contactshacker.Utils;

import android.Manifest;
import android.os.Environment;

import com.randomappsinc.contactshacker.Models.Contact;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private static final String CONTACTS_KEY = "contacts";
    private static final String ID_KEY = "id";
    private static final String DISPLAY_NAME_KEY = "displayName";

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

    static boolean createBackup(List<Contact> contacts) {
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

    static String convertStreamToString(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    static List<Contact> getContactsFromBackup() {
        List<Contact> contacts = new ArrayList<>();

        try {
            File backup = new File(Environment.getExternalStorageDirectory().getPath() + "/ContactsHacker", "backup.txt");
            FileInputStream fileInputStream = new FileInputStream(backup);
            String contactsText = convertStreamToString(fileInputStream);

            JSONObject backupJson = new JSONObject(contactsText);
            JSONArray contactsArray = backupJson.getJSONArray(CONTACTS_KEY);
            for (int i = 0; i < contactsArray.length(); i++) {
                Contact contact = new Contact();
                contact.setId(contactsArray.getJSONObject(i).getString(ID_KEY));
                contact.setDisplayName(contactsArray.getJSONObject(i).getString(DISPLAY_NAME_KEY));
                contacts.add(contact);
            }

            fileInputStream.close();
        } catch (Exception ignored) {}

        return contacts;
    }

    static void deleteBackup() {
        File backup = new File(Environment.getExternalStorageDirectory().getPath() + "/ContactsHacker", "backup.txt");
        backup.delete();
    }
}
