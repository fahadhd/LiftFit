package com.example.fahadhd.bodybuildingtracker.sessions;

import com.example.fahadhd.bodybuildingtracker.exercises.Workout;

import java.io.Serializable;
import java.util.ArrayList;

public class Session implements Serializable {
    String date;
    int user_weight;
    long sessionId;
    ArrayList<Workout> workouts;

    public Session(String date, int user_weight, long sessionId, ArrayList<Workout> workouts){
        this.date = date;
        this.user_weight = user_weight;
        this.sessionId = sessionId;
        this.workouts = workouts;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
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
