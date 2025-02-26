package com.example.gsb.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.repository.AuthRepository;
import com.example.gsb.data.repository.LogoutResult;

public class HomeViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<LogoutResult> logoutResult = new MutableLiveData<>();

    public HomeViewModel() {
        this.authRepository = new AuthRepository();
    }

    public LiveData<LogoutResult> getLogoutResult() {
        return logoutResult;
    }

    public void logout() {
        authRepository.logout(result -> logoutResult.postValue(result));
    }
}
