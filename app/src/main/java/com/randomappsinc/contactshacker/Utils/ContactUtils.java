package com.randomappsinc.contactshacker.Utils;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.contactshacker.Activities.MainActivity;
import com.randomappsinc.contactshacker.Contact;
import com.randomappsinc.contactshacker.Models.ProgressEvent;
import com.randomappsinc.contactshacker.Models.SnackbarEvent;
import com.randomappsinc.contactshacker.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class ContactUtils {
    public static void changeToOneName(String name) {
        List<String> singleName = new ArrayList<>();
        singleName.add(name);
        changeContacts(singleName, MainActivity.LOG_TAG);
    }

    // Non-scramble mode
    public static void changeContacts(List<String> newNames, String screen) {
        Context context = MyApplication.getAppContext();

        if (!PermissionUtils.isPermissionGranted(Manifest.permission.READ_CONTACTS) ||
            !PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_error)));
        }

        ContentResolver resolver = MyApplication.getAppContext().getContentResolver();
        final String[] projection = {ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};

        Cursor contactsCursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI, projection,
                ContactsContract.RawContacts._ID + " > 0", null, null);

        List<Contact> currentContacts = new ArrayList<>();

        if (contactsCursor != null) {
            int idColumn = contactsCursor.getColumnIndex(ContactsContract.RawContacts._ID);
            int displayNameColumn = contactsCursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
            while (contactsCursor.moveToNext()) {
                if (!contactsCursor.getString(displayNameColumn).equals("null")) {
                    Contact contact = new Contact();
                    contact.setId(contactsCursor.getString(idColumn));
                    contact.setDisplayName(contactsCursor.getString(displayNameColumn));
                    currentContacts.add(contact);
                }
            }
            contactsCursor.close();
        }

        // If the contacts backup fails, prevent them from changing contacts
        if (!FileUtils.createBackup(currentContacts)) {
            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_error)));
        } else {
            EventBus.getDefault().post(new ProgressEvent(screen, ProgressEvent.SET_MAX, currentContacts.size()));

            Random random = new Random();

            // This loop changes all of the contacts
            for (Contact contact : currentContacts) {
                ArrayList<ContentProviderOperation> ops = new ArrayList<>();

                String newName = newNames.get(random.nextInt(newNames.size()));

                // CONTACT UPDATE DONE HERE
                ops.add(ContentProviderOperation
                        .newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                        .withSelection(ContactsContract.RawContacts._ID + " LIKE ?", new String[] {contact.getId()})
                        .withValue(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, newName)
                        .build());

                try {
                    resolver.applyBatch(ContactsContract.AUTHORITY, ops);
                    EventBus.getDefault().post(new ProgressEvent(screen, ProgressEvent.INCREMENT, 0));
                }
                catch (Exception ignored) {}
            }

            EventBus.getDefault().post(new SnackbarEvent(screen, context.getString(R.string.contacts_success)));
        }
    }

    // Non-scramble mode
    public static boolean repairContacts(List<String> newNames, Context context) {
        if (!PermissionUtils.isPermissionGranted(Manifest.permission.READ_CONTACTS) ||
                !PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            return false;
        }

        ContentResolver resolver = context.getContentResolver();
        final String[] projection = {ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};

        Cursor contactsCursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI, projection,
                ContactsContract.RawContacts._ID + " > 0", null, null);

        List<Contact> currentContacts = new ArrayList<>();

        if (contactsCursor != null) {
            int idColumn = contactsCursor.getColumnIndex(ContactsContract.RawContacts._ID);
            int displayNameColumn = contactsCursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
            while (contactsCursor.moveToNext()) {
                if (!contactsCursor.getString(displayNameColumn).equals("null")) {
                    Contact contact = new Contact();
                    contact.setId(contactsCursor.getString(idColumn));
                    contact.setDisplayName(contactsCursor.getString(displayNameColumn));
                    currentContacts.add(contact);
                }
            }
            contactsCursor.close();
        }

        // If the contacts backup fails, prevent them from changing contacts
        if (!FileUtils.createBackup(currentContacts)) {
            return false;
        } else {
            MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                    .title(R.string.hacking_progress)
                    .content(R.string.changing_contacts)
                    .progress(false, currentContacts.size(), true)
                    .cancelable(false)
                    .show();

            Random random = new Random();

            // This loop changes all of the contacts
            for (Contact contact : currentContacts) {
                ArrayList<ContentProviderOperation> ops = new ArrayList<>();

                String newName = newNames.get(random.nextInt(newNames.size()));

                // CONTACT UPDATE DONE HERE
                ops.add(ContentProviderOperation
                        .newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                        .withSelection(ContactsContract.RawContacts._ID + " LIKE ?", new String[] {contact.getId()})
                        .withValue(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, newName)
                        .build());

                try {
                    resolver.applyBatch(ContactsContract.AUTHORITY, ops);
                    progressDialog.incrementProgress(1);
                }
                catch (Exception ignored) {}
            }
            return true;
        }
    }
}
