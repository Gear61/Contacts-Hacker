package com.randomappsinc.contactshacker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.contactshacker.R;
import com.randomappsinc.contactshacker.Utils.GroupServer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestionsAdapter extends BaseAdapter {

    private Context context;
    private List<String> suggestions;

    public SuggestionsAdapter(Context context) {
        this.context = context;
        this.suggestions = GroupServer.getInstance().getListNames();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class SuggestionViewHolder {
        @BindView(R.id.suggestion) TextView suggestion;

        public SuggestionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadSuggestion(int position) {
            suggestion.setText(getItem(position));
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SuggestionViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.suggestions_list_item, parent, false);
            holder = new SuggestionViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SuggestionViewHolder) view.getTag();
        }
        holder.loadSuggestion(position);
        return view;
    }
}
