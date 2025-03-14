package com.example.gsb.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Role;
import com.example.gsb.data.model.User;
import com.example.gsb.data.repository.RoleRepository;
import com.example.gsb.data.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private MutableLiveData<Map<Integer, String>> rolesMap = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(); // ✅ Gestion des erreurs

    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    public LiveData<Map<Integer, String>> getRolesMap() {
        return rolesMap;
    }


    public ProfileViewModel() {
        this.userRepository = new UserRepository();
        this.roleRepository = new RoleRepository();
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

    public void loadAllRoles(String token) {
        roleRepository.getAllRoles(token, new RoleRepository.RoleCallback() {
            @Override
            public void onSuccess(List<Role> roleList) {
                Map<Integer, String> map = new HashMap<>();
                for (Role role : roleList) {
                    map.put(role.getId_role(), role.getDescription());
                }
                rolesMap.postValue(map); // Met à jour le LiveData
            }

            @Override
            public void onFailure(String error) {
                Log.e("MedicamentListViewModel", "Erreur de récupération des catégories : " + error);
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
                errorMessage.postValue(error);
                updateSuccess.postValue(false);
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getErrorMessage() { // ✅ Exposition de l'erreur à l'UI
        return errorMessage;
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

}
