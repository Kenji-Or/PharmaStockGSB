package com.example.gsb.data.repository;

import android.util.Log;

import com.example.gsb.data.model.Medicament;
import com.example.gsb.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MedicamentRepository {
    final ApiService apiService;

    public MedicamentRepository() {
        this.apiService = new ApiService();
    }

    public void getAllMedicaments(String token,final MedicamentCallback callback) {
        apiService.getAllMedicaments(token, new ApiService.ApiCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                List<Medicament> medicamentList = new ArrayList<>();
                try {
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject medicamentJson = response.getJSONObject(i);
                        Medicament medicament = new Medicament(
                                medicamentJson.getLong("id"),
                                medicamentJson.getString("name"),
                                medicamentJson.getInt("quantity"),
                                medicamentJson.getInt("category"),
                                medicamentJson.getString("date_expiration"),
                                medicamentJson.getInt("alerte_stock")
                        );
                        medicamentList.add(medicament);
                    }
                    callback.onSuccess(medicamentList);
                } catch (JSONException e) {
                    Log.e("MedicamentListViewModel", "Erreur de parsing JSON", e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure(errorMessage);
                Log.e("MedicamentListViewModel", "Erreur API: " + errorMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void getMedicamentById(String token, Long medicamentId, final MedicamentCallback callback) {
        apiService.getMedicamentById(token, medicamentId, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    long idMedicament = response.getLong("id");
                    String nameMedicament = response.getString("name");
                    int quantiteMedicament = response.getInt("quantity");
                    int categorieMedicament = response.getInt("category");
                    String date_expiration = response.getString("date_expiration");
                    int alerte_stock = response.getInt("alerte_stock");

                    Medicament medicament = new Medicament(idMedicament, nameMedicament, quantiteMedicament,categorieMedicament,date_expiration, alerte_stock);
                    callback.onResult(medicament);
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

    public void createMedicament(String token, String name, int quantity, int category, String dateExpiration, int alerteStock, final MedicamentCallback callback) {
        apiService.createMedicament(token, name, quantity, category, dateExpiration, alerteStock, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    // Vérifier si l'utilisateur a bien été créé et récupérer les infos
                    long medicamentId = response.getLong("id");
                    String createdName = response.getString("name");
                    int createdQuantity = response.getInt("quantity");
                    int createdCategory = response.getInt("category");
                    String createdDateExpiration = response.getString("date_expiration");
                    int createdAlerteStock = response.getInt("alerte_stock");

                    Medicament medicament = new Medicament(medicamentId, createdName, createdQuantity, createdCategory, createdDateExpiration, createdAlerteStock);
                    callback.onResult(medicament);
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
                callback.onFailure("Échec de la création du médicament: " + errorMessage);
            }
        });
    }

    public void updateMedicament(String token, Long medicamentId, String name, Integer quantity, Integer categorie, String dateExpiration, Integer alerteStock, final MedicamentCallback callback) {
        apiService.updateMedicament(token,medicamentId, name, quantity, categorie, dateExpiration, alerteStock, new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    // Vérifier si l'utilisateur a bien été créé et récupérer les infos
                    long medicamentId = response.getLong("id");
                    String updateName = response.getString("name");
                    int updateQuantity = response.getInt("quantity");
                    int updateCategory = response.getInt("category");
                    String updateDateExpiration = response.getString("date_expiration");
                    int updateAlerteStock = response.getInt("alerte_stock");

                    Medicament updateMedicament = new Medicament(medicamentId, updateName, updateQuantity, updateCategory, updateDateExpiration, updateAlerteStock);
                    callback.onResult(updateMedicament);
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

    public void deleteMedicamentById(String token, Long medicamentId, final MedicamentCallback callback) {
        apiService.deleteMedicament(token, medicamentId, new ApiService.ApiCallback<JSONObject>() {
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

    public interface MedicamentCallback {
        void onSuccess(List<Medicament> medicaments);
        void onResult(Medicament medicament);
        void onDeleted();
        void onFailure(String errorMessage);
    }
}
