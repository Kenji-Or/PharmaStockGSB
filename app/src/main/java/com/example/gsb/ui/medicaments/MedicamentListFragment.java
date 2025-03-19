package com.example.gsb.ui.medicaments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
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
import com.example.gsb.data.model.Medicament;
import com.example.gsb.databinding.FragmentMedicamentListBinding;
import com.example.gsb.ui.categorie.CategorieFragment;
import com.example.gsb.ui.home.HomeFragment;
import com.example.gsb.utils.JwtUtils;
import com.example.gsb.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class MedicamentListFragment extends Fragment implements MedicamentListAdapter.OnMedicamentActionListener {
    private MedicamentListViewModel medicamentListViewModel;
    private MedicamentListAdapter medicamentListAdapter;
    private FragmentMedicamentListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMedicamentListBinding.inflate(inflater, container, false);

        binding.recyclerViewMedicaments.setLayoutManager(new LinearLayoutManager(getContext()));
        medicamentListAdapter = new MedicamentListAdapter(this);
        binding.recyclerViewMedicaments.setAdapter(medicamentListAdapter);

        medicamentListViewModel = new ViewModelProvider(this).get(MedicamentListViewModel.class);

        String token = SharedPrefsHelper.getToken(requireContext());
        int idCategorie = 0;

        if (token != null && !token.isEmpty()) {
            medicamentListViewModel.fetchMedicaments(token, idCategorie);
            medicamentListViewModel.loadAllCategorie(token);
            medicamentListViewModel.loadCategoriesForSpinner(token);
        } else {
            binding.errorTextView.setText("Aucun token trouvé. Veuillez vous connecter.");
            binding.errorTextView.setVisibility(View.VISIBLE);
        }

        String role = JwtUtils.getRoleFromToken(token);
        if (role != null) {
            if (role.equals("1")) { // Si rôle admin, on affiche le bouton
                binding.buttonNavigateCategorie.setVisibility(View.VISIBLE);
            } else { // Si rôle non-admin, on masque le bouton
                binding.buttonNavigateCategorie.setVisibility(View.GONE);
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicamentListViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
                    if (categories != null) {
                        // Création de la liste des catégories avec "Toutes les catégories" au début
                        List<Categorie> categoriesWithAll = new ArrayList<>();
                        categoriesWithAll.add(new Categorie(0, "Toutes les catégories"));
                        categoriesWithAll.addAll(categories);

                        // Utilisation de l'adaptateur personnalisé
                        FilterCategorySpinnerAdapter adapter = new FilterCategorySpinnerAdapter(requireContext(), categoriesWithAll);
                        binding.spinnerCategories.setAdapter(adapter);
                        // Gestion de la sélection d'une catégorie
                        binding.spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Categorie selectedCategory = categoriesWithAll.get(position);
                                int categoryId = selectedCategory.getIdCategorie();
                                String token = SharedPrefsHelper.getToken(requireContext());

                                if (token != null && !token.isEmpty()) {
                                    medicamentListViewModel.fetchMedicaments(token, categoryId);
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
        });


        // Observer la mise à jour des catégories
        medicamentListViewModel.getCategoriesMap().observe(getViewLifecycleOwner(), categoriesMap -> {
            medicamentListAdapter.setCategoriesMap(categoriesMap);
            medicamentListAdapter.notifyDataSetChanged();
        });

        medicamentListViewModel.getMedicamentsLiveData().observe(getViewLifecycleOwner(), medicaments -> {
            if (medicaments != null) {
                medicamentListAdapter.setMedicaments(medicaments);
                binding.recyclerViewMedicaments.setVisibility(View.VISIBLE);
                binding.errorTextView.setVisibility(View.GONE);
            }
        });

        // Observe loading state
        medicamentListViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // Observe error messages
        medicamentListViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.errorTextView.setText(error);
                binding.errorTextView.setVisibility(View.VISIBLE);
                binding.recyclerViewMedicaments.setVisibility(View.GONE);
            }
        });

        binding.buttonBack.setOnClickListener(v -> navigateToHomeFragment());
        binding.fabAddMedicament.setOnClickListener(v -> navigateToCreateMedicamentFragment());
        binding.buttonNavigateCategorie.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenuInflater().inflate(R.menu.menu_options, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_settings) {
                    navigateToCategorieFragment();
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public void onDeleteClick(Medicament medicament) {
        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(getContext())
                .setMessage("Êtes-vous sûr de vouloir supprimer ce médicament ?")
                .setCancelable(false)
                .setPositiveButton("Oui", (dialog, id) -> {
                    String token = SharedPrefsHelper.getToken(requireContext());
                    if (token != null) {
                        medicamentListViewModel.deleteMedicament(token, medicament.getIdMedicament());
                        Toast.makeText(getContext(), "Médicament supprimé", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Token invalide. Connexion requise.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    public void onDetailsClick(Medicament medicament) {
        DetailMedicamentFragment detailMedicamentFragment = new DetailMedicamentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("medicament_id", medicament.getIdMedicament()); // Ajout de l'ID du médicament
        detailMedicamentFragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailMedicamentFragment);
        transaction.addToBackStack(null); // Pour permettre le retour en arrière
        transaction.commit();
    }

    private void navigateToHomeFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigateToCreateMedicamentFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CreateMedicamentFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigateToCategorieFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CategorieFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
