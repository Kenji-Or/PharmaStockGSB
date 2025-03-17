package com.example.gsb.ui.medicaments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Categorie;
import com.example.gsb.data.model.Medicament;
import com.example.gsb.data.model.Role;
import com.example.gsb.data.model.User;
import com.example.gsb.data.repository.CategorieRepository;
import com.example.gsb.data.repository.MedicamentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditMedicamentViewModel extends ViewModel {
    private final MedicamentRepository medicamentRepository;
    private final CategorieRepository categorieRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<List<Categorie>> categories = new MutableLiveData<>();
    private final MutableLiveData<Medicament> medicament = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();

    public EditMedicamentViewModel() {
        this.categorieRepository = new CategorieRepository();
        this.medicamentRepository =new MedicamentRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Medicament> getMedicament() {
        return medicament;
    }
    public LiveData<List<Categorie>> getCategories() {
        return categories;  // Retourner les categories en LiveData
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
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

    public void updateMedicament(String token,Long medicamentId, String name, Integer quantity, Integer categorie, String dateExpiration, Integer alerteStock) {
        medicamentRepository.updateMedicament(token, medicamentId, name, quantity, categorie, dateExpiration, alerteStock, new MedicamentRepository.MedicamentCallback() {
            @Override
            public void onSuccess(List<Medicament> medicaments) {
            }

            @Override
            public void onResult(Medicament updateMedicament) {
                medicament.postValue(updateMedicament);
                updateSuccess.postValue(true);
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

    public void loadAllCategories(String token) {
        categorieRepository.getAllCategories(token, new CategorieRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Categorie> categoriesList) {
                categories.postValue(categoriesList);
            }

            @Override
            public void onResult(Categorie categorie){
            }

            @Override
            public void onFailure(String errorMessage) {
                categories.postValue(new ArrayList<>());
            }
        });
    }
}
