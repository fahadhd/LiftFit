package com.example.fahadhd.bodybuildingtracker.Utility;

public class Session {
    String date;
    int user_weight;

    public Session(String date, int user_weight){
        this.date = date;
        this.user_weight = user_weight;
    }

    public String getDate(){
        return this.date;
    }
    public int getWeight(){
        return this.user_weight;
    }
}
