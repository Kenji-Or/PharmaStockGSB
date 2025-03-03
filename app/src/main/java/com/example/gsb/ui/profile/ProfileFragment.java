package com.example.gsb.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gsb.databinding.FragmentProfileBinding;
import com.example.gsb.utils.SharedPrefsHelper;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        String token = SharedPrefsHelper.getToken(requireContext());

        if (token != null && !token.isEmpty()) {  // ✅ Vérifie que le token n'est pas vide
            profileViewModel.loadUserData(token);
        } else {
            Toast.makeText(getContext(), "Erreur: Token invalide", Toast.LENGTH_SHORT).show();
        }

        // ✅ Mise à jour des infos utilisateur
        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.profileName.setText(user.getFirstName() + " " + user.getLastName());
                binding.profileEmail.setText(user.getEmail());
                if (user.getRole() == 1) {
                    binding.profileRole.setText("Admin");
                } else {
                    binding.profileRole.setText("Utilisateur");
                }
            }
        });

        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // ✅ Gestion des erreurs
        profileViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), "Erreur: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
