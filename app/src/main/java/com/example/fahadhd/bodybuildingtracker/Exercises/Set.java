package com.example.fahadhd.bodybuildingtracker.Exercises;


import java.io.Serializable;

public class Set implements Serializable {
    int workout_id, max_reps, curr_rep, set_num;

    public Set(int workout_id, int max_reps, int set_num){
        workout_id = workout_id;
        max_reps = max_reps;
        curr_rep = 0;
        set_num = set_num;
    }

    public void updateRep(){
        if(this.curr_rep < this.max_reps ){
            this.curr_rep++;
        }
        else{
            this.curr_rep = 0;
        }
    }
    public int getCurrRep(){
        return this.curr_rep;
    }
    public int getMaxReps(){
        return this.max_reps;
    }
    public int getWorkoutID(){
        return this.workout_id;
    }

    public int getSetNum(){
        return this.set_num;
    }
}
