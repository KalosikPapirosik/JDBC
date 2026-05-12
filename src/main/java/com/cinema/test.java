package com.cinema;

import com.cinema.dao.AdditionalServicesDao;
import com.cinema.dao.HousingDao;
import com.cinema.model.AdditionalServices;
import com.cinema.model.Housing;

import java.math.BigDecimal;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) throws SQLException {
        HousingDao housingDao = new HousingDao();
        BigDecimal cost = BigDecimal.valueOf(12000.00);
        AdditionalServicesDao additionalServicesDao = new AdditionalServicesDao();
        for(AdditionalServices a : additionalServicesDao.findAll()){
            System.out.println(a.toString());
        }
    }
}
