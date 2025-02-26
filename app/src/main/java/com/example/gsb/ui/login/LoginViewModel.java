package com.example.gsb.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.repository.AuthRepository;
import com.example.gsb.data.repository.LoginResult;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final AuthRepository authRepository;

    public LoginViewModel() {
        this.authRepository = new AuthRepository();
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public boolean validateInput(String email, String password) {
        return email != null && !email.trim().isEmpty() && password != null && !password.trim().isEmpty();
    }

    public void login(String email, String password) {
        authRepository.login(email, password, loginResult::postValue);
    }
}
