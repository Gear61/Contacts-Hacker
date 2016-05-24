package com.randomappsinc.contactshacker.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.contactshacker.Adapters.SuggestionsAdapter;
import com.randomappsinc.contactshacker.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class SuggestionsActivity extends StandardActivity {
    @Bind(R.id.suggestions) ListView suggestions;

    private SuggestionsAdapter suggestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.our_suggestions);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        suggestionsAdapter = new SuggestionsAdapter(this);
        suggestions.setAdapter(suggestionsAdapter);
    }

    @OnItemClick(R.id.suggestions)
    public void suggestionChosen(int position) {
        String listName = suggestionsAdapter.getItem(position);
        String confirmationText = getString(R.string.are_you_sure) + listName + "\"?";
        new MaterialDialog.Builder(this)
                .content(confirmationText)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }
}
