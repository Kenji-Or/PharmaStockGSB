package com.example.gsb.ui.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gsb.data.model.User;
import com.example.gsb.data.repository.UserRepository;
import java.util.List;

public class UserListViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserListViewModel() {
        this.userRepository = new UserRepository();
    }

    public LiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchUsers(String token) {
        isLoading.setValue(true);

        userRepository.getAllUsers(token, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(List<User> userList) {
                isLoading.postValue(false);
                usersLiveData.postValue(userList);
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onResult(User user) {
                // On ne fait rien ici car on attend une liste, pas un seul utilisateur
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Erreur API : " + error);
            }
        });
    }

    public void deleteUser(String token, long userId) {
        isLoading.setValue(true);

        userRepository.deleteUser(token, userId, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(List<User> users) {
                isLoading.postValue(false);
                fetchUsers(token); // Recharger les utilisateurs après suppression
            }

            @Override
            public void onDeleted() {
                isLoading.postValue(false);
                fetchUsers(token); // Rafraîchir la liste des utilisateurs après suppression
            }

            @Override
            public void onResult(User user) {
                // This won't be called for delete, so we can leave it empty
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Erreur API: " + error);
            }
        });
    }
}
