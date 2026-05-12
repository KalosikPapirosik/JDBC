package com.cinema.model;

import java.math.BigDecimal;

public class AdditionalServices {
    private int id;
    private String label;
    private BigDecimal cost;

    public AdditionalServices(int id, String label, BigDecimal cost) {
        this.id = id;
        this.label = label;
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "AdditionalServices{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", cost=" + cost +
                '}';
    }
}
