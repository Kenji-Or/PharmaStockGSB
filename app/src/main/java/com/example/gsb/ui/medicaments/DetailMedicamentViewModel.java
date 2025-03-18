package com.example.gsb.ui.medicaments;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Categorie;
import com.example.gsb.data.model.Medicament;
import com.example.gsb.data.repository.CategorieRepository;
import com.example.gsb.data.repository.MedicamentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailMedicamentViewModel extends ViewModel {
    private final MedicamentRepository medicamentRepository;
    private final CategorieRepository categorieRepository;
    private MutableLiveData<Map<Integer, String>> categoriesMap = new MutableLiveData<>();
    private final MutableLiveData<Medicament> medicament = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public DetailMedicamentViewModel() {
        this.medicamentRepository = new MedicamentRepository();
        this.categorieRepository = new CategorieRepository();
    }

    public LiveData<Map<Integer, String>> getCategoriesMap() {
        return categoriesMap;
    }
    public LiveData<Medicament> getMedicament() {
        return medicament;
    }

    public LiveData<String> getErrorMessage() { // ✅ Exposition de l'erreur à l'UI
        return errorMessage;
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadMedicamentData(String token, Long medicamentId){
        isLoading.setValue(true);
        medicamentRepository.getMedicamentById(token, medicamentId, new MedicamentRepository.MedicamentCallback() {
            @Override
            public void onSuccess(List<Medicament> medicaments) {
            }

            @Override
            public void onResult(Medicament medicamentData) {
                if (medicamentData != null) {
                    isLoading.postValue(false);
                    medicament.postValue(medicamentData);
                }
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue(error);
            }
        });
    }

    public void loadAllCategorie(String token) {
        isLoading.setValue(true);
        categorieRepository.getAllCategories(token, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categoriesList) {
                Map<Integer, String> map = new HashMap<>();
                for (Categorie category : categoriesList) {
                    map.put(category.getIdCategorie(), category.getNameCategorie());
                }
                categoriesMap.postValue(map); // Met à jour le LiveData
            }

            @Override
            public void onResult(Categorie categorieData){
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onFailure(String error) {
                Log.e("DetailMedicamentViewModel", "Erreur de récupération des catégories : " + error);
            }
        });
    }
}
