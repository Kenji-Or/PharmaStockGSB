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

public class EditUserViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<List<Role>> roles = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(); // ✅ Gestion des erreurs

    public EditUserViewModel() {
        this.roleRepository = new RoleRepository();
        this.userRepository = new UserRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadUserData(String token, Long userId) {
        userRepository.getUserbyId(token, userId, new UserRepository.UserCallback() {

            @Override
            public void onSuccess(List<User> userList) {
            }

            @Override
            public void onDeleted() {
            }
            @Override
            public void onResult(User userData) {
                if (userData != null) {
                    user.postValue(userData);
                }
            }

            @Override
            public void onFailure(String error) { //  Ajout de la gestion des erreurs
                errorMessage.postValue(error);
            }
        });
    }

    public void updateUser(Long userId, String firstName, String lastName, String email, Integer roleId, String password, String token) {
        userRepository.editUser(userId,firstName, lastName, email, roleId, password, token, new UserRepository.UserCallback() {

            @Override
            public void onSuccess(List<User> userList) {
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onResult(User updatedUser) {
                user.postValue(updatedUser);
                updateSuccess.postValue(true);
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue(error);
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

    public LiveData<User> getUser() {
        return user;
    }
    public LiveData<List<Role>> getRoles() {
        return roles;  // Retourner les rôles en LiveData
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }


}
