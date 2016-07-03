package com.example.fahadhd.bodybuildingtracker.Sessions;

import java.io.Serializable;

public class Session implements Serializable {
    String date;
    int user_weight;
    long sessionId;

    public Session(String date, int user_weight, long sessionId){
        this.date = date;
        this.user_weight = user_weight;
        this.sessionId = sessionId;
    }

    public String getDate(){
        return this.date;
    }
    public Integer getWeight(){
        return this.user_weight;
    }
    public Long getSessionId(){
        return this.sessionId;
    }

    public String toString(){
        return "Session: "+sessionId+" | Date: "+date+ " | User_Weight "+user_weight;
    }
}
