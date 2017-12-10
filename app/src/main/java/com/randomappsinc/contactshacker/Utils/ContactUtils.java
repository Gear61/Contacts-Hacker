package com.randomappsinc.contactshacker.Utils;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.randomappsinc.contactshacker.Activities.MainActivity;
import com.randomappsinc.contactshacker.Models.Contact;
import com.randomappsinc.contactshacker.Models.ProgressEvent;
import com.randomappsinc.contactshacker.Models.SnackbarEvent;
import com.randomappsinc.contactshacker.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContactUtils {

    public static void changeToOneName(String name) {
        List<String> singleName = new ArrayList<>();
        singleName.add(name);
        changeContacts(singleName, MainActivity.LOG_TAG);
    }

    private static List<Contact> getCurrentContacts() {
        ContentResolver resolver = MyApplication.getAppContext().getContentResolver();
        final String[] projection = {ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};

        Cursor contactsCursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI, projection,
                ContactsContract.RawContacts._ID + " > 0", null, null);

        List<Contact> currentContacts = new ArrayList<>();

        if (contactsCursor != null) {
            int idColumn = contactsCursor.getColumnIndex(ContactsContract.RawContacts._ID);
            int displayNameColumn = contactsCursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
            while (contactsCursor.moveToNext()) {
                String displayName = contactsCursor.getString(displayNameColumn);
                if (displayName != null && !displayName.equals("null")) {
                    Contact contact = new Contact();
                    contact.setId(contactsCursor.getString(idColumn));
                    contact.setDisplayName(contactsCursor.getString(displayNameColumn));
                    currentContacts.add(contact);
                }
            }
            contactsCursor.close();
        }

        return currentContacts;
    }

    private static void changeContact(String id, String newName, String screen) {
        // CONTACT UPDATE DONE HERE
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation
                .newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts._ID + " LIKE ?", new String[] {id})
                .withValue(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, newName)
                .build());

        try {
            MyApplication.getAppContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            EventBus.getDefault().post(new ProgressEvent(screen, ProgressEvent.INCREMENT, 0));
        }
        catch (Exception ignored) {}
    }

    // Non-scramble mode
    public static void changeContacts(List<String> newNames, String screen) {
        Context context = MyApplication.getAppContext();

        if (!PermissionUtils.isPermissionGranted(Manifest.permission.READ_CONTACTS) ||
            !PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_error)));
            return;
        }

        List<Contact> currentContacts = getCurrentContacts();

        // If the contacts backup fails, prevent them from changing contacts
        if (!FileUtils.createBackup(currentContacts)) {
            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_error)));
        } else {
            EventBus.getDefault().post(new ProgressEvent(screen, ProgressEvent.SET_MAX, currentContacts.size()));

            Random random = new Random();

            // This loop changes all of the contacts
            for (Contact contact : currentContacts) {
                String newName = newNames.get(random.nextInt(newNames.size()));
                changeContact(contact.getId(), newName, screen);
            }

            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_success)));
        }
    }

    public static void scrambleContacts() {
        Context context = MyApplication.getAppContext();
        String screen = MainActivity.LOG_TAG;

        if (!PermissionUtils.isPermissionGranted(Manifest.permission.READ_CONTACTS) ||
                !PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_error)));
            return;
        }

        List<Contact> currentContacts = getCurrentContacts();
        List<String> contactIds = new ArrayList<>();
        for (Contact contact : currentContacts) {
            contactIds.add(contact.getId());
        }

        // If the contacts backup fails, prevent them from changing contacts
        if (!FileUtils.createBackup(currentContacts)) {
            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_error)));
        } else {
            EventBus.getDefault().post(new ProgressEvent(screen, ProgressEvent.SET_MAX, currentContacts.size()));

            Random random = new Random();

            // This loop changes all of the contacts
            for (String contactId: contactIds) {
                int contactIndex = random.nextInt(currentContacts.size());
                String newName = currentContacts.get(contactIndex).getDisplayName();
                currentContacts.remove(contactIndex);

                changeContact(contactId, newName, screen);
            }

            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_success)));
        }
    }

    public static void repairContacts() {
        Context context = MyApplication.getAppContext();
        String screen = MainActivity.LOG_TAG;

        List<Contact> currentContacts = FileUtils.getContactsFromBackup();
        EventBus.getDefault().post(new ProgressEvent(screen, ProgressEvent.SET_MAX, currentContacts.size()));

        // This loop changes all of the contacts
        for (Contact contact: currentContacts) {
            changeContact(contact.getId(), contact.getDisplayName(), screen);
        }

        FileUtils.deleteBackup();
        EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_repaired)));
    }
}
