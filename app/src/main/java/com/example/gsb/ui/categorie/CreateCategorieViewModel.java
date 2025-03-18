package com.example.gsb.ui.categorie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Categorie;
import com.example.gsb.data.repository.CategorieRepository;

import java.util.List;

public class CreateCategorieViewModel extends ViewModel {
    private final CategorieRepository categorieRepository;
    private final MutableLiveData<Boolean> categorieCreated = new MutableLiveData<>();

    public CreateCategorieViewModel() {
        this.categorieRepository = new CategorieRepository();
    }

    public void createCategorie(String token, String nameCategorie) {
        categorieRepository.createCategorie(token, nameCategorie, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categories) {
            }

            @Override
            public void onResult(Categorie categorie) {
                categorieCreated.postValue(true);
            }

            @Override
            public void onDeleted() {
            }

            @Override
            public void onFailure(String errorMessage) {
                categorieCreated.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getCategorieCreated() {
        return categorieCreated;
    }
}
