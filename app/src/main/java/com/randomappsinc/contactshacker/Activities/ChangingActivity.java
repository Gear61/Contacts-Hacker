package com.randomappsinc.contactshacker.Activities;

import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.contactshacker.Models.ProgressEvent;
import com.randomappsinc.contactshacker.Models.SnackbarEvent;
import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class ChangingActivity extends StandardActivity {
    protected String logTag;
    protected MaterialDialog progressDialog;

    @Bind(R.id.parent) View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.hacking_progress)
                .content(R.string.changing_contacts)
                .progress(false, 100, true)
                .cancelable(false)
                .build();
    }

    public void setUpLayout(int layoutId) {
        setContentView(layoutId);
        ButterKnife.bind(this);
    }

    @Subscribe
    public void onEvent(ProgressEvent event) {
        if (event.getScreen().equals(logTag)) {
            switch (event.getEventType()) {
                case ProgressEvent.SET_MAX:
                    progressDialog.setMaxProgress(event.getTotal());
                    break;
                case ProgressEvent.INCREMENT:
                    progressDialog.incrementProgress(1);
                    break;
            }
        }
    }

    @Subscribe
    public void onEvent(SnackbarEvent event) {
        if (event.getScreen().equals(logTag)) {
            progressDialog.dismiss();
            if (event.getMessage().equals(getString(R.string.contacts_success))) {
                UIUtils.showChangedSnackbar(parent, this);
            } else {
                UIUtils.showSnackbar(parent, event.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
