package com.randomappsinc.contactshacker.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.randomappsinc.contactshacker.Models.SnackbarEvent;
import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.ContactUtils;
import com.randomappsinc.contactshacker.Utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 5/22/16.
 */
public class GrabBagActivity extends ChangingActivity {
    public static final String LOG_TAG = "GrabBagActivity";

    @Bind(R.id.parent) View parent;
    @Bind({R.id.name_1, R.id.name_2, R.id.name_3, R.id.name_4, R.id.name_5}) List<EditText> inputs;

    private List<String> grabBag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logTag = LOG_TAG;

        setContentView(R.layout.grab_bag);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.change_contacts)
    public void changeContacts() {
        UIUtils.hideKeyboard(this);
        List<String> nameInputs = new ArrayList<>();
        for (EditText input : inputs) {
            String nameInput = input.getText().toString().trim();
            if (!nameInput.isEmpty()) {
                nameInputs.add(nameInput);
            }
        }
        if (nameInputs.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.no_names));
        } else {
            grabBag = nameInputs;
            progressDialog.setProgress(0);
            progressDialog.show();
            new GrabBag().execute();
        }
    }

    private class GrabBag extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ContactUtils.changeContacts(grabBag, LOG_TAG);
            return null;
        }
    }

    @OnClick(R.id.our_suggestions)
    public void ourSuggestions() {
        startActivity(new Intent(this, SuggestionsActivity.class));
    }

    @Subscribe
    public void onEvent(SnackbarEvent event) {
        if (event.getScreen().equals(LOG_TAG)) {
            progressDialog.dismiss();
            UIUtils.showSnackbar(parent, event.getMessage());
        }
    }
}
