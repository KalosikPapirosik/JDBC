package com.cinema.dao;

import com.cinema.db.ConnectionManager;
import com.cinema.model.Booking;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO для таблицы Бронирования.
 */
public class BookingDao {

    public List<Booking> findAll() throws SQLException {
        String sql = "SELECT id, guest_id, housing_id, additional_services_id, date_of_start, date_of_end, date_of_service FROM booking ORDER BY id";
        List<Booking> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    public Optional<Booking> findById(int id) throws SQLException {
        String sql = "SELECT id, guest_id, housing_id, additional_services_id, date_of_start, date_of_end, date_of_service FROM booking WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(Booking booking) throws SQLException {
        String sql = "INSERT INTO booking (guest_id, housing_id, additional_services_id, date_of_start, date_of_end, date_of_service) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getGuest_id());
            ps.setInt(2, booking.getHousing_id());
            ps.setInt(3, booking.getAdditional_services_id());
            ps.setObject(4, booking.getDate_of_start());
            ps.setObject(5, booking.getDate_of_end());
            ps.setObject(6, booking.getDate_of_service());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    booking.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить сгенерированный ключ");
        }
    }

    public boolean update(Booking booking) throws SQLException {
        String sql = "UPDATE booking SET guest_id = ?, housing_id = ?, additional_services_id = ?, date_of_start = ?, date_of_end = ?, date_of_service = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, booking.getGuest_id());
            ps.setInt(2, booking.getHousing_id());
            ps.setInt(3, booking.getAdditional_services_id());
            ps.setObject(4, booking.getDate_of_start());
            ps.setObject(5, booking.getDate_of_end());
            ps.setObject(6, booking.getDate_of_service());
            ps.setInt(7, booking.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM booking WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Optional<Booking> findByHousingAndGuestId(int housingId, int guestId){
        String sql = "SELECT id, guest_id, housing_id, additional_services_id, date_of_start, date_of_end, date_of_service from booking WHERE guest_id = ? AND housing_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            preparedStatement.setInt(1, guestId);
            preparedStatement.setInt(2, housingId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int bookingByHousingAndGuestId(int housingId, int guestId){

        if (!findByHousingAndGuestId(housingId, guestId).isEmpty()) throw new RuntimeException("Booking is already exists");

        Booking booking = new Booking(guestId, housingId, OffsetDateTime.now(), OffsetDateTime.now());

        String sql = "INSERT INTO booking (guest_id, housing_id, date_of_start, date_of_end) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getGuest_id());
            ps.setInt(2, booking.getHousing_id());
            ps.setObject(3, booking.getDate_of_start());
            ps.setObject(4, booking.getDate_of_end());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    booking.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить сгенерированный ключ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Booking mapRow(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("id"),
                rs.getInt("guest_id"),
                rs.getInt("housing_id"),
                rs.getInt("additional_services_id"),
                rs.getObject("date_of_start", OffsetDateTime.class),
                rs.getObject("date_of_end", OffsetDateTime.class),
                rs.getObject("date_of_service", OffsetDateTime.class)
        );
    }
}
