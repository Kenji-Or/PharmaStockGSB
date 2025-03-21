package com.example.gsb.ui.categorie;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gsb.R;
import com.example.gsb.data.model.Categorie;
import com.example.gsb.databinding.FragmentCategorieBinding;
import com.example.gsb.ui.home.HomeFragment;
import com.example.gsb.ui.medicaments.MedicamentListFragment;
import com.example.gsb.utils.SharedPrefsHelper;

public class CategorieFragment extends Fragment implements CategorieAdapter.OnCategorieActionListener {
    private CategorieViewModel categorieViewModel;
    private CategorieAdapter categorieAdapter;
    private FragmentCategorieBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategorieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categorieViewModel = new ViewModelProvider(this).get(CategorieViewModel.class);
        binding.recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(getContext()));
        categorieAdapter = new CategorieAdapter(this);
        binding.recyclerViewCategorie.setAdapter(categorieAdapter);

        String token = SharedPrefsHelper.getToken(requireContext());
        if (token != null && !token.isEmpty()) {
            categorieViewModel.fetchCategories(token);
        } else {
            binding.errorTextViewCategorie.setText("Aucun token trouvé. Veuillez vous connecter.");
            binding.errorTextViewCategorie.setVisibility(View.VISIBLE);
        }

        categorieViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categorieAdapter.setCategories(categories);
                binding.recyclerViewCategorie.setVisibility(View.VISIBLE);
                binding.errorTextViewCategorie.setVisibility(View.GONE);
            }
        });

        // Observe loading state
        categorieViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBarCategorie.setVisibility(View.VISIBLE);
            } else {
                binding.progressBarCategorie.setVisibility(View.GONE);
            }
        });

        // Observe error messages
        categorieViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.errorTextViewCategorie.setText(error);
                binding.errorTextViewCategorie.setVisibility(View.VISIBLE);
                binding.recyclerViewCategorie.setVisibility(View.GONE);
            }
        });

        binding.buttonBack.setOnClickListener(v -> navigateToMedicamentListFragment());
        binding.fabAddCategorie.setOnClickListener(v -> navigateToCreateCategorieFragment());
    }

    @Override
    public void onEditClick(Categorie categorie) {
        EditCategorieFragment editCategorieFragment = new EditCategorieFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("categorie_id", categorie.getIdCategorie()); // Ajout de l'ID du médicament
        editCategorieFragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editCategorieFragment);
        transaction.addToBackStack(null); // Pour permettre le retour en arrière
        transaction.commit();
    }

    @Override
    public void onDeleteClick(Categorie categorie) {
        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(getContext())
                .setMessage("Êtes-vous sûr de vouloir supprimer cette catégorie ?")
                .setCancelable(false)
                .setPositiveButton("Oui", (dialog, id) -> {
                    String token = SharedPrefsHelper.getToken(requireContext());
                    if (token != null) {
                        categorieViewModel.deleteCategorie(token, categorie.getIdCategorie());
                        Toast.makeText(getContext(), "Catégorie supprimé", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Token invalide. Connexion requise.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void navigateToMedicamentListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MedicamentListFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigateToCreateCategorieFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CreateCategorieFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Libérer la mémoire
    }
}
