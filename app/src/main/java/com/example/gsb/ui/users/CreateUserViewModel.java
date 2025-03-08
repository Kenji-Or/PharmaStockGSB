package com.example.gsb.ui.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Role;
import com.example.gsb.data.model.User;
import com.example.gsb.data.repository.RoleRepository;
import com.example.gsb.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class CreateUserViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MutableLiveData<Boolean> userCreated = new MutableLiveData<>();
    private final MutableLiveData<List<Role>> roles = new MutableLiveData<>();

    public CreateUserViewModel() {
        this.roleRepository = new RoleRepository();
        this.userRepository = new UserRepository();
    }

    public void createUser(String token, String firstName, String lastName, String email, int idRole, String password) {
        userRepository.createUser(token, firstName, lastName, email, idRole, password, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(List<User> userList) {
            }

            @Override
            public void onResult(User user) {
                userCreated.postValue(true);
            }

            @Override
            public void onFailure(String error) {
                userCreated.postValue(false);
            }
        });
    }

    public void loadRoles(String token) {
        roleRepository.getAllRoles(token, new RoleRepository.RoleCallback() {
            @Override
            public void onSuccess(List<Role> roleList) {
                roles.postValue(roleList);  // Mettre à jour LiveData avec la liste des rôles
            }

            @Override
            public void onFailure(String errorMessage) {
                // Gérer l'erreur (afficher un message d'erreur si nécessaire)
                roles.postValue(new ArrayList<>());
            }
        });
    }

    public LiveData<Boolean> getUserCreated() {
        return userCreated;
    }

    public LiveData<List<Role>> getRoles() {
        return roles;  // Retourner les rôles en LiveData
    }
}
