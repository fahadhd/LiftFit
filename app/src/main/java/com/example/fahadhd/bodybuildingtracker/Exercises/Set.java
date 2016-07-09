package com.example.fahadhd.bodybuildingtracker.Exercises;


import java.io.Serializable;

public class Set implements Serializable {
    long workout_id, set_id;
    int curr_rep, orderNum;

    public Set(long set_id, long workout_id, int orderNum, int curr_rep){
        this.set_id = set_id;
        this.workout_id = workout_id;
        this.curr_rep = curr_rep;
        this.orderNum = orderNum;
    }

    public long getSetID() {
        return set_id;
    }
    public long getWorkoutID(){
        return this.workout_id;
    }

    public int getCurrRep(){
        return this.curr_rep;
    }

    //Returns the set order number in a workout
    public int getSetNum(){
        return this.orderNum;
    }
}
