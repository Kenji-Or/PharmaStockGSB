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
import com.example.gsb.databinding.FragmentCreateCategorieBinding;
import com.example.gsb.utils.SharedPrefsHelper;

public class CreateCategorieFragment extends Fragment {
    private FragmentCreateCategorieBinding binding;
    private CreateCategorieViewModel createCategorieViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateCategorieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createCategorieViewModel = new ViewModelProvider(this).get(CreateCategorieViewModel.class);

        createCategorieViewModel.getCategorieCreated().observe(getViewLifecycleOwner(), isCreated -> {
            if (isCreated) {
                Toast.makeText(getContext(), "Catégorie créé avec succès !", Toast.LENGTH_SHORT).show();
                navigateToCategorieListFragment();
            } else {
                Toast.makeText(getContext(), "Échec de la création de la catégorie.", Toast.LENGTH_SHORT).show();
            }
        });
        String token = SharedPrefsHelper.getToken(requireContext());
        binding.buttonSaveCategorie.setOnClickListener(v -> {
            String nameCategorie = binding.editTextNameCategorie.getText().toString().trim();
            if (!nameCategorie.isEmpty()) {
                createCategorieViewModel.createCategorie(token, nameCategorie);
            } else {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonBackCategorie.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.buttonCancelCategorie.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void navigateToCategorieListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CategorieFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
