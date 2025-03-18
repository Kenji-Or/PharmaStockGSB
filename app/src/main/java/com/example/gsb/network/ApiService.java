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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void getAllRoles(String token, ApiCallback<JSONArray> callback) {
        String url = BASE_URL + "roles";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                callback::onSuccess,
                error -> callback.onFailure("Erreur API: " + error.getMessage())
        ) {
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

    public void getAllCategory(String token, ApiCallback<JSONArray> callback) {
        String url = BASE_URL + "category";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                callback::onSuccess,
                error -> callback.onFailure("Erreur API: " + error.getMessage())) {
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

    public void getAllMedicaments(String token, ApiCallback<JSONArray> callback) {
        String url = BASE_URL + "medicaments";

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

    public void getMedicamentById(String token, Long medicamentId, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "medicament/" + medicamentId;

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

    public void getCategoryById(String token, int id, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "category/" + id;

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

    public void getUserById(String token, Long userId, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "user/" + userId;

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

    public void getMedicamentByExpiredDate(String token, ApiCallback<JSONArray> callback) {
        String url = BASE_URL + "medicament/dateexpiration";

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

    public void createUser(String token, String firstName, String lastName, String email, int idRole, String password, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "user/create";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("firstName", firstName);
            jsonBody.put("lastName", lastName);
            jsonBody.put("mail", email);
            jsonBody.put("password", password);
            jsonBody.put("role", idRole);
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
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }

    public void createMedicament(String token, String name, int quantity, int category, String dateExpiration, int alerteStock, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "medicament/create";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", name);
            jsonBody.put("quantity", quantity);
            jsonBody.put("category", category);
            jsonBody.put("date_expiration", dateExpiration);
            jsonBody.put("alerte_stock", alerteStock);
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
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }

    public void createCategory(String token, String name, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "category/create";
        JSONObject jsonBody = new JSONObject();

        try{
            jsonBody.put("name", name);
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
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(request);
    }


    public void updateUser(Long userId, String firstName, String lastName, String email, Integer role, String password, String token, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "user/editUser/" + userId;
        JSONObject jsonBody = new JSONObject();
        try {
            if (firstName != null) jsonBody.put("firstName", firstName);
            if (lastName != null) jsonBody.put("lastName", lastName);
            if (email != null) jsonBody.put("mail", email);
            if (role != null) jsonBody.put("role", role);
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

    public void updateMedicament(String token, Long idMedicament, String name, Integer quantity, Integer categorie, String dateExpiration, Integer alerteStock, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "medicament/" + idMedicament;
        JSONObject jsonBody = new JSONObject();

        try{
            if (name != null) jsonBody.put("name", name);
            if (quantity != null) jsonBody.put("quantity", quantity);
            if (categorie != null) jsonBody.put("category", categorie);
            if (dateExpiration != null && !dateExpiration.isEmpty()) jsonBody.put("date_expiration", dateExpiration);
            if (alerteStock != null) jsonBody.put("alerte_stock", alerteStock);
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

    public void updateCategorie(String token, int categorieId, String nameCategorie, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "category/" + categorieId;
        JSONObject jsonBody = new JSONObject();

        try{
            if (nameCategorie != null) jsonBody.put("name", nameCategorie);
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

    public void deleteUser(String token, Long userId, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "user/delete/" + userId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> callback.onSuccess(null),
                error -> callback.onFailure("Erreur de suppression : " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
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

    public void deleteMedicament(String token, Long medicamentId, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "medicament/" + medicamentId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> callback.onSuccess(null),
                error -> callback.onFailure("Erreur de suppression : " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
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

    public void deleteCategorie(String token, int categorieId, ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "category/" + categorieId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> callback.onSuccess(null),
                error -> callback.onFailure("Erreur de suppression : " + (error.networkResponse != null ? error.networkResponse.statusCode : "Unknown"))) {
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
