package com.randomappsinc.contactshacker.Activities;

import android.os.Bundle;
import android.widget.ListView;

import com.randomappsinc.contactshacker.Adapters.SuggestionsAdapter;
import com.randomappsinc.contactshacker.R;

import butterknife.Bind;
import butterknife.ButterKnife;

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
}
