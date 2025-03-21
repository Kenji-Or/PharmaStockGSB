package com.example.gsb.ui.alert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gsb.R;
import com.example.gsb.data.model.Medicament;
import com.example.gsb.databinding.FragmentAlertBinding;
import com.example.gsb.ui.home.HomeFragment;
import com.example.gsb.ui.medicaments.DetailMedicamentFragment;
import com.example.gsb.utils.SharedPrefsHelper;

public class AlertFragment extends Fragment implements AlertAdapter.OnAlertActionListener {
    private FragmentAlertBinding binding;
    private AlertViewModel alertViewModel;
    private AlertAdapter alertAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAlertBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewAlerte.setLayoutManager(new LinearLayoutManager(getContext()));
        alertAdapter = new AlertAdapter(this);
        binding.recyclerViewAlerte.setAdapter(alertAdapter);

        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);
        String token = SharedPrefsHelper.getToken(requireContext());

        if (token != null && !token.isEmpty()) {
            alertViewModel.fetchMedicamentRuptureStock(token);
        } else {
            binding.errorTextView.setText("Aucun token trouvé. Veuillez vous connecter.");
            binding.errorTextView.setVisibility(View.VISIBLE);
        }

        alertViewModel.getMedicamentsLiveData().observe(getViewLifecycleOwner(), medicaments -> {
            if (medicaments != null) {
                alertAdapter.setMedicaments(medicaments);
                binding.recyclerViewAlerte.setVisibility(View.VISIBLE);
                binding.recyclerViewAlerte.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                binding.errorTextView.setVisibility(View.GONE);
            }
        });

        // Observe loading state
        alertViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // Observe error messages
        alertViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.errorTextView.setText(error);
                binding.errorTextView.setVisibility(View.VISIBLE);
                binding.recyclerViewAlerte.setVisibility(View.GONE);
            }
        });

        binding.buttonBack.setOnClickListener(v -> navigateToHomeFragment());
    }

    private void navigateToHomeFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDetailClick(Medicament medicament) {
        DetailMedicamentFragment detailMedicamentFragment = new DetailMedicamentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("medicament_id", medicament.getIdMedicament()); // Ajout de l'ID du médicament
        detailMedicamentFragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailMedicamentFragment);
        transaction.addToBackStack(null); // Pour permettre le retour en arrière
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Libérer la mémoire
    }
}
