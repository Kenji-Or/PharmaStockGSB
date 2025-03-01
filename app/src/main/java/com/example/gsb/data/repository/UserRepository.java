package com.example.gsb.data.repository;

import com.example.gsb.data.model.User;
import com.example.gsb.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRepository {
    private final ApiService apiService;

    public UserRepository() {
        this.apiService = new ApiService();
    }

    public void validateToken(String token, final UserCallback callback) {
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
                    callback.onResult(user); // ✅ Correction ici
                } catch (JSONException e) {
                    callback.onFailure("Erreur lors du parsing JSON: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure(errorMessage); // ✅ Correction ici
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public interface UserCallback {
        void onResult(User user);
        void onFailure(String errorMessage); // ✅ Ajout de cette méthode
    }
}
