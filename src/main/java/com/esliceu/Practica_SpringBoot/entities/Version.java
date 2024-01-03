package com.esliceu.Practica_SpringBoot.entities;

import java.sql.Timestamp;

public class Version {
    private String json;
    private int id;
    private Timestamp timeStamp;
    private int id_drawing;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getId_drawing() {
        return id_drawing;
    }

    public void setId_drawing(int id_drawing) {
        this.id_drawing = id_drawing;
    }
}
