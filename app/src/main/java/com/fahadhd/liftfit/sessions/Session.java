package com.fahadhd.liftfit.sessions;

import com.fahadhd.liftfit.exercises.Workout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Session implements Serializable {
    String date;
    int user_weight;
    long sessionId;
    String templateName;
   public ArrayList<Workout> workouts;

    public Session(String date, int user_weight, long sessionId, ArrayList<Workout> workouts,String templateName){
        this.date = date;
        this.user_weight = user_weight;
        this.sessionId = sessionId;
        this.workouts = workouts;
        this.templateName = templateName;
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
    public long getSessionId(){
        return this.sessionId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void updateTemplateName(String newName){
        this.templateName = newName;
    }

    public String toString(){
        return "Session: "+sessionId+" | Date: "+date+ " | User_Weight "+user_weight;
    }

    public void updateWorkouts(List<Workout> data){
        this.workouts = new ArrayList<>(data);
    }
}
