package com.example.gsb.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.User;
import com.example.gsb.data.repository.UserRepository;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(); // ✅ Gestion des erreurs

    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();


    public ProfileViewModel() {
        userRepository = new UserRepository();
    }

    public void loadUserData(String token) {
        userRepository.getUserbyId(token, new UserRepository.UserCallback() {

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

    public void updateUser(String firstName, String lastName, String email, String password, String token) {
        userRepository.editUser(firstName, lastName, email, password, token, new UserRepository.UserCallback() {

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
