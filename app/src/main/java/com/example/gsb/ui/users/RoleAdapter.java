package com.example.gsb.ui.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.gsb.data.model.Role;

import java.util.List;

public class RoleAdapter extends ArrayAdapter<Role> {
    public RoleAdapter(Context context, List<Role> roles) {
        super(context, android.R.layout.simple_spinner_item, roles);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Utilisation de la vue déroulante par défaut
    }

    // Cette méthode gère l'affichage de l'élément sélectionné dans le Spinner
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Role role = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(role.getDescription());  // Affiche la description du rôle dans la vue sélectionnée

        return convertView;
    }

    // Cette méthode gère l'affichage des éléments dans la liste déroulante
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        Role role = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(role.getDescription());  // Affiche la description du rôle dans la liste déroulante

        return convertView;
    }
}
