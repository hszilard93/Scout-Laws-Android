package com.myprojects.b4kancs.scoutlaws.data.model;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class ScoutLaw {
    public final int number;
    public final String text;
    public final String description;
    private int weight;

    public ScoutLaw(int number, String text, String description) {
        this.number = number;
        this.text = text;
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
