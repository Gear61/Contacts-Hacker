package com.randomappsinc.contactshacker.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.ContactUtils;
import com.randomappsinc.contactshacker.Utils.FileUtils;
import com.randomappsinc.contactshacker.Utils.PermissionUtils;
import com.randomappsinc.contactshacker.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends StandardActivity {
    public static final int WRITE_CONTACTS_CODE = 1;
    public static final int READ_CONTACTS_CODE = 2;
    public static final int WRITE_EXTERNAL_CODE = 3;

    @Bind(R.id.parent) View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kill activity if it's above an existing stack due to launcher bug
        if (!isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            processPermission(R.string.write_contacts_explanation,
                    Manifest.permission.WRITE_CONTACTS, WRITE_CONTACTS_CODE);
        } else if (!PermissionUtils.isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            processPermission(R.string.read_contacts_explanation,
                    Manifest.permission.READ_CONTACTS, READ_CONTACTS_CODE);
        } else if (!PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            processPermission(R.string.write_external_explanation,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_CODE);
        }
    }

    private void askForPermission(String permission, int code) {
        PermissionUtils.requestPermission(this, permission, code);
    }

    private void processPermission(int body, final String permission, final int code) {
        new MaterialDialog.Builder(this)
                .content(body)
                .positiveText(android.R.string.yes)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        askForPermission(permission, code);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_CONTACTS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    processPermission(R.string.read_contacts_explanation,
                            Manifest.permission.READ_CONTACTS, READ_CONTACTS_CODE);
                } else {
                    processPermission(R.string.write_contacts_explanation,
                            Manifest.permission.WRITE_CONTACTS, WRITE_CONTACTS_CODE);
                }
                break;
            case READ_CONTACTS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    processPermission(R.string.write_external_explanation,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_CODE);
                } else {
                    processPermission(R.string.read_contacts_explanation,
                            Manifest.permission.READ_CONTACTS, READ_CONTACTS_CODE);
                }
                break;
            case WRITE_EXTERNAL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileUtils.createExternalDirectory();
                } else {
                    processPermission(R.string.write_external_explanation,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_CODE);
                }
        }
    }

    @OnClick(R.id.single_name)
    public void singleName() {
        new MaterialDialog.Builder(this)
                .content(R.string.single_name_body)
                .alwaysCallInputCallback()
                .inputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .input(getString(R.string.name), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        dialog.getActionButton(DialogAction.POSITIVE)
                                .setEnabled(!input.toString().trim().isEmpty());
                    }
                })
                .positiveText(R.string.change)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newName = dialog.getInputEditText().getText().toString().trim();
                        changeToSingleName(newName);
                    }
                })
                .show();
    }

    private void changeToSingleName(String name) {
        if (!ContactUtils.changeToOneName(name, this)) {
            UIUtils.showSnackbar(parent, getString(R.string.contacts_error));
        }
    }

    @OnClick(R.id.scramble)
    public void scramble() {

    }

    @OnClick(R.id.grab_bag)
    public void grabBag() {
        startActivity(new Intent(this, GrabBagActivity.class));
    }

    @OnClick(R.id.undo_changes)
    public void undoChanges() {
        if (FileUtils.doesBackupExist()) {

        } else {
            UIUtils.showSnackbar(parent, getString(R.string.no_changes));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.settings).setIcon(
                new IconDrawable(this, IoniconsIcons.ion_android_settings)
                        .colorRes(R.color.white)
                        .actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
