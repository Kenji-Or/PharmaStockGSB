package com.example.gsb.ui.medicaments;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Categorie;
import com.example.gsb.data.model.Medicament;
import com.example.gsb.data.model.User;
import com.example.gsb.data.repository.CategorieRepository;
import com.example.gsb.data.repository.MedicamentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicamentListViewModel extends ViewModel {
    private final MedicamentRepository medicamentRepository;
    private final CategorieRepository categorieRepository;
    private MutableLiveData<Map<Integer, String>> categoriesMap = new MutableLiveData<>();
    private final MutableLiveData<List<Medicament>> medicamentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MedicamentListViewModel() {
        this.medicamentRepository = new MedicamentRepository();
        this.categorieRepository = new CategorieRepository();
    }

    public LiveData<List<Medicament>> getMedicamentsLiveData() {
        return medicamentsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Map<Integer, String>> getCategoriesMap() {
        return categoriesMap;
    }

    public void fetchMedicaments(String token) {
        isLoading.setValue(true);
        medicamentRepository.getAllMedicaments(token, new MedicamentRepository.MedicamentCallback() {
            @Override
            public void onSuccess(List<Medicament> medicaments) {
                isLoading.postValue(false);
                medicamentsLiveData.postValue(medicaments);
            }

            @Override
            public void onResult(Medicament medicament) {

            }

            @Override
            public void onDeleted() {

            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Erreur API : " + error);
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
            public void onFailure(String error) {
                Log.e("MedicamentListViewModel", "Erreur de récupération des catégories : " + error);
            }
        });
    }

    public void deleteMedicament(String token, Long id) {
        isLoading.setValue(true);

        medicamentRepository.deleteMedicamentById(token, id, new MedicamentRepository.MedicamentCallback() {
            @Override
            public void onSuccess(List<Medicament> medicaments) {
                isLoading.postValue(false);
                fetchMedicaments(token);
            }

            @Override
            public void onResult(Medicament medicament) {
            }

            @Override
            public void onDeleted() {
                isLoading.postValue(false);
                fetchMedicaments(token);
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Erreur API: " + error);
            }
        });
    }
}
