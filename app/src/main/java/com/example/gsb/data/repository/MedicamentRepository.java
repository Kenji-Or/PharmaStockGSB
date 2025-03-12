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
                                medicamentJson.getLong("category"),
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
                    long categorieMedicament = response.getLong("category");
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
