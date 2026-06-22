package com.cinema.dao;

import com.cinema.db.ConnectionManager;
import com.cinema.model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO для таблицы Гости.
 */
public class GuestDao {

    public List<Guest> findAll() throws SQLException {
        String sql = "SELECT id, document, lastname, firstname, patronymic FROM guest ORDER BY id";
        List<Guest> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    public Optional<Guest> findById(int id) throws SQLException {
        String sql = "SELECT id, document, lastname, firstname, patronymic FROM guest WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(Guest guest) throws SQLException {
        String sql = "INSERT INTO guest (document, lastname, firstname, patronymic) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, guest.getDocument());
            ps.setString(2, guest.getLastname());
            ps.setString(3, guest.getFirstname());
            ps.setString(4, guest.getPatronymic());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    guest.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить сгенерированный ключ");
        }
    }

    public boolean update(Guest guest) throws SQLException {
        String sql = "UPDATE guest SET document = ?, lastname = ?, firstname = ?, patronymic = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guest.getDocument());
            ps.setString(2, guest.getLastname());
            ps.setString(3, guest.getFirstname());
            ps.setString(4, guest.getPatronymic());
            ps.setInt(5, guest.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(String document, int guest_id) throws SQLException{
        String sql = "UPDATE guest SET document = ? where id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, document);
            ps.setInt(2, guest_id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM guest WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Guest mapRow(ResultSet rs) throws SQLException {
        return new Guest(
                rs.getInt("id"),
                rs.getString("document"),
                rs.getString("lastname"),
                rs.getString("firstname"),
                rs.getString("patronymic")
        );
    }
}
