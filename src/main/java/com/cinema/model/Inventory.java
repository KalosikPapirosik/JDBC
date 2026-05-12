package com.cinema.model;

public class Inventory {
    int id;
    int camp_id;
    String type_of_inventory;
    int quantity;

    public Inventory(int id, int camp_id, String type_of_inventory, int quantity) {
        this.id = id;
        this.camp_id = camp_id;
        this.type_of_inventory = type_of_inventory;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getCamp_id() {
        return camp_id;
    }

    public String getType_of_inventory() {
        return type_of_inventory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCamp_id(int camp_id) {
        this.camp_id = camp_id;
    }

    public void setType_of_inventory(String type_of_inventory) {
        this.type_of_inventory = type_of_inventory;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", camp_id=" + camp_id +
                ", type_of_inventory='" + type_of_inventory + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
