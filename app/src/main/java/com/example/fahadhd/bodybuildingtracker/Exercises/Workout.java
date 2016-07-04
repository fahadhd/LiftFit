package com.example.fahadhd.bodybuildingtracker.Exercises;


import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable {
    int session_id, workout_num, weight, max_sets;
    String name;
    ArrayList<Set> sets;
    public Workout(int session_id, int workout_num, String name, int weight, int max_sets){
        this.session_id = session_id;
        this.workout_num = workout_num;
        this.name = name;
        this.weight = weight;
        this.max_sets = max_sets;
    }

}
