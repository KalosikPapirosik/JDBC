package com.cinema.dao;

import com.cinema.db.ConnectionManager;
import com.cinema.model.Booking;
import com.cinema.model.Housing;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO для таблицы Фильм + связь M:N с Жанром.
 */
public class HousingDao {

    public List<Housing> findAll() throws SQLException {
        String sql = "SELECT id, type_id, camp_id, comfort_lvl, capacity, cost, housing_condition_id FROM housing ORDER BY id";
        List<Housing> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    public Optional<Housing> findById(int id) throws SQLException {
        String sql = "SELECT id, type_id, camp_id, comfort_lvl, capacity, cost, housing_condition_id FROM housing WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(Housing housing) throws SQLException {
        String sql = "insert into housing (type_id, camp_id, comfort_lvl, capacity, cost, housing_condition_id) values (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, housing.getType_id());
            ps.setInt(2, housing.getCamp_id());
            ps.setString(3, housing.getComfort_lvl());
            ps.setInt(4, housing.getCapacity());
            ps.setBigDecimal(5, housing.getCost());
            ps.setInt(6, housing.getHousing_condition_id());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    housing.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить ключ");
        }
    }

    public boolean update(Housing housing) throws SQLException {
        String sql = "UPDATE housing SET type_id = ?, camp_id = ?, comfort_lvl = ?, capacity = ?, cost = ?, housing_condition_id = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, housing.getType_id());
            ps.setInt(2, housing.getCamp_id());
            ps.setString(3, housing.getComfort_lvl());
            ps.setInt(4, housing.getCapacity());
            ps.setBigDecimal(5, housing.getCost());
            ps.setInt(6, housing.getHousing_condition_id());
            ps.setInt(7, housing.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM housing WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Booking> findBookingsByHousing(int filmId) throws SQLException {
       String sql = """
               SELECT b.housing_id, b.id, b.guest_id, b.additional_services_id, b.date_of_start, b.date_of_end, b.date_of_service
               FROM booking b
               JOIN housing h ON b.housing_id = h.id
               where h.id = ?
               ORDER BY b.id
              """;
       List<Booking> result = new ArrayList<>();
       try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
           ps.setInt(1, filmId);
          try (ResultSet rs = ps.executeQuery()) {
               while (rs.next()) {
                  result.add(new Booking(rs.getInt("id"),
                          rs.getInt("guest_id"),
                          rs.getInt("housing_id"),
                          rs.getInt("additional_services_id"),
                          rs.getObject("date_of_start", OffsetDateTime.class),
                          rs.getObject("date_of_end", OffsetDateTime.class),
                          rs.getObject("date_of_service", OffsetDateTime.class)));
              }
          }
       }
       return result;
   }

    private Housing mapRow(ResultSet rs) throws SQLException {
        return new Housing(
                rs.getInt("id"),
                rs.getInt("type_id"),
                rs.getInt("camp_id"),
                rs.getString("comfort_lvl"),
                rs.getInt("capacity"),
                rs.getBigDecimal("cost"),
                rs.getInt("housing_condition_id")
        );
    }
}
