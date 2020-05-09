package com.example.android.castleappnf;

import android.os.Parcelable;

import java.io.Serializable;

public class Castles implements Serializable {
    private String name;
    private String operator;
    //Need to add time and geolocation in future
    private int rating;
    private String history;
    private int image;
    private int audio;

    public Castles(String name, String operator, int rating, String history, int image, int audio) {
        this.name = name;
        this.operator = operator;
        this.rating = rating;
        this.history = history;
        this.image = image;
        this.audio = audio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }


}
