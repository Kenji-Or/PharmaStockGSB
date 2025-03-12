package com.example.gsb.ui.medicaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.gsb.R;
import com.example.gsb.databinding.FragmentCreateMedicamentBinding;
import com.example.gsb.utils.SharedPrefsHelper;

public class CreateMedicamentFragment extends Fragment {
    private FragmentCreateMedicamentBinding binding;
    private CreateMedicamentViewModel createMedicamentViewModel;
    private CategorieAdapter categorieAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateMedicamentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createMedicamentViewModel = new ViewModelProvider(this).get(CreateMedicamentViewModel.class);

        createMedicamentViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categorieAdapter = new CategorieAdapter(getContext(), categories);
                binding.spinnerCategorie.setAdapter(categorieAdapter);

                binding.spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // auncune catégories disponible
                    }
                });
            } else {
                // Afficher un message d'erreur ou laisser le Spinner vide
                Toast.makeText(getContext(), "Aucune catégorie disponible.", Toast.LENGTH_SHORT).show();
            }
        });

        String token = SharedPrefsHelper.getToken(requireContext());
        createMedicamentViewModel.loadCategories(token);

        createMedicamentViewModel.getMedicamentCreated().observe(getViewLifecycleOwner(), isCreated -> {
            if (isCreated) {
                Toast.makeText(getContext(), "Médicament créé avec succès !", Toast.LENGTH_SHORT).show();
                navigateToMedicamentListFragment();
            } else {
                Toast.makeText(getContext(), "Échec de la création du médicament.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonSaveMedicament.setOnClickListener(v -> {
            String name = binding.editTextNameMedicament.getText().toString().trim();
            int quantity;
            try {
                quantity = Integer.parseInt(binding.editTextQuantity.getText().toString().trim());
            } catch (NumberFormatException e) {
                // Gérer l'exception si la valeur saisie n'est pas un entier
                Log.e("Erreur", "Valeur saisie n'est pas un entier");
                quantity = 0; // une valeur par défaut
            }
            String dateExpiration = binding.editTextDateExpiration.getText().toString().trim();
            int alerteStock;
            try {
                alerteStock = Integer.parseInt(binding.editTextAlerteStock.getText().toString().trim());
            } catch (NumberFormatException e) {
                // Gérer l'exception si la valeur saisie n'est pas un entier
                Log.e("Erreur", "Valeur saisie n'est pas un entier");
                alerteStock = 0; // une valeur par défaut
            }

            if (categorieAdapter != null) {
                int selectedCategorieId = categorieAdapter.getItem(binding.spinnerCategorie.getSelectedItemPosition()).getIdCategorie();

                if (!name.isEmpty() && !dateExpiration.isEmpty()) {
                    // Appeler la méthode pour créer l'utilisateur avec l'ID du rôle
                    createMedicamentViewModel.createMedicament(token, name, quantity, selectedCategorieId, dateExpiration, alerteStock);
                } else {
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Erreur : catégorie non sélectionné.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.buttonCancelMedicament.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void navigateToMedicamentListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MedicamentListFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Évite les fuites de mémoire
    }
}


