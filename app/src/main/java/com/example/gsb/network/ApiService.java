package com.example.gsb.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.gsb.MyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiService {

    private static final String BASE_URL = "http://10.0.2.2:5000/";

    public void login(String email, String password, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "user/login";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mail", email);
            jsonBody.put("password", password);
        } catch (Exception e) {
            callback.onError("Erreur JSON");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> callback.onSuccess(response),
                error -> callback.onError("Erreur de connexion: " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }

    public void logout(String token, ApiCallback<String> callback) {
        String url = BASE_URL + "user/logout";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> callback.onSuccess(response),
                error -> callback.onError("Erreur de connexion: " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }

    public void getAllUser(String token, ApiCallback<JSONArray> callback) {
        String url = BASE_URL + "users";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                callback::onSuccess,
                error -> callback.onError("Erreur de connexion: " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }

    public void getUserById(String token, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "user/userInfo";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> callback.onSuccess(response),
                error -> callback.onError("Erreur de connexion: " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }

    public void updateUser(String firstName, String lastName, String email, String password, String token, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "user/editUser";
        JSONObject jsonBody = new JSONObject();
        try {
            if (firstName != null) jsonBody.put("firstName", firstName);
            if (lastName != null) jsonBody.put("lastName", lastName);
            if (email != null) jsonBody.put("mail", email);
            if (password != null) jsonBody.put("password", password);
        } catch (Exception e) {
            callback.onError("Erreur JSON");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, jsonBody,
                response -> callback.onSuccess(response),
                error -> callback.onError("Erreur de connexion: " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }

    public interface ApiCallback<T> {
        void onSuccess(T response);
        void onError(String errorMessage);

        void onFailure(String errorMessage);
    }
}
