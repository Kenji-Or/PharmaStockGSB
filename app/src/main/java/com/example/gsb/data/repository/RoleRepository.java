package com.example.gsb.data.repository;

import com.example.gsb.data.model.Role;
import com.example.gsb.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoleRepository {
    final ApiService apiService;

    public RoleRepository() {
        this.apiService = new ApiService();
    }

    public void getAllRoles(String token, final RoleCallback callback) {
        apiService.getAllRoles(token, new ApiService.ApiCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    List<Role> roles = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject roleObject = response.getJSONObject(i);
                        int idRole = roleObject.getInt("id_role");
                        String description = roleObject.getString("description");
                        roles.add(new Role(idRole, description));  // Créer un objet Role et l'ajouter à la liste
                    }
                    callback.onSuccess(roles);  // Retourner la liste des rôles au callback
                } catch (JSONException e) {
                    callback.onFailure("Erreur lors du parsing JSON: " + e.getMessage());  // Gestion des erreurs
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure("Erreur API: " + errorMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure("Échec lors de la récupération des rôles: " + errorMessage);
            }
        });
    }

    public interface RoleCallback {
        void onSuccess(List<Role> roles);
        void onFailure(String errorMessage);
    }
}
