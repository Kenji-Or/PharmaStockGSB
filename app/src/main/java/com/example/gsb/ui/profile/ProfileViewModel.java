package com.example.gsb.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.User;
import com.example.gsb.data.repository.UserRepository;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(); // ✅ Gestion des erreurs

    public ProfileViewModel() {
        userRepository = new UserRepository();
    }

    public void loadUserData(String token) {
        userRepository.validateToken(token, new UserRepository.UserCallback() {
            @Override
            public void onResult(User userData) {
                if (userData != null) {
                    user.postValue(userData);
                }
            }

            @Override
            public void onFailure(String error) { // ✅ Ajout de la gestion des erreurs
                errorMessage.postValue(error);
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getErrorMessage() { // ✅ Exposition de l'erreur à l'UI
        return errorMessage;
    }
}
