package com.fahadhd.liftfit.exercises;


import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable {
    long session_id, workout_id;
    int weight, max_sets, max_reps;
    String name, task;
    ArrayList<Set> sets;


    public Workout(long session_id, long workout_id,
                   String name, int weight, int max_sets, int max_reps, ArrayList<Set> sets){
        this.session_id = session_id;
        this.workout_id  = workout_id;
        this.name = name;
        this.weight = weight;
        this.max_sets = max_sets;
        this.max_reps = max_reps;
        this.sets = sets;
    }

    public Workout(long session_id,
                   String name, int weight, int max_sets, int max_reps,String task){
        this.session_id = session_id;
        this.name = name;
        this.weight = weight;
        this.max_sets = max_sets;
        this.max_reps = max_reps;
        this.task = task;
    }


    public Workout(Workout workout){
        this.session_id = workout.session_id;
        this.workout_id  = workout.workout_id;
        this.name = workout.name;
        this.weight = workout.weight;
        this.max_sets = workout.max_sets;
        this.max_reps = workout.max_reps;
        this.sets = workout.sets;

    }

    public String getTask(){
        return this.task;
    }

    public void updateTask(String task){
        this.task = task;
    }

    public Workout(String buttonName){
        this.name = buttonName;
    }


    public String getName(){
        return this.name;
    }

    public long getWorkoutID(){
        return workout_id;
    }

    public long getSessionID() {
        return session_id;
    }

    public int getWeight(){
        return this.weight;
    }
    public int getMaxSets(){
        return this.max_sets;
    }
    public int getMaxReps(){
        return this.max_reps;
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Workout))return false;
        Workout workout = (Workout)other;

        return (workout.getWorkoutID() == this.getWorkoutID() && workout.getName().equals(this.getName())
                && workout.getWeight() == this.getWeight() && workout.getMaxSets() == this.getMaxSets() &&
                workout.getMaxReps() == this.getMaxReps());
    }
}