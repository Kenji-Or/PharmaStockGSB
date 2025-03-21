package com.example.gsb.ui.home;

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
import com.example.gsb.databinding.FragmentHomeBinding;
import com.example.gsb.ui.alert.AlertFragment;
import com.example.gsb.ui.login.LoginFragment;
import com.example.gsb.ui.medicaments.DetailMedicamentFragment;
import com.example.gsb.ui.medicaments.MedicamentListFragment;
import com.example.gsb.ui.profile.ProfileFragment;
import com.example.gsb.ui.users.UserListFragment;
import com.example.gsb.utils.JwtUtils;
import com.example.gsb.utils.SharedPrefsHelper;

public class HomeFragment extends Fragment implements MedicamentExpiredDateListAdapter.OnMedicamentExpiredDateActionListener {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private MedicamentExpiredDateListAdapter medicamentExpiredDateListAdapter;
    private AlertDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        String token = SharedPrefsHelper.getToken(requireContext());
        String role = JwtUtils.getRoleFromToken(token);
        if (role != null) {
            if (role.equals("1")) { // Si rôle admin, on affiche le bouton
                binding.buttonGestionUsers.setVisibility(View.VISIBLE);
            } else { // Si rôle non-admin, on masque le bouton
                binding.buttonGestionUsers.setVisibility(View.GONE);
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.recyclerViewDateExpirationHome.setLayoutManager(new LinearLayoutManager(getContext()));
        medicamentExpiredDateListAdapter = new MedicamentExpiredDateListAdapter(this);
        binding.recyclerViewDateExpirationHome.setAdapter(medicamentExpiredDateListAdapter);
        String token = SharedPrefsHelper.getToken(requireContext());
        if (token != null && !token.isEmpty()) {
            homeViewModel.fetchMedicamentExpiredDate(token);
        }
        setupObservers();

        homeViewModel.getMedicamentsDateExpirationLiveData().observe(getViewLifecycleOwner(), medicaments -> {
            if (medicaments != null) {
                medicamentExpiredDateListAdapter.setMedicaments(medicaments);
                binding.recyclerViewDateExpirationHome.setVisibility(View.VISIBLE);
            }
        });

        homeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBarHome.setVisibility(View.VISIBLE);
            } else {
                binding.progressBarHome.setVisibility(View.GONE);
            }
        });

        binding.buttonLogout.setOnClickListener(v -> {
            showLoading();
            homeViewModel.logout();
        });

        binding.buttonViewProfile.setOnClickListener(v -> {
            navigateToProfileFragment();
        });

        binding.buttonGestionUsers.setOnClickListener(v -> {
            navigateToGestionUsersFragment();
        });

        binding.buttonManageMedications.setOnClickListener(v -> goToMedicamentListFragment());

        binding.alertButton.setOnClickListener(v -> goToAlertFragment());
    }

    private void goToAlertFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new AlertFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }

    private void navigateToProfileFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }

    private void navigateToGestionUsersFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new UserListFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }

    private void setupObservers() {
        homeViewModel.getLogoutResult().observe(getViewLifecycleOwner(), result -> {
            hideLoading();
            if (result.isSuccess()) {
                Toast.makeText(getContext(), "Déconnexion réussie", Toast.LENGTH_SHORT).show();
                goToLoginFragment();
            } else {
                Toast.makeText(getActivity(), "Erreur : " + result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToLoginFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
        fragmentTransaction.commit();
    }

    private void goToMedicamentListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MedicamentListFragment());
        fragmentTransaction.commit();
    }

    private void showLoading() {
        if (getActivity() == null) return;
        progressDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setView(R.layout.progress_dialog)
                .create();
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Libérer la mémoire
    }
}
