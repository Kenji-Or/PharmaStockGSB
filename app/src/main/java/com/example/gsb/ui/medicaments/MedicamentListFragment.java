package com.example.gsb.ui.medicaments;

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
import com.example.gsb.data.model.Medicament;
import com.example.gsb.databinding.FragmentMedicamentListBinding;
import com.example.gsb.ui.home.HomeFragment;
import com.example.gsb.utils.SharedPrefsHelper;

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

        if (token != null && !token.isEmpty()) {
            medicamentListViewModel.fetchMedicaments(token);
        } else {
            binding.errorTextView.setText("Aucun token trouvé. Veuillez vous connecter.");
            binding.errorTextView.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        medicamentListViewModel = new ViewModelProvider(this).get(MedicamentListViewModel.class);

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

    private void navigateToHomeFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
