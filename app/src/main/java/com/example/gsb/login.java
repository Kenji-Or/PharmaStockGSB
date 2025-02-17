package com.example.gsb;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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
                    // Si la connexion est réussie, redirigez vers HomeActivity
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    // Optionnel : Finir l'activité actuelle pour qu'elle ne soit plus dans la pile
                    getActivity().finish();
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
}