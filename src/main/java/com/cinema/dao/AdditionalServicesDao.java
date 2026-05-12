package com.cinema.dao;

import com.cinema.db.ConnectionManager;
import com.cinema.model.AdditionalServices;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO для таблицы Дополнительные услуги.
 */
public class AdditionalServicesDao {

    public List<AdditionalServices> findAll() throws SQLException {
        String sql = "SELECT id, label, cost FROM additional_services ORDER BY id";
        List<AdditionalServices> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    public Optional<AdditionalServices> findById(int id) throws SQLException {
        String sql = "SELECT id, label, cost FROM additional_services WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(AdditionalServices services) throws SQLException {
        String sql = "INSERT INTO additional_services (label, cost) VALUES (?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, services.getLabel());
            ps.setBigDecimal(2, services.getCost());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    services.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить сгенерированный ключ");
        }
    }

    public boolean update(AdditionalServices services) throws SQLException {
        String sql = "UPDATE additional_services SET label = ?, cost = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, services.getLabel());
            ps.setBigDecimal(2, services.getCost());
            ps.setInt(3, services.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM additional_services WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private AdditionalServices mapRow(ResultSet rs) throws SQLException {
        return new AdditionalServices(
                rs.getInt("id"),
                rs.getString("label"),
                rs.getBigDecimal("cost")
        );
    }
}
