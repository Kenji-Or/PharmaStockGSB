package com.example.gsb.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gsb.data.model.Medicament;
import com.example.gsb.data.repository.AuthRepository;
import com.example.gsb.data.repository.LogoutResult;
import com.example.gsb.data.repository.MedicamentRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MedicamentRepository medicamentRepository;
    private final MutableLiveData<LogoutResult> logoutResult = new MutableLiveData<>();
    private final MutableLiveData<List<Medicament>> medicamentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public HomeViewModel() {
        this.authRepository = new AuthRepository();
        this.medicamentRepository = new MedicamentRepository();
    }

    public LiveData<List<Medicament>> getMedicamentsDateExpirationLiveData() {
        return medicamentsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<LogoutResult> getLogoutResult() {
        return logoutResult;
    }

    public void logout() {
        authRepository.logout(result -> logoutResult.postValue(result));
    }


    public void fetchMedicamentExpiredDate(String token) {
        isLoading.setValue(true);
        medicamentRepository.getMedicamentByDateExpiration(token, new MedicamentRepository.MedicamentCallback() {
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

}
