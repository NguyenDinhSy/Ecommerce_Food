package com.example.ecommerce.Model;

public class Ratings
{


    private String pid;
    private String ratingValue;
    private String userphone;
    private String comment;

    public Ratings() {
    }


    public Ratings(String pid, String ratingValue, String userphone, String comment) {
        this.pid = pid;
        this.ratingValue = ratingValue;
        this.userphone = userphone;
        this.comment = comment;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
