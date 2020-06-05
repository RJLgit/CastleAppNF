package com.example.android.castleappnf;


import java.io.Serializable;
import java.util.Comparator;

/*Class that represents the castles themselves, implements Serializable so it can be passed between activities in intents.*/
public class Castles implements Serializable {
    private String name;
    private String operator;
    private int rating;
    private String[] history;
    private int audio;
    private float distance;
    private double longdi;
    private double lat;
    private String website;
    private String openingTimes;

    public Castles(String name, String operator, int rating, String[] history, int audio, double longditude, double latitude, String web, String openingTimes) {
        this.name = name;
        this.operator = operator;
        this.rating = rating;
        this.history = history;
        this.audio = audio;
        //Hardcoded distance in case location on phone doesn't work
        this.distance = 45;
        this.lat = latitude;
        this.longdi = longditude;
        this.website = web;
        this.openingTimes = openingTimes;
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

    public String[] getHistory() {
        return history;
    }

    public void setHistory(String[] history) {
        this.history = history;
    }


    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getLongdi() {
        return longdi;
    }

    public void setLongdi(double longdi) {
        this.longdi = longdi;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(String openingTimes) {
        this.openingTimes = openingTimes;
    }

    //Comparator to allow sorting by A-Z
    static class AZComparator implements Comparator<Castles> {
        @Override
        public int compare(Castles castles, Castles t1) {
           return castles.getName().compareTo(t1.getName());
        }
    }
    //Comparator to allow sorting by distance
    static class DistanceComparator implements Comparator<Castles> {
        @Override
        public int compare(Castles castles, Castles t1) {
            if(t1.getDistance()==castles.getDistance())
                return 0;
            else if(castles.getDistance()>t1.getDistance())
                return 1;
            else
                return -1;
        }
    }
    //Comparator to allow sorting by rating
    static class RatingComparator implements Comparator<Castles> {
        @Override
        public int compare(Castles castles, Castles t1) {
            if(t1.getRating()==castles.getRating())
                return 0;
            else if(castles.getRating()>t1.getRating())
                return -1;
            else
                return 1;
        }
    }
    
}
