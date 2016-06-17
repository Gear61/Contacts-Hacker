package com.randomappsinc.contactshacker.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.contactshacker.Adapters.SuggestionsAdapter;
import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.ContactUtils;
import com.randomappsinc.contactshacker.Utils.GroupServer;

import java.util.List;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class SuggestionsActivity extends ChangingActivity {
    public static final String LOG_TAG = "SuggestionsActivity";

    @Bind(R.id.suggestions) ListView suggestions;

    private SuggestionsAdapter suggestionsAdapter;
    private String selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logTag = LOG_TAG;

        setUpLayout(R.layout.our_suggestions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        suggestionsAdapter = new SuggestionsAdapter(this);
        suggestions.setAdapter(suggestionsAdapter);
    }

    @OnItemClick(R.id.suggestions)
    public void suggestionChosen(int position) {
        selectedList = suggestionsAdapter.getItem(position);
        String confirmationText = getString(R.string.are_you_sure) + selectedList + "\"?";
        new MaterialDialog.Builder(this)
                .content(confirmationText)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        progressDialog.setProgress(0);
                        progressDialog.show();
                        new GrabBag().execute();
                    }
                })
                .show();
    }

    private class GrabBag extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            List<String> grabBag = GroupServer.getInstance().getNamesInList(selectedList);
            ContactUtils.changeContacts(grabBag, LOG_TAG);
            return null;
        }
    }
}
