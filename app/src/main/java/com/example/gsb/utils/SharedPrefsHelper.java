package com.example.gsb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.gsb.MyApplication;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SharedPrefsHelper {
    private static final String PREFS_NAME = "GSB_PREFS";
    private static final String TOKEN_KEY = "USER_TOKEN";

    /**
     * Sauvegarde le jeton d'authentification dans les préférences partagées.
     *
     * @param token  Le jeton à sauvegarder.
     * @param context Le contexte de l'application.
     */
    public static void saveToken(String token, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(TOKEN_KEY, token).apply();
    }

    /**
     * Récupère le jeton d'authentification depuis les préférences partagées.
     *
     * @param context Le contexte de l'application.
     * @return Le jeton d'authentification, ou null s'il n'est pas présent.
     */
    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);
    }

    /**
     * Efface le jeton d'authentification des préférences partagées.
     */
    public static void clearToken() {
        SharedPreferences sharedPreferences = MyApplication.getAppContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }

    public static boolean isTokenValid(Context context) {
        String token = getToken(context);
        if (token == null) return false;

        try {
            String[] parts = token.split("\\."); // Découper le JWT en 3 parties
            if (parts.length < 2) return false; // Un JWT doit avoir 3 parties

            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(payload);

            long exp = jsonObject.getLong("exp"); // Récupérer le timestamp d’expiration
            long now = System.currentTimeMillis() / 1000; // Convertir le temps actuel en secondes

            return exp > now; // Retourne true si le token est toujours valide
        } catch (Exception e) {
            e.printStackTrace();
            return false; // En cas d'erreur, considère le token comme invalide
        }
    }
}
