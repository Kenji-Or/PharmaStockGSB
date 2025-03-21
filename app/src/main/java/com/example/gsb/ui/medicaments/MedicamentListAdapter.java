package com.example.gsb.ui.medicaments;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicamentListAdapter extends RecyclerView.Adapter<MedicamentListAdapter.MedicamentViewHolder> {
    private List<Medicament> medicaments = new ArrayList<>();
    private Map<Integer, String> categoriesMap = new HashMap<>();

    private OnMedicamentActionListener listener;

    public interface OnMedicamentActionListener {
        void onDetailsClick(Medicament medicament);
        void onDeleteClick(Medicament medicament);
    }

    public MedicamentListAdapter(OnMedicamentActionListener listener) {
        this.listener = listener;
    }

    public void setCategoriesMap(Map<Integer, String> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicament, parent, false);
        return new MedicamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentListAdapter.MedicamentViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);
        holder.nomMedicament.setText(medicament.getName());
        holder.quantiteMedicament.setText(String.valueOf(medicament.getQuantite()));
        String categoryName = categoriesMap.get(medicament.getCategory());
        holder.Category.setText(categoryName != null ? categoryName : "Catégorie inconnue");
        holder.dateExpiration.setText(medicament.getDateExpiration());

        holder.deleteIconMedicament.setOnClickListener(v -> listener.onDeleteClick(medicament));
        // Clic sur l'élément complet (CardView)
        holder.itemView.setOnClickListener(v -> listener.onDetailsClick(medicament));
    }

    @Override
    public int getItemCount() {
        return medicaments.size();
    }



    static class MedicamentViewHolder extends RecyclerView.ViewHolder {

        TextView nomMedicament, quantiteMedicament, Category, dateExpiration;
        ImageView deleteIconMedicament;

        public MedicamentViewHolder(@NonNull View itemView) {
            super(itemView);
            nomMedicament = itemView.findViewById(R.id.nomMedicament);
            quantiteMedicament = itemView.findViewById(R.id.quantiteMedicament);
            Category = itemView.findViewById(R.id.Category);
            dateExpiration = itemView.findViewById(R.id.dateExpiration);
            deleteIconMedicament = itemView.findViewById(R.id.deleteIconMedicament);
        }
    }
}
