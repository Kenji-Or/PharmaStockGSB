package com.example.gsb.data.repository;

import com.example.gsb.data.model.Categorie;

import com.example.gsb.data.model.Role;
import com.example.gsb.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategorieRepository {
    final ApiService apiService;

    public CategorieRepository() {
        this.apiService = new ApiService();
    }

    public void getAllCategories(String token, CategoryCallback callback) {
        apiService.getAllCategory(token, new ApiService.ApiCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    List<Categorie> categories = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject categorieObject = response.getJSONObject(i);
                        int idCategorie = categorieObject.getInt("id_category");
                        String name = categorieObject.getString("name");
                        categories.add(new Categorie(idCategorie, name));  // Créer un objet Role et l'ajouter à la liste
                    }
                    callback.onSuccess(categories);
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
                callback.onFailure("Échec lors de la récupération des catégories: " + errorMessage);
            }
        });
    }

    public void getCategorieById(String token, int idCategorie, CategoryCallback callback) {
        apiService.getCategoryById(token, idCategorie, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int idCategorie = response.getInt("id_category");
                    String nameCategorie = response.getString("name");

                    Categorie categorie = new Categorie(idCategorie, nameCategorie);
                    callback.onResult(categorie);
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

    public void createCategorie(String token, String name, CategoryCallback callback) {
        apiService.createCategory(token, name, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int categorieId = response.getInt("id_category");
                    String nameCategorie = response.getString("name");

                    Categorie newCategorie = new Categorie(categorieId, nameCategorie);
                    callback.onResult(newCategorie);
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

    public void updateCategorie(String token, int categorieId, String nameCategorie, CategoryCallback callback) {
        apiService.updateCategorie(token, categorieId, nameCategorie, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int idCategorie = response.getInt("id");
                    String categorieName = response.getString("name");

                    Categorie updateCategorie = new Categorie(idCategorie, categorieName);
                    callback.onResult(updateCategorie);
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

    public void deleteCategorie(String token, int categorieId, CategoryCallback callback) {
        apiService.deleteCategorie(token, categorieId, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                callback.onDeleted();
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


    public interface CategoryCallback {
        void onSuccess(List<Categorie> categories);
        void onResult(Categorie categorie);
        void onDeleted();
        void onFailure(String errorMessage);
    }
}
