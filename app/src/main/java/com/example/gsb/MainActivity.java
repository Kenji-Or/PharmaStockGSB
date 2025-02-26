package com.example.gsb;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gsb.ui.home.HomeFragment;
import com.example.gsb.ui.login.LoginFragment;
import com.example.gsb.utils.SharedPrefsHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vérifiez si l'utilisateur est déjà connecté
        if (SharedPrefsHelper.getToken(this) != null) {
            // Si l'utilisateur est connecté, affichez le fragment Home
            loadFragment(new HomeFragment());
        } else {
            // Sinon, affichez le fragment Login
            loadFragment(new LoginFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
