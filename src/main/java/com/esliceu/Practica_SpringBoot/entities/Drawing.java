package com.esliceu.Practica_SpringBoot.entities;

public class Drawing {
    private String json;
    private User usuari;
    private int id;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public User getUsuari() {
        return usuari;
    }

    public void setUsuari(User usuari) {
        this.usuari = usuari;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
