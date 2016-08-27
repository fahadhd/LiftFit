package com.example.fahadhd.bodybuildingtracker.exercises;

import java.io.Serializable;
import java.util.ArrayList;

/**Templates hold workouts in which the user saved**/
public class Template implements Serializable {
    ArrayList<Workout> workouts;
    String templateName;
    public Template(ArrayList<Workout> workouts, String templateName){
        this.workouts = workouts;
        this.templateName  = templateName;
    }

    public void increaseWeight(){
        for(Workout workout: workouts){
            //TODO: Change from 5 to the users preferred increase in weight.
            workout.weight +=5;
        }
    }

    public ArrayList<Workout> getWorkouts() {
        return this.workouts;
    }

}
