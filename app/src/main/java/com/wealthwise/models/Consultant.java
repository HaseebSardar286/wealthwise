package com.wealthwise.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Consultant {
    private String uid;
    private String name;
    private String specialty;
    private String profileImageUrl;
    private double rating;
    private int ratingCount;

    public Consultant() {}

    public Consultant(String uid, String name, String specialty, String profileImageUrl, double rating, int ratingCount) {
        this.uid = uid;
        this.name = name;
        this.specialty = specialty;
        this.profileImageUrl = profileImageUrl;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}
