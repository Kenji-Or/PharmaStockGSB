package com.example.gsb.ui.medicaments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Categorie;
import com.example.gsb.data.model.Medicament;
import com.example.gsb.data.repository.CategorieRepository;
import com.example.gsb.data.repository.MedicamentRepository;

import java.util.ArrayList;
import java.util.List;


public class CreateMedicamentViewModel extends ViewModel {
    private final MedicamentRepository medicamentRepository;
    private final CategorieRepository categorieRepository;
    private final MutableLiveData<Boolean> medicamentCreated = new MutableLiveData<>();
    private final MutableLiveData<List<Categorie>> categories = new MutableLiveData<>();

    public CreateMedicamentViewModel() {
        this.medicamentRepository = new MedicamentRepository();
        this.categorieRepository = new CategorieRepository();
    }

    public void createMedicament(String token, String name, int quantity, int category, String dateExpiration, int alerteStock) {
        medicamentRepository.createMedicament(token, name, quantity, category, dateExpiration, alerteStock, new MedicamentRepository.MedicamentCallback() {
            @Override
            public void onSuccess(List<Medicament> medicaments) {
            }

            @Override
            public void onResult(Medicament medicament) {
                medicamentCreated.postValue(true);
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onFailure(String errorMessage) {
                medicamentCreated.postValue(false);
            }
        });
    }

    public void loadCategories(String token) {
        categorieRepository.getAllCategories(token, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categoriesList) {
                categories.postValue(categoriesList);
            }

            @Override
            public void onResult(Categorie categorie){
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onFailure(String errorMessage) {
                categories.postValue(new ArrayList<>());
            }
        });
    }

    public LiveData<Boolean> getMedicamentCreated() {
        return medicamentCreated;
    }

    public LiveData<List<Categorie>> getCategories() {
        return categories;
    }
}
