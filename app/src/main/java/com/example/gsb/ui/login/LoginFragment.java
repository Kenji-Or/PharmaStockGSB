package com.example.gsb.ui.login;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.gsb.R;
import com.example.gsb.databinding.FragmentLoginBinding;
import com.example.gsb.ui.home.HomeFragment;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            if (loginViewModel.validateInput(email, password)) {
                loginViewModel.login(email, password);
            } else {
                Toast.makeText(getActivity(), "Veuillez entrer un email et un mot de passe valides.", Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null && result.isSuccess()) {
                Toast.makeText(getActivity(), "Login réussi", Toast.LENGTH_SHORT).show();
                goToHomeFragment();
            } else {
                String errorMsg = (result != null) ? result.getError() : "Erreur inconnue";
                Toast.makeText(getActivity(), "Erreur: " + errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        binding.textViewMotDePasseOublie.setOnClickListener(v -> Toast.makeText(getActivity(), "Veuillez contacter un administrateur afin qu'il réinitialise votre mot de passe.", Toast.LENGTH_SHORT).show());
    }

    private void goToHomeFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Libérer la mémoire
    }
}
