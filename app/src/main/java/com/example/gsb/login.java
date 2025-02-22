package com.example.gsb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.style.ClickableSpan;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gsb.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends Fragment {
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        TextView motDePasseOublie = view.findViewById(R.id.textViewMotDePasseOublie);
        String text = getString(R.string.mot_de_passe_oublie);

        SpannableString spannableString = getSpannableString(text);
        motDePasseOublie.setText(spannableString);
        motDePasseOublie.setMovementMethod(LinkMovementMethod.getInstance());
        motDePasseOublie.setHighlightColor(Color.TRANSPARENT); // Pour éviter le fond de couleur lors du clic


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (validateInput(email, password)) {
                    // Logique de connexion ici
                    loginRequest(email, password);
                    // Si la connexion est réussie, redirigez vers HomeActivity
//                    Intent intent = new Intent(getActivity(), HomeActivity.class);
//                    startActivity(intent);
//                    // Optionnel : Finir l'activité actuelle pour qu'elle ne soit plus dans la pile
//                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Veuillez entrer un email et un mot de passe valides.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @NonNull
    private SpannableString getSpannableString(String text) {
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Action à effectuer lorsque le lien est cliqué
                // Par exemple, ouvrir une URL
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"));
                startActivity(browserIntent);
            }
        };

        spannableString.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private boolean validateInput(String email, String password) {
        // Ajoutez ici votre logique de validation
        return !email.isEmpty() && !password.isEmpty();
    }

    private void loginRequest(String userVar, String passVar) {
        String loginUrl = "http://10.0.2.2:5000/user/login"; // your server's URL
        // Afficher une boîte de dialogue de chargement
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false); // Empêcher l'utilisateur de fermer la boîte de dialogue
        builder.setView(R.layout.progress_dialog); // Fichier XML personnalisé avec un ProgressBar
        AlertDialog progressDialog = builder.create();
        progressDialog.show();

        // Créer l'objet JSON avec les données utilisateur
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mail", userVar); // Utiliser le nom d'utilisateur
            jsonBody.put("password", passVar); // Utiliser le mot de passe
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Erreur de création de la requête", Toast.LENGTH_LONG).show();
            return; // Retourner si erreur dans la création de l'objet JSON
        }

        // Utiliser JsonObjectRequest pour envoyer la requête POST avec JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, jsonBody,
                response -> {
                    progressDialog.dismiss();
                    Log.d("API_RESPONSE", "Response: " + response); // Log de la réponse brute du serveur
                    try {
                        // Si la réponse contient un "token"
                        if (response.has("token")) {
                            String token = response.getString("token");
                            // Logique de réussite, utiliser le token pour l'authentification future
                            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                            // Sauvegarder le token pour un usage futur
                            saveToken(token);
                            // Rediriger vers HomeActivity ou un autre fragment
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            // Si la réponse ne contient pas de token, c'est une erreur
                            Toast.makeText(getActivity(), "Erreur: " + response.toString(), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to parse server response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    String errorMessage = "Failed to connect to server. ";
                    if (error.networkResponse != null) {
                        errorMessage += "Server Response Code: " + error.networkResponse.statusCode;
                        Log.e("API_ERROR", "Error Response Code: " + error.networkResponse.statusCode);
                        Log.e("API_ERROR", "Error Data: " + new String(error.networkResponse.data));
                    } else {
                        Log.e("API_ERROR", "Network error: " + error.getMessage());
                    }
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                }) {

            // Ajouter les en-têtes pour indiquer que la requête est en JSON
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Spécifie le contenu JSON pour la requête
                headers.put("Accept", "application/json"); // Spécifie que la réponse attendue est en JSON
                return headers;
            }
        };

        // Ajouter la requête à la file d'attente
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();  // Sauvegarde du token
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null);  // Retourne le token ou null si non trouvé
    }

//    private void switchToHomeFragment() {
//        HomeFragment homeFragment = new HomeFragment(); // Instancier ton nouveau fragment
//
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, homeFragment); // Remplace l'ancien fragment
//        transaction.addToBackStack(null); // Permet de revenir en arrière avec le bouton retour
//        transaction.commit();
//    }

}