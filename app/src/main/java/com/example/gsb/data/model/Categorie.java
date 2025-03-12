package com.example.gsb.data.model;

public class Categorie {
    private Long idCategorie;
    private String nameCategorie;

    public Categorie(Long idCategorie, String nameCategorie) {
        this.idCategorie = idCategorie;
        this.nameCategorie = nameCategorie;
    }

    public Long getIdCategorie() {
        return idCategorie;
    }

    public String getNameCategorie() {
        return nameCategorie;
    }
}
