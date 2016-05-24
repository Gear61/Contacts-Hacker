package com.randomappsinc.contactshacker.Activities;

import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.contactshacker.Models.ProgressEvent;
import com.randomappsinc.contactshacker.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class ChangingActivity extends StandardActivity {
    protected String logTag;
    protected MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.hacking_progress)
                .title(R.string.changing_contacts)
                .progress(false, 100, true)
                .cancelable(false)
                .build();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
