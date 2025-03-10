package com.example.gsb.ui.profile;

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
import com.example.gsb.databinding.FragmentProfileBinding;
import com.example.gsb.ui.home.HomeFragment;
import com.example.gsb.utils.JwtUtils;
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
        String stringUserId = JwtUtils.getUserIdFromToken(token);
        assert stringUserId != null;
        Long userId = Long.parseLong(stringUserId);

        if (token != null && !token.isEmpty()) {  // ✅ Vérifie que le token n'est pas vide
            profileViewModel.loadUserData(token, userId);
        } else {
            Toast.makeText(getContext(), "Erreur: Token invalide", Toast.LENGTH_SHORT).show();
        }

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

        binding.buttonBack.setOnClickListener(v -> navigateToHomeFragment());

        binding.buttonEditUser.setOnClickListener(v -> {
            navigateToEditProfileFragment(userId);
        });

        // ✅ Gestion des erreurs
        profileViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), "Erreur: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToEditProfileFragment(Long userId) {
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", userId); // Ajout de l'ID de l'utilisateur
        editProfileFragment.setArguments(bundle);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, editProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigateToHomeFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }
}
