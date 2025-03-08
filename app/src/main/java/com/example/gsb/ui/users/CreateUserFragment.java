package com.example.gsb.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.gsb.R;
import com.example.gsb.data.model.Role;
import com.example.gsb.data.model.User;
import com.example.gsb.databinding.FragmentCreateUserBinding;
import com.example.gsb.ui.profile.ProfileFragment;
import com.example.gsb.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class CreateUserFragment extends Fragment {

    private FragmentCreateUserBinding binding;
    private CreateUserViewModel createUserViewModel;
    private RoleAdapter roleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createUserViewModel = new ViewModelProvider(this).get(CreateUserViewModel.class);

        // Observer le LiveData pour les rôles
        createUserViewModel.getRoles().observe(getViewLifecycleOwner(), roles -> {
            if (roles != null && !roles.isEmpty()) {
                // Créer un adaptateur personnalisé pour le Spinner
                roleAdapter = new RoleAdapter(getContext(), roles); // Utilisation de la variable membre
                binding.spinnerRole.setAdapter(roleAdapter);

                // Écouter les sélections du Spinner
                binding.spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                        // Ce bloc est appelé à chaque fois qu'un item est sélectionné
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Aucun rôle sélectionné
                    }
                });
            } else {
                // Afficher un message d'erreur ou laisser le Spinner vide
                Toast.makeText(getContext(), "Aucun rôle disponible.", Toast.LENGTH_SHORT).show();
            }
        });

        String token = SharedPrefsHelper.getToken(requireContext());
        createUserViewModel.loadRoles(token);

        // Observer le LiveData pour détecter si la création a réussi ou échoué
        createUserViewModel.getUserCreated().observe(getViewLifecycleOwner(), isCreated -> {
            if (isCreated) {
                Toast.makeText(getContext(), "Utilisateur créé avec succès !", Toast.LENGTH_SHORT).show();
                navigateToUserListFragment();
            } else {
                Toast.makeText(getContext(), "Échec de la création de l'utilisateur.", Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton pour créer l'utilisateur
        binding.buttonSaveUser.setOnClickListener(v -> {
            String firstName = binding.editTextFirstName.getText().toString().trim();
            String lastName = binding.editTextLastName.getText().toString().trim();
            String email = binding.editTextEmail.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();
            String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

            // Récupérer l'ID du rôle sélectionné
            if (roleAdapter != null) {
                int selectedRoleId = roleAdapter.getItem(binding.spinnerRole.getSelectedItemPosition()).getId_role();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                    if (!password.equals(confirmPassword)) {
                        Toast.makeText(getContext(), "Les mots de passes ne sont pas identiques.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Appeler la méthode pour créer l'utilisateur avec l'ID du rôle
                    createUserViewModel.createUser(token, firstName, lastName, email, selectedRoleId, password);
                } else {
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Erreur : rôle non sélectionné.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.buttonCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Évite les fuites de mémoire
    }

    private void navigateToUserListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new UserListFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
