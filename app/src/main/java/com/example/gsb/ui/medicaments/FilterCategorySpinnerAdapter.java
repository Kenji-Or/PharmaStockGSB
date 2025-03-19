package com.example.gsb.ui.medicaments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gsb.data.model.Categorie;

import java.util.List;

public class FilterCategorySpinnerAdapter extends ArrayAdapter<Categorie> {
    private final Context context;
    private final List<Categorie> categories;
    public FilterCategorySpinnerAdapter(Context context, List<Categorie> categories) {
        super(context, android.R.layout.simple_spinner_item, categories);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(categories.get(position).getNameCategorie());
        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(categories.get(position).getNameCategorie());
        return textView;
    }
}
