package com.example.gsb.ui.medicaments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.gsb.data.model.Categorie;

import java.util.List;

public class CategorieAdapter extends ArrayAdapter<Categorie> {
    public CategorieAdapter(Context context, List<Categorie> categories) {
        super(context, android.R.layout.simple_spinner_item, categories);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Categorie categorie = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(categorie.getNameCategorie());
         return convertView;
    }

    // Cette méthode gère l'affichage des éléments dans la liste déroulante
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        Categorie categorie = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(categorie.getNameCategorie());  // Affiche la description du rôle dans la liste déroulante

        return convertView;
    }
}
