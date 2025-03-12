package com.example.gsb.data.model;

public class Medicament {
    private Long id;
    private String name;
    private int quantite;
    private Long category;
    private String dateExpiration;
    private int alerte_stock;

    public Medicament(Long id, String name, int quantite, Long category, String dateExpiration, int alerte_stock) {
        this.id = id;
        this.name = name;
        this.quantite = quantite;
        this.category = category;
        this.dateExpiration = dateExpiration;
        this.alerte_stock = alerte_stock;
    }

    public Long getIdMedicament() {
        return id;
    }
    public String getName() {
        return name;
    }

    public int getQuantite() {
        return quantite;
    }

    public Long getCategory() {
        return category;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public int getAlerte_stock() {
        return alerte_stock;
    }

}
