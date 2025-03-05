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
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
import com.example.gsb.R;
import com.example.gsb.data.model.User;
import com.example.gsb.databinding.FragmentEditProfileBinding;
import com.example.gsb.utils.SharedPrefsHelper;

public class EditProfileFragment extends Fragment {
    private FragmentEditProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
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

//        NavController navController = Navigation.findNavController(view);

        // Récupérer les infos actuelles de l'utilisateur
        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.editFirstName.setText(user.getFirstName());
                binding.editLastName.setText(user.getLastName());
                binding.editEmail.setText(user.getEmail());
            }
        });

        // Bouton retour
        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.buttonCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        binding.buttonSave.setOnClickListener(v -> {
            // Récupérer les anciennes valeurs depuis le ViewModel
            User currentUser = profileViewModel.getUser().getValue();
            if (currentUser == null) return; // Sécurité si user non chargé

            // Récupérer les nouvelles valeurs saisies
            String newFirstName = binding.editFirstName.getText().toString().trim();
            String newLastName = binding.editLastName.getText().toString().trim();
            String newEmail = binding.editEmail.getText().toString().trim();
            String newPassword = binding.editPassword.getText().toString().trim();
            String newPasswordConfirm = binding.confirmPassword.getText().toString().trim();

            // Vérifier que les valeurs sont modifiées
            boolean isFirstNameChanged = !newFirstName.isEmpty() && !newFirstName.equals(currentUser.getFirstName());
            boolean isLastNameChanged = !newLastName.isEmpty() && !newLastName.equals(currentUser.getLastName());
            boolean isEmailChanged = !newEmail.isEmpty() && !newEmail.equals(currentUser.getEmail());
            boolean isPasswordChanged = !newPassword.isEmpty() && newPassword.equals(newPasswordConfirm);

            if (!isFirstNameChanged) newFirstName = null;
            if (!isLastNameChanged) newLastName = null;
            if (!isEmailChanged) newEmail = null;
            if (!isPasswordChanged) newPassword = null;


            profileViewModel.updateUser(newFirstName, newLastName, newEmail, newPassword, token);
            profileViewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(getContext(), "Mise à jour réussie", Toast.LENGTH_SHORT).show();
                    navigateToProfileFragment();
                } else {
                    Toast.makeText(getContext(), "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateToProfileFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }
}
