package com.cinema.model;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Фильм.
 */
public class Housing {
    private int id;
    private int type_id;
    private int camp_id;
    private String comfort_lvl;
    private int capacity;
    private BigDecimal cost;
    private int housing_condition_id;

    public Housing() {}

    public Housing(int id, int type_id, int camp_id, String comfort_lvl, int capacity, BigDecimal cost, int housing_condition_id) {
        this.id = id;
        this.type_id = type_id;
        this.camp_id = camp_id;
        this.comfort_lvl = comfort_lvl;
        this.capacity = capacity;
        this.cost = cost;
        setHousing_Condition(housing_condition_id);
    }

    public Housing(int type_id, int camp_id, String comfort_lvl, int capacity, BigDecimal cost, int housing_condition_id) {
        this.type_id = type_id;
        this.camp_id = camp_id;
        this.comfort_lvl = comfort_lvl;
        this.capacity = capacity;
        this.cost = cost;
        setHousing_Condition(housing_condition_id);
    }

    public void setHousing_Condition(int id){
        this.housing_condition_id = id;
    }

    public int getHousing_condition_id(){
        return housing_condition_id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getComfort_lvl() {
        return comfort_lvl;
    }

    public int getCamp_id() {
        return camp_id;
    }

    public int getType_id() {
        return type_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setCamp_id(int camp_id) {
        this.camp_id = camp_id;
    }

    public void setComfort_lvl(String comfort_lvl) {
        this.comfort_lvl = comfort_lvl;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("Домid=%d, '%s', %d, %d}", id, comfort_lvl, capacity, housing_condition_id);
    }
}
