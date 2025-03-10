package com.example.gsb.utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class JwtUtils {
    public static String getRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\."); // Séparation du JWT en 3 parties
            if (parts.length != 3) {
                return null; // Format invalide
            }

            // Décoder la partie PAYLOAD (Base64)
            String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(payloadJson);

            // Récupérer le rôle
            return jsonObject.optString("idRole", null);
        } catch (Exception e) {
            Log.e("JwtUtils", "Erreur lors du décodage du token", e);
            return null;
        }
    }

    public static String getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\."); // Séparation du JWT en 3 parties
            if (parts.length != 3) {
                return null; // Format invalide
            }

            // Décoder la partie PAYLOAD (Base64)
            String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(payloadJson);

            // Récupérer l'id user
            return jsonObject.optString("idUser", null);
        } catch (Exception e) {
            Log.e("JwtUtils", "Erreur lors du décodage du token", e);
            return null;
        }
    }
}
