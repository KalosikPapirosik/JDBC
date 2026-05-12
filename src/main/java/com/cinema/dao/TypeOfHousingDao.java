package com.cinema.dao;

import com.cinema.db.ConnectionManager;
import com.cinema.model.TypeOfHousing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO для таблицы Типы жилья.
 */
public class TypeOfHousingDao {

    public List<TypeOfHousing> findAll() throws SQLException {
        String sql = "SELECT id, name FROM type_of_housing ORDER BY id";
        List<TypeOfHousing> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    public Optional<TypeOfHousing> findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM type_of_housing WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(TypeOfHousing housingType) throws SQLException {
        String sql = "INSERT INTO type_of_housing (name) VALUES (?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, housingType.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    housingType.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить сгенерированный ключ");
        }
    }

    public boolean update(TypeOfHousing housingType) throws SQLException {
        String sql = "UPDATE type_of_housing SET name = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, housingType.getName());
            ps.setInt(2, housingType.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM type_of_housing WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private TypeOfHousing mapRow(ResultSet rs) throws SQLException {
        return new TypeOfHousing(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
