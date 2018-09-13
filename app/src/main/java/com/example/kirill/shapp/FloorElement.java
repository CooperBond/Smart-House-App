package com.example.kirill.shapp;

public class FloorElement {
    private String floor_name;
    private int temperature;
    private int snow_mode;
    private int fire_mode;
    private int issue_status;
    private int background_id;
    private int floor_plan_resource;

    public FloorElement() {

    }

    public int getFire_mode() {
        return fire_mode;
    }

    public int getIssue_status() {
        return issue_status;
    }

    public int getSnow_mode() {
        return snow_mode;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getFloor_name() {
        return floor_name;
    }

    public void setFire_mode(int fire_mode) {
        this.fire_mode = fire_mode;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }

    public void setIssue_status(int issue_status) {
        this.issue_status = issue_status;
    }

    public void setSnow_mode(int snow_mode) {
        this.snow_mode = snow_mode;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getBackground_id() {
        return background_id;
    }

    public void setBackground_id(int background_id) {
        this.background_id = background_id;
    }

    public int getFloor_plan_resource() {
        return floor_plan_resource;
    }

    public void setFloor_plan_resource(int floor_plan_resource) {
        this.floor_plan_resource = floor_plan_resource;
    }
}
