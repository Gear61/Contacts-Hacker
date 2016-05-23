package com.randomappsinc.contactshacker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 5/22/16.
 */
public class GrabBagActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind({R.id.name_1, R.id.name_2, R.id.name_3, R.id.name_4, R.id.name_5}) List<EditText> inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }
    }

    @OnClick(R.id.our_suggestions)
    public void ourSuggestions() {
        startActivity(new Intent(this, SuggestionsActivity.class));
    }
}
