package com.example.gsb.ui.medicaments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Medicament;
import com.example.gsb.data.repository.MedicamentRepository;

import java.util.List;

public class MedicamentListViewModel extends ViewModel {
    private final MedicamentRepository medicamentRepository;
    private final MutableLiveData<List<Medicament>> medicamentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MedicamentListViewModel() {
        this.medicamentRepository = new MedicamentRepository();
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
