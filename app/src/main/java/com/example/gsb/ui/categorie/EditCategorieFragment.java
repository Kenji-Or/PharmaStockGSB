package com.example.gsb.ui.categorie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.gsb.R;
import com.example.gsb.data.model.Categorie;
import com.example.gsb.databinding.FragmentEditCategorieBinding;
import com.example.gsb.utils.SharedPrefsHelper;

public class EditCategorieFragment extends Fragment {
    private FragmentEditCategorieBinding binding;
    private EditCategorieViewModel editCategorieViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditCategorieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editCategorieViewModel = new ViewModelProvider(this).get(EditCategorieViewModel.class);
        String token = SharedPrefsHelper.getToken(requireContext());
        if (token != null && !token.isEmpty() && getArguments() != null) {  // Vérifie que le token n'est pas vide
            int categorieId = getArguments().getInt("categorie_id", -1);
            editCategorieViewModel.loadCategorieById(token, categorieId);
        } else {
            Toast.makeText(getContext(), "Erreur: Token invalide", Toast.LENGTH_SHORT).show();
        }

        editCategorieViewModel.getCategorie().observe(getViewLifecycleOwner(), categorie -> {
            if (categorie != null) {
                binding.editTextNameEditCategorie.setText(categorie.getNameCategorie());
            } else {
                Toast.makeText(getContext(), "Échec de la récupération de la catégorie", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.buttonCancelEditCategorie.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        binding.buttonSaveEditCategorie.setOnClickListener(v -> {
            Categorie currentCategorie = editCategorieViewModel.getCategorie().getValue();
            if (currentCategorie == null) return;

            String newName = binding.editTextNameEditCategorie.getText().toString().trim();

            boolean isNameChanged = !newName.isEmpty() && !newName.equals(currentCategorie.getNameCategorie());

            if (!isNameChanged) newName = null;

            editCategorieViewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(getContext(), "Mise à jour réussie", Toast.LENGTH_SHORT).show();
                    navigateToCategorieListFragment();
                } else {
                    Toast.makeText(getContext(), "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            });
            editCategorieViewModel.updateCategorie(token, currentCategorie.getIdCategorie(), newName);

        });
    }

    private void navigateToCategorieListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CategorieFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }
}
