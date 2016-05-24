package com.randomappsinc.contactshacker.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.PermissionUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends StandardActivity {
    public static final int WRITE_CONTACTS_CODE = 1;
    public static final int WRITE_EXTERNAL_CODE = 2;

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
            PermissionUtils.requestPermission(this, Manifest.permission.WRITE_CONTACTS, WRITE_CONTACTS_CODE);
        } else if (!PermissionUtils.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            processWrite();
        }
    }

    private void askForWrite() {
        PermissionUtils.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_CODE);
    }

    private void processWrite() {
        if (PermissionUtils.shouldShowExplanation(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new MaterialDialog.Builder(this)
                    .content(R.string.write_external_explanation)
                    .cancelable(false)
                    .positiveText(android.R.string.yes)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            askForWrite();
                        }
                    })
                    .show();
        } else {
            askForWrite();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_CONTACTS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    processWrite();
                } else {
                    PermissionUtils.requestPermission(this, Manifest.permission.WRITE_CONTACTS, WRITE_CONTACTS_CODE);
                }
                break;
            case WRITE_EXTERNAL_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    processWrite();
                }
        }
    }

    @OnClick(R.id.single_name)
    public void singleName() {

    }

    @OnClick(R.id.scramble)
    public void scramble() {

    }

    @OnClick(R.id.grab_bag)
    public void grabBag() {
        startActivity(new Intent(this, GrabBagActivity.class));
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
