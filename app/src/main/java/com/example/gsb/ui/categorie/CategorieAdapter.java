package com.example.gsb.ui.categorie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gsb.R;
import com.example.gsb.data.model.Categorie;

import java.util.ArrayList;
import java.util.List;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder> {
    private List<Categorie> categories = new ArrayList<>();
    private CategorieAdapter.OnCategorieActionListener listener;

    public interface OnCategorieActionListener {
        void onEditClick(Categorie categorie);
        void onDeleteClick(Categorie categorie);
    }

    public CategorieAdapter(CategorieAdapter.OnCategorieActionListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategorieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorie, parent, false);
        return new CategorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorieViewHolder holder, int position) {
        Categorie categorie = categories.get(position);
        holder.nomCategorie.setText(categorie.getNameCategorie());
        holder.editIconCategorie.setOnClickListener(v -> listener.onEditClick(categorie));
        holder.deleteIconCategorie.setOnClickListener(v -> listener.onDeleteClick(categorie));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategorieViewHolder extends RecyclerView.ViewHolder {

        TextView nomCategorie;
        ImageView deleteIconCategorie, editIconCategorie;

        public CategorieViewHolder(@NonNull View itemView) {
            super(itemView);
            nomCategorie = itemView.findViewById(R.id.textViewCategoryName);
            editIconCategorie = itemView.findViewById(R.id.editIconCategorie);
            deleteIconCategorie = itemView.findViewById(R.id.deleteIconCategorie);
        }
    }
}
