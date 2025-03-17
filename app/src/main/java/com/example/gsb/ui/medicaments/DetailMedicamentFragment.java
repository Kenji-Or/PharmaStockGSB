package com.example.gsb.ui.medicaments;

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
import com.example.gsb.databinding.FragmentDetailMedicamentBinding;
import com.example.gsb.ui.users.EditUserFragment;
import com.example.gsb.utils.SharedPrefsHelper;

public class DetailMedicamentFragment extends Fragment {
    private FragmentDetailMedicamentBinding binding;
    private DetailMedicamentViewModel detailMedicamentViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailMedicamentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailMedicamentViewModel = new ViewModelProvider(this).get(DetailMedicamentViewModel.class);

        String token = SharedPrefsHelper.getToken(requireContext());
        Long medicamentId = getArguments().getLong("medicament_id", -1);
        if (token != null && !token.isEmpty() && getArguments() != null) {
            detailMedicamentViewModel.loadAllCategorie(token);
            detailMedicamentViewModel.loadMedicamentData(token, medicamentId);
        }

        detailMedicamentViewModel.getCategoriesMap().observe(getViewLifecycleOwner(), categoriesMap -> {
           detailMedicamentViewModel.getMedicament().observe(getViewLifecycleOwner(), medicament -> {
               if (medicament != null) {
                   binding.textViewProfileDetailMedicament.setText(medicament.getName());
                   binding.quantiteMedicament.setText(String.valueOf(medicament.getQuantite()));
                   String categorieName = categoriesMap.get(medicament.getCategory());
                   binding.categorieMedicament.setText(categorieName != null ? categorieName : "Catégorie inconnue");
                   binding.dateExpirationMedicament.setText(medicament.getDateExpiration());
                   binding.alerteStockMedicament.setText(String.valueOf(medicament.getAlerte_stock()));
               }
           });
        });

        binding.buttonBack.setOnClickListener(v -> navigateToMedicamentListFragment());
        binding.btnModifierDetailMedicament.setOnClickListener(v -> {
            EditMedicamentFragment editMedicamentFragment = new EditMedicamentFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("medicament_id", medicamentId); // Ajout de l'ID de l'utilisateur
            editMedicamentFragment.setArguments(bundle);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, editMedicamentFragment);
            transaction.addToBackStack(null); // Pour permettre le retour en arrière
            transaction.commit();
        });

        // ✅ Gestion des erreurs
        detailMedicamentViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), "Erreur: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
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
