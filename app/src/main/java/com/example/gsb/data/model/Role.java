package com.example.gsb.data.model;

public class Role {
    private int id_role;
    private String description;

    public Role(int id_role, String description) {
        this.id_role = id_role;
        this.description = description;
    }

    public int getId_role() {
        return id_role;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
