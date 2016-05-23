package com.randomappsinc.contactshacker.Activities;

import android.os.Bundle;
import android.widget.EditText;

import com.randomappsinc.contactshacker.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 5/22/16.
 */
public class GrabBagActivity extends StandardActivity {
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

    }

    @OnClick(R.id.our_suggestions)
    public void ourSuggestions() {

    }
}
