package com.example.gsb.ui.alert;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gsb.R;
import com.example.gsb.data.model.Medicament;

import java.util.ArrayList;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {
    private List<Medicament> medicaments = new ArrayList<>();
    private OnAlertActionListener listener;

    // Interface pour gérer les clics sur les icônes
    public interface OnAlertActionListener {
        void onDetailClick(Medicament medicament);
    }

    public AlertAdapter(OnAlertActionListener listener) {
        this.listener = listener;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);
        holder.textViewName.setText("Attention Stock faible pour " + medicament.getName());
        holder.textViewDetailMedicament.setText("Il n'en reste seulement " + medicament.getQuantite());

        // Set up click listeners for the icons
        holder.detailIcon.setOnClickListener(v -> listener.onDetailClick(medicament));
    }

    @Override
    public int getItemCount() {
        return medicaments.size();
    }


    static class AlertViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDetailMedicament;
        ImageView detailIcon;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.notification_title);
            textViewDetailMedicament = itemView.findViewById(R.id.notification_message);
            detailIcon = itemView.findViewById(R.id.btnGoToMedicament);
        }
    }
}
