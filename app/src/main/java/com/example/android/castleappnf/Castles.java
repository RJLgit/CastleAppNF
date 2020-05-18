package com.example.android.castleappnf;

import android.location.Location;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;
import java.util.Comparator;

public class Castles implements Serializable {
    private String name;
    private String operator;
    //Need to add time and geolocation in future
    private int rating;
    private String history;
    private int[] image;
    private int audio;
    //Hard coded until calculated from location data and phone location
    private float distance;
    private double longdi;
    private double lat;
    private String website;
    private String openingTimes;
    //private Location castleLocation;

    public Castles(String name, String operator, int rating, String history, int[] images, int audio, double longditude, double latitude, String web, String openingTimes) {
        this.name = name;
        this.operator = operator;
        this.rating = rating;
        this.history = history;
        this.image = images;
        this.audio = audio;
        this.distance = 45;
        this.lat = latitude;
        this.longdi = longditude;
        this.website = web;
        this.openingTimes = openingTimes;
        //castleLocation = new Location("");
        //castleLocation.setLatitude(latitude);
        //castleLocation.setLongitude(longditude);
    }

  /*  public Location getCastleLocation() {
        return castleLocation;
    }

    public void setCastleLocation(Location castleLocation) {
        this.castleLocation = castleLocation;
    }*/

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

    public int[] getImage() {
        return image;
    }

    public void setImage(int[] image) {
        this.image = image;
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

    /*@Override
    public int compareTo(Castles castles) {
        if(distance==castles.distance)
            return 0;
        else if(distance>castles.distance)
            return 1;
        else
            return -1;
    }*/

    static class AZComparator implements Comparator<Castles> {
        @Override
        public int compare(Castles castles, Castles t1) {
           return castles.getName().compareTo(t1.getName());
        }
    }

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
