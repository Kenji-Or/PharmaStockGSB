package com.example.gsb.ui.medicaments;

import android.os.Bundle;
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
import com.example.gsb.data.model.Medicament;
import com.example.gsb.databinding.FragmentEditMedicamentBinding;
import com.example.gsb.utils.SharedPrefsHelper;

public class EditMedicamentFragment extends Fragment {
    private FragmentEditMedicamentBinding binding;
    private EditMedicamentViewModel editMedicamentViewModel;
    private CategorieAdapter categorieAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditMedicamentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editMedicamentViewModel = new ViewModelProvider(this).get(EditMedicamentViewModel.class);
        String token = SharedPrefsHelper.getToken(requireContext());
        editMedicamentViewModel.loadAllCategories(token);
        if (token != null && !token.isEmpty() && getArguments() != null) {  // Vérifie que le token n'est pas vide
            Long medicamentId = getArguments().getLong("medicament_id", -1);
            editMedicamentViewModel.loadMedicamentData(token, medicamentId);
        } else {
            Toast.makeText(getContext(), "Erreur: Token invalide", Toast.LENGTH_SHORT).show();
        }

        editMedicamentViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categorieAdapter = new CategorieAdapter(getContext(), categories);
                binding.categorieEditMedicament.setAdapter(categorieAdapter);

                editMedicamentViewModel.getMedicament().observe(getViewLifecycleOwner(), medicament -> {
                    if (medicament != null) {
                        binding.nameEditMedicament.setText(medicament.getName());
                        binding.quantiteEditMedicament.setText(String.valueOf(medicament.getQuantite()));
                        binding.dateExpirationEditMedicament.setText(medicament.getDateExpiration());
                        binding.alerteStockEditMedicament.setText(String.valueOf(medicament.getAlerte_stock()));
                        int categoryId = medicament.getCategory();
                        // Trouver l'index correspondant à l'ID de l'utilisateur
                        int position = -1;
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getIdCategorie() == categoryId) { // Comparer les ID
                                position = i;
                                break;
                            }
                        }

                        // Sélectionner le rôle correspondant si trouvé
                        if (position >= 0) {
                            binding.categorieEditMedicament.setSelection(position);
                        }
                    } else {
                        Toast.makeText(getContext(), "Échec de la récupération du médicament.", Toast.LENGTH_SHORT).show();
                    }
                });

                binding.categorieEditMedicament.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Bouton retour
        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.btnAnnulerEditMedicament.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        binding.btnEnregistrerEditMedicament.setOnClickListener(v -> {
            Medicament currentMedicament = editMedicamentViewModel.getMedicament().getValue();
            if (currentMedicament == null) return;

            String newName = binding.nameEditMedicament.getText().toString().trim();
            Integer newQuantity = Integer.parseInt(binding.quantiteEditMedicament.getText().toString().trim());
            Integer newCategorie = categorieAdapter.getItem(binding.categorieEditMedicament.getSelectedItemPosition()).getIdCategorie();
            String newDateExpiration = binding.dateExpirationEditMedicament.getText().toString().trim();
            Integer newAlerteStock = Integer.parseInt(binding.alerteStockEditMedicament.getText().toString().trim());

            // Vérifier que les valeurs sont modifiées
            boolean isNameChanged = !newName.isEmpty() && !newName.equals(currentMedicament.getName());
            boolean isQuantityChanged = newQuantity != null && newQuantity != currentMedicament.getQuantite();
            boolean isCategorieIdChanged = newCategorie != null && newCategorie != currentMedicament.getCategory();
            boolean isDateExpirationChanged = !newDateExpiration.isEmpty() && !newDateExpiration.equals(currentMedicament.getDateExpiration());
            boolean isAlerteStockChanged = newAlerteStock != null && newAlerteStock != currentMedicament.getAlerte_stock();

            if (!isNameChanged) newName = null;
            if (!isQuantityChanged) newQuantity = null;
            if (!isCategorieIdChanged) newCategorie = null;
            if (!isDateExpirationChanged) newDateExpiration = null;
            if (!isAlerteStockChanged) newAlerteStock = null;

            editMedicamentViewModel.updateMedicament(token, currentMedicament.getIdMedicament(), newName, newQuantity, newCategorie, newDateExpiration, newAlerteStock);
            editMedicamentViewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(getContext(), "Mise à jour réussie", Toast.LENGTH_SHORT).show();
                    navigateToMedicamentListFragment();
                } else {
                    Toast.makeText(getContext(), "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateToMedicamentListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MedicamentListFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }
}
