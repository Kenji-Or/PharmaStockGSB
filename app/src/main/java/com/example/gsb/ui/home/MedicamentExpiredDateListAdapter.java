package com.example.gsb.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gsb.R;
import com.example.gsb.data.model.Medicament;

import java.util.ArrayList;
import java.util.List;

public class MedicamentExpiredDateListAdapter extends RecyclerView.Adapter<MedicamentExpiredDateListAdapter.MedicamentExpiredDateViewHolder> {
    private List<Medicament> medicaments = new ArrayList<>();
    private OnMedicamentExpiredDateActionListener listener;

    public interface OnMedicamentExpiredDateActionListener {
        void onDetailsClick(Medicament medicament);

    }

    public MedicamentExpiredDateListAdapter(OnMedicamentExpiredDateActionListener listener) {
        this.listener = listener;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicamentExpiredDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_expiration, parent, false);
        return new MedicamentExpiredDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentExpiredDateViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);
        holder.nomMedicament.setText(medicament.getName());
        holder.dateExpiration.setText(medicament.getDateExpiration());

        // Clic sur l'élément complet (CardView)
        holder.itemView.setOnClickListener(v -> listener.onDetailsClick(medicament));
    }

    @Override
    public int getItemCount() {
        return medicaments.size();
    }

    static class MedicamentExpiredDateViewHolder extends RecyclerView.ViewHolder {

        TextView nomMedicament, dateExpiration;

        public MedicamentExpiredDateViewHolder(@NonNull View itemView) {
            super(itemView);
            nomMedicament = itemView.findViewById(R.id.nomMedicamentItemDateExpiration);
            dateExpiration = itemView.findViewById(R.id.dateExpirationItemDateExpiration);
        }
    }
}
