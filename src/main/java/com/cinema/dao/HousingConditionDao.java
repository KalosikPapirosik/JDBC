package com.cinema.dao;

import com.cinema.db.ConnectionManager;
import com.cinema.model.HousingCondition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO для таблицы Условия проживания.
 */
public class HousingConditionDao {

    public List<HousingCondition> findAll() throws SQLException {
        String sql = "SELECT id, name FROM housing_condition ORDER BY id";
        List<HousingCondition> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    public Optional<HousingCondition> findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM housing_condition WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(HousingCondition condition) throws SQLException {
        String sql = "INSERT INTO housing_condition (name) VALUES (?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, condition.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    condition.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить сгенерированный ключ");
        }
    }

    public boolean update(HousingCondition condition) throws SQLException {
        String sql = "UPDATE housing_condition SET name = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, condition.getName());
            ps.setInt(2, condition.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM housing_condition WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private HousingCondition mapRow(ResultSet rs) throws SQLException {
        return new HousingCondition(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
