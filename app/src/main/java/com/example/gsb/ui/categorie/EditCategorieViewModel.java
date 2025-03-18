package com.example.gsb.ui.categorie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Categorie;
import com.example.gsb.data.repository.CategorieRepository;

import java.util.List;

public class EditCategorieViewModel extends ViewModel {
    private final CategorieRepository categorieRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Categorie> categorie = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();

    public EditCategorieViewModel() {
        this.categorieRepository = new CategorieRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Categorie> getCategorie() {
        return categorie;
    }
    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public void loadCategorieById(String token, int categorieId) {
        isLoading.setValue(true);
        categorieRepository.getCategorieById(token, categorieId, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categories) {

            }

            @Override
            public void onResult(Categorie categorieData) {
                if (categorieData != null) {
                    isLoading.postValue(false);
                    categorie.postValue(categorieData);
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

    public void updateCategorie(String token, int categorieId, String categorieName) {
        isLoading.setValue(true);
        categorieRepository.updateCategorie(token, categorieId, categorieName, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categories) {
            }

            @Override
            public void onResult(Categorie updateCategorie) {
                isLoading.postValue(false);
                categorie.postValue(updateCategorie);
                updateSuccess.postValue(true);
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                errorMessage.postValue(error);
                updateSuccess.postValue(false);
            }
        });
    }
}
