package com.example.gsb.data.model;

public class Categorie {
    private int idCategorie;
    private String nameCategorie;

    public Categorie(int idCategorie, String nameCategorie) {
        this.idCategorie = idCategorie;
        this.nameCategorie = nameCategorie;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public String getNameCategorie() {
        return nameCategorie;
    }
}
