package com.example.gsb.ui.categorie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Categorie;
import com.example.gsb.data.repository.CategorieRepository;

import java.util.List;

public class CategorieViewModel extends ViewModel {
    private final CategorieRepository categorieRepository;
    private final MutableLiveData<List<Categorie>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public CategorieViewModel() {
        this.categorieRepository = new CategorieRepository();
    }

    public LiveData<List<Categorie>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchCategories(String token) {
        isLoading.setValue(true);
        categorieRepository.getAllCategories(token, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categories) {
                isLoading.postValue(false);
                categoriesLiveData.postValue(categories);
            }

            @Override
            public void onResult(Categorie categorie) {
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

    public void deleteCategorie(String token, int categorieId) {
        isLoading.setValue(true);
        categorieRepository.deleteCategorie(token, categorieId, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categories) {
                isLoading.postValue(false);
                fetchCategories(token);
            }

            @Override
            public void onResult(Categorie categorie) {
            }

            @Override
            public void onDeleted() {
                isLoading.postValue(false);
                fetchCategories(token);
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Erreur API: " + error);
            }
        });
    }
}
