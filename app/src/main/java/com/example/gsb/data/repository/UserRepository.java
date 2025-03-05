package com.example.gsb.data.repository;

import com.example.gsb.MyApplication;
import com.example.gsb.data.model.User;
import com.example.gsb.network.ApiService;
import com.example.gsb.utils.SharedPrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRepository {
    private final ApiService apiService;

    public UserRepository() {
        this.apiService = new ApiService();
    }

    public void getUserbyId(String token, final UserCallback callback) {
        apiService.getUserById(token, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    // Parse l'objet User depuis la réponse JSON
                    long userId = response.getLong("id");
                    String firstName = response.getString("firstName");
                    String lastName = response.getString("lastName");
                    String email = response.getString("mail");
                    int role = response.getInt("role");

                    User user = new User(userId, firstName, lastName, email, null, role);
                    callback.onResult(user);
                } catch (JSONException e) {
                    callback.onFailure("Erreur lors du parsing JSON: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure(errorMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void editUser(String firstName, String lastName, String email, String password, String token, final UserCallback callback) {
        apiService.updateUser(firstName, lastName, email, password, token, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    // Extraction des données mises à jour depuis la réponse
                    long userId = response.getLong("id");
                    String updatedFirstName = response.getString("firstName");
                    String updatedLastName = response.getString("lastName");
                    String updatedEmail = response.getString("mail");
                    int role = response.getInt("role");

                    if (response.has("token")) {
                        String newToken = response.getString("token");

                        // ✅ Stocke le nouveau token
                        SharedPrefsHelper.saveToken(newToken, MyApplication.getAppContext());
                    }

                    // Mise a jour d'un objet User avec les données entrées
                    User updatedUser = new User(userId, updatedFirstName, updatedLastName, updatedEmail, null, role);

                    // Appelle le callback avec l'utilisateur mis à jour
                    callback.onResult(updatedUser);
                } catch (JSONException e) {
                    callback.onFailure("Erreur lors du parsing JSON: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure("Erreur API: " + errorMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure("Échec de la mise à jour: " + errorMessage);
            }
        });
    }

    public interface UserCallback {
        void onResult(User user);
        void onFailure(String errorMessage); // ✅ Ajout de cette méthode
    }
}
