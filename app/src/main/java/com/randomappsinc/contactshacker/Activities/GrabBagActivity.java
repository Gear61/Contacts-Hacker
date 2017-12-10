package com.randomappsinc.contactshacker.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.ContactUtils;
import com.randomappsinc.contactshacker.Utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class GrabBagActivity extends ChangingActivity {

    public static final String LOG_TAG = "GrabBagActivity";

    @BindView(R.id.parent) View parent;
    @BindViews({R.id.name_1, R.id.name_2, R.id.name_3, R.id.name_4, R.id.name_5}) List<EditText> inputs;

    private static List<String> grabBag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logTag = LOG_TAG;
        setUpLayout(R.layout.grab_bag);
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

    private static class GrabBag extends AsyncTask<Void, Void, Void> {
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
}
