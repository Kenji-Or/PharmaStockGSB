package com.example.gsb.data.repository;

import com.example.gsb.network.ApiService;
import com.example.gsb.utils.SharedPrefsHelper;
import com.example.gsb.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthRepository {

    private final ApiService apiService;

    public AuthRepository() {
        this.apiService = new ApiService();
    }

    public void login(String email, String password, LoginCallback callback) {
        apiService.login(email, password, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("token")) {
                        String token = response.getString("token");
                        SharedPrefsHelper.saveToken(token, MyApplication.getAppContext());
                        callback.onResult(new LoginResult(true, null));
                    } else {
                        callback.onResult(new LoginResult(false, "Réponse invalide"));
                    }
                } catch (JSONException e) {
                    callback.onResult(new LoginResult(false, "Erreur de parsing JSON"));
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onResult(new LoginResult(false, errorMessage));
            }
        });
    }

    public void logout(LogoutCallback callback) {
        String token = SharedPrefsHelper.getToken(MyApplication.getAppContext());
        apiService.logout(token, new ApiService.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                SharedPrefsHelper.clearToken();
                callback.onResult(new LogoutResult(true, null));
            }

            @Override
            public void onError(String errorMessage) {
                callback.onResult(new LogoutResult(false, errorMessage));
            }
        });
    }

    public interface LoginCallback {
        void onResult(LoginResult result);
    }

    public interface LogoutCallback {
        void onResult(LogoutResult result);
    }
}
