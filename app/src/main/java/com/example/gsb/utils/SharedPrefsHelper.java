package com.example.gsb.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gsb.MyApplication;

public class SharedPrefsHelper {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String TOKEN_KEY = "auth_token";

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
}
