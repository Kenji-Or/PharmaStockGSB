package com.example.gsb.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.gsb.R;
import com.example.gsb.data.model.User;
import com.example.gsb.ui.users.EditUserViewModel;
import com.example.gsb.databinding.FragmentEditUserBinding;
import com.example.gsb.utils.SharedPrefsHelper;

public class EditUserFragment extends Fragment {
    private FragmentEditUserBinding binding;
    private EditUserViewModel editUserViewModel;
    private RoleAdapter roleAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editUserViewModel = new ViewModelProvider(this).get(EditUserViewModel.class);

        String token = SharedPrefsHelper.getToken(requireContext());
        editUserViewModel.loadRoles(token);

        if (token != null && !token.isEmpty() && getArguments() != null) {  // ✅ Vérifie que le token n'est pas vide
            Long userId = getArguments().getLong("user_id", -1);
            editUserViewModel.loadUserData(token, userId);
        } else {
            Toast.makeText(getContext(), "Erreur: Token invalide", Toast.LENGTH_SHORT).show();
        }

        // Récupérer les infos actuelles de l'utilisateur
        // Observer le LiveData pour récupérer les rôles disponibles
        editUserViewModel.getRoles().observe(getViewLifecycleOwner(), roles -> {
            if (roles != null && !roles.isEmpty()) {
                // Créer l'adaptateur et l'attacher au Spinner
                roleAdapter = new RoleAdapter(getContext(), roles);
                binding.spinnerRole.setAdapter(roleAdapter);

                // Une fois les rôles chargés, observer l'utilisateur pour récupérer son rôle actuel
                editUserViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        binding.editTextFirstName.setText(user.getFirstName());
                        binding.editTextLastName.setText(user.getLastName());
                        binding.editTextEmail.setText(user.getEmail());

                        // Récupérer le rôle actuel de l'utilisateur
                        int userRole = user.getRole();

                        // Trouver l'index correspondant à l'ID de l'utilisateur
                        int position = -1;
                        for (int i = 0; i < roles.size(); i++) {
                            if (roles.get(i).getId_role() == userRole) { // Comparer les ID
                                position = i;
                                break;
                            }
                        }

                        // Sélectionner le rôle correspondant si trouvé
                        if (position >= 0) {
                            binding.spinnerRole.setSelection(position);
                        }
                    }
                });

                // Gérer la sélection d'un rôle par l'utilisateur
                binding.spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Aucun rôle sélectionné
                    }
                });
            } else {
                Toast.makeText(getContext(), "Aucun rôle disponible.", Toast.LENGTH_SHORT).show();
            }
        });


        // Bouton retour
        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.buttonCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        binding.buttonSaveUser.setOnClickListener(v -> {
            // Récupérer les anciennes valeurs depuis le ViewModel
            User currentUser = editUserViewModel.getUser().getValue();
            if (currentUser == null) return; // Sécurité si user non chargé

            // Récupérer les nouvelles valeurs saisies
            String newFirstName = binding.editTextFirstName.getText().toString().trim();
            String newLastName = binding.editTextLastName.getText().toString().trim();
            String newEmail = binding.editTextEmail.getText().toString().trim();
            String newPassword = binding.editTextPassword.getText().toString().trim();
            String newPasswordConfirm = binding.editTextConfirmPassword.getText().toString().trim();
            Integer newSelectedRoleId = roleAdapter.getItem(binding.spinnerRole.getSelectedItemPosition()).getId_role();

            // Vérifier que les valeurs sont modifiées
            boolean isFirstNameChanged = !newFirstName.isEmpty() && !newFirstName.equals(currentUser.getFirstName());
            boolean isLastNameChanged = !newLastName.isEmpty() && !newLastName.equals(currentUser.getLastName());
            boolean isEmailChanged = !newEmail.isEmpty() && !newEmail.equals(currentUser.getEmail());
            boolean isPasswordChanged = !newPassword.isEmpty() && newPassword.equals(newPasswordConfirm);
            boolean isRoleIdChanged = newSelectedRoleId != null && newSelectedRoleId != currentUser.getRole();

            if (!isFirstNameChanged) newFirstName = null;
            if (!isLastNameChanged) newLastName = null;
            if (!isEmailChanged) newEmail = null;
            if (!isPasswordChanged) newPassword = null;
            if (!isRoleIdChanged) newSelectedRoleId = null;


            editUserViewModel.updateUser(currentUser.getId(),newFirstName, newLastName, newEmail, newSelectedRoleId, newPassword, token);
            editUserViewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(getContext(), "Mise à jour réussie", Toast.LENGTH_SHORT).show();
                    navigateToUserListFragment();
                } else {
                    Toast.makeText(getContext(), "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateToUserListFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new UserListFragment());
        fragmentTransaction.addToBackStack(null); // Ajoute la transaction à la pile de retour
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Libérer la mémoire
    }

}
