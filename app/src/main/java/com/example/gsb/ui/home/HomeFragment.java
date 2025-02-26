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

import com.example.gsb.R;
import com.example.gsb.databinding.FragmentHomeBinding;
import com.example.gsb.ui.login.LoginFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private AlertDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setupObservers();

        binding.buttonLogout.setOnClickListener(v -> {
            showLoading();
            homeViewModel.logout();
        });
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
}
