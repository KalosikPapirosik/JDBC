package com.cinema.dao;

import com.cinema.db.ConnectionManager;
import com.cinema.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO для таблицы Инвентарь.
 */
public class InventoryDao {

    public List<Inventory> findAll() throws SQLException {
        String sql = "SELECT id, camp_id, type_of_inventory, quantity FROM inventory ORDER BY id";
        List<Inventory> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    public Optional<Inventory> findById(int id) throws SQLException {
        String sql = "SELECT id, camp_id, type_of_inventory, quantity FROM inventory WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(Inventory inventory) throws SQLException {
        String sql = "INSERT INTO inventory (camp_id, type_of_inventory, quantity) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inventory.getCamp_id());
            ps.setString(2, inventory.getType_of_inventory());
            ps.setInt(3, inventory.getQuantity());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    inventory.setId(id);
                    return id;
                }
            }
            throw new SQLException("Не удалось получить сгенерированный ключ");
        }
    }

    public boolean update(Inventory inventory) throws SQLException {
        String sql = "UPDATE inventory SET camp_id = ?, type_of_inventory = ?, quantity = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inventory.getCamp_id());
            ps.setString(2, inventory.getType_of_inventory());
            ps.setInt(3, inventory.getQuantity());
            ps.setInt(4, inventory.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM inventory WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Inventory mapRow(ResultSet rs) throws SQLException {
        return new Inventory(
                rs.getInt("id"),
                rs.getInt("camp_id"),
                rs.getString("type_of_inventory"),
                rs.getInt("quantity")
        );
    }
}
