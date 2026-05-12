package com.cinema.model;

import java.time.OffsetDateTime;

public class Booking {
    private int id;
    private int guest_id;
    private int housing_id;
    private int additional_services_id;
    private OffsetDateTime date_of_start;
    private OffsetDateTime date_of_end;
    private OffsetDateTime date_of_service;

    public Booking(int id, int guest_id, int housing_id, int additional_services_id, OffsetDateTime date_of_start, OffsetDateTime date_of_end, OffsetDateTime date_of_service) {
        this.id = id;
        this.guest_id = guest_id;
        this.housing_id = housing_id;
        this.additional_services_id = additional_services_id;
        this.date_of_start = date_of_start;
        this.date_of_end = date_of_end;
        this.date_of_service = date_of_service;
    }

    public int getId() {
        return id;
    }

    public int getGuest_id() {
        return guest_id;
    }

    public int getHousing_id() {
        return housing_id;
    }

    public int getAdditional_services_id() {
        return additional_services_id;
    }

    public OffsetDateTime getDate_of_start() {
        return date_of_start;
    }

    public OffsetDateTime getDate_of_end() {
        return date_of_end;
    }

    public OffsetDateTime getDate_of_service() {
        return date_of_service;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public void setHousing_id(int housing_id) {
        this.housing_id = housing_id;
    }

    public void setAdditional_services_id(int additional_services_id) {
        this.additional_services_id = additional_services_id;
    }

    public void setDate_of_start(OffsetDateTime date_of_start) {
        this.date_of_start = date_of_start;
    }

    public void setDate_of_end(OffsetDateTime date_of_end) {
        this.date_of_end = date_of_end;
    }

    public void setDate_of_service(OffsetDateTime date_of_service) {
        this.date_of_service = date_of_service;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", guest_id=" + guest_id +
                ", housing_id=" + housing_id +
                ", additional_services_id=" + additional_services_id +
                ", date_of_start=" + date_of_start +
                ", date_of_end=" + date_of_end +
                ", date_of_service=" + date_of_service +
                '}';
    }
}
