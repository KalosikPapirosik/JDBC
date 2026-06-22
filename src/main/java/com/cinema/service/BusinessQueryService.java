package com.cinema.service;

import com.cinema.db.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;

public class BusinessQueryService {

    // =================================================================
    // БИЗНЕС-ЗАПРОСЫ ДЛЯ СИСТЕМЫ БРОНИРОВАНИЯ ЖИЛЬЯ В КЕМПАХ
    // =================================================================

    /**
     * 1. Выручка по типам жилья
     * Показывает, какие типы жилья приносят наибольший доход.
     */
    public void revenueByHousingType() throws SQLException {
        System.out.println("=== Выручка по типам жилья ===");
        String sql = """
                SELECT
                    th.name AS type_name,
                    COUNT(DISTINCT b.id) AS bookings_count,
                    SUM(h.cost) AS total_revenue
                FROM booking b
                JOIN housing h ON b.housing_id = h.id
                JOIN type_of_housing th ON h.type_id = th.id
                GROUP BY th.id, th.name
                ORDER BY total_revenue DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf(Locale.US, "%-20s %-12s %-15s%n", "Тип жилья", "Бронирований", "Выручка (₽)");
            System.out.println("─".repeat(50));
            while (rs.next()) {
                System.out.printf(Locale.US, "%-20s %-12d %-15.2f%n",
                        rs.getString("type_name"),
                        rs.getInt("bookings_count"),
                        rs.getBigDecimal("total_revenue"));
            }
        }
        System.out.println();
    }

    /**
     * 2. Статистика по гостям: кто чаще бронирует и сколько тратит
     * Топ-10 самых активных гостей по количеству бронирований и общей сумме.
     */
    public void topGuestsByActivity() throws SQLException {
        System.out.println("=== Топ-10 активных гостей ===");
        String sql = """
                SELECT
                    g.firstname || ' ' || g.lastname AS full_name,
                    g.document,
                    COUNT(b.id) AS bookings,
                    SUM(h.cost + COALESCE(asvc.cost, 0)) AS total_spent
                FROM guest g
                JOIN booking b ON g.id = b.guest_id
                JOIN housing h ON b.housing_id = h.id
                LEFT JOIN additional_services asvc ON b.additional_services_id = asvc.id
                GROUP BY g.id, g.firstname, g.lastname, g.document
                ORDER BY bookings DESC, total_spent DESC
                LIMIT 10
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf(Locale.US, "%-25s %-15s %-10s %-12s%n", "Гость", "Документ", "Броней", "Потрачено (₽)");
            System.out.println("─".repeat(65));
            int rank = 1;
            while (rs.next()) {
                System.out.printf(Locale.US, "%-25s %-15s %-10d %-12.2f%n",
                        truncate(rs.getString("full_name"), 24),
                        rs.getString("document"),
                        rs.getInt("bookings"),
                        rs.getBigDecimal("total_spent"));
                rank++;
            }
        }
        System.out.println();
    }

    /**
     * 3. Заполняемость кемпов: сравнение вместимости и фактических бронирований
     * Показывает, насколько эффективно используются ресурсы каждого кемпа.
     */
    public void campOccupancyReport() throws SQLException {
        System.out.println("=== Заполняемость кемпов ===");
        String sql = """
                SELECT
                    c.location AS camp_location,
                    COUNT(DISTINCT h.id) AS total_housing,
                    SUM(h.capacity) AS total_capacity,
                    COUNT(DISTINCT b.id) AS active_bookings,
                    ROUND(COUNT(DISTINCT b.id) * 100.0 / NULLIF(COUNT(DISTINCT h.id), 0), 1) AS occupancy_rate
                FROM camp c
                LEFT JOIN housing h ON c.id = h.camp_id
                LEFT JOIN booking b ON h.id = b.housing_id
                    AND b.date_of_end >= CURRENT_DATE
                GROUP BY c.id, c.location
                ORDER BY occupancy_rate DESC NULLS LAST
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-25s %-10s %-12s %-15s %-10s%n",
                    "Кемп", "Единиц", "Вместимость", "Актив. броней", "Загруз.%");
            System.out.println("─".repeat(75));
            while (rs.next()) {
                System.out.printf("%-25s %-10d %-12d %-15d %-10.1f%n",
                        truncate(rs.getString("camp_location"), 24),
                        rs.getInt("total_housing"),
                        rs.getInt("total_capacity"),
                        rs.getInt("active_bookings"),
                        rs.getDouble("occupancy_rate"));
            }
        }
        System.out.println();
    }

    /**
     * 4. Популярность дополнительных услуг
     * Какие доп.услуги чаще всего заказывают и какой доход они приносят.
     */
    public void additionalServicesPopularity() throws SQLException {
        System.out.println("=== Популярность дополнительных услуг ===");
        String sql = """
                SELECT
                    asvc.label AS service_name,
                    COUNT(b.id) AS times_ordered,
                    SUM(asvc.cost) AS revenue_from_service,
                    ROUND(AVG(asvc.cost), 2) AS avg_price
                FROM additional_services asvc
                JOIN booking b ON asvc.id = b.additional_services_id
                GROUP BY asvc.id, asvc.label, asvc.cost
                ORDER BY times_ordered DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf(Locale.US, "%-25s %-12s %-15s %-10s%n",
                    "Услуга", "Заказов", "Доход (₽)", "Сред.цена");
            System.out.println("─".repeat(65));
            while (rs.next()) {
                System.out.printf(Locale.US, "%-25s %-12d %-15.2f %-10.2f%n",
                        truncate(rs.getString("service_name"), 24),
                        rs.getInt("times_ordered"),
                        rs.getBigDecimal("revenue_from_service"),
                        rs.getBigDecimal("avg_price"));
            }
        }
        System.out.println();
    }

    /**
     * 5. Динамика бронирований по датам (за последний месяц)
     * Показывает тренды: в какие дни чаще бронируют и какая выручка.
     */
    public void bookingTrendsLastMonth() throws SQLException {
        System.out.println("=== Динамика бронирований (последние 30 дней) ===");
        String sql = """
                SELECT
                    DATE(b.date_of_start) AS booking_date,
                    COUNT(b.id) AS new_bookings,
                    SUM(h.cost + COALESCE(asvc.cost, 0)) AS daily_revenue,
                    AVG(h.cost + COALESCE(asvc.cost, 0)) AS avg_check
                FROM booking b
                JOIN housing h ON b.housing_id = h.id
                LEFT JOIN additional_services asvc ON b.additional_services_id = asvc.id
                WHERE b.date_of_start >= CURRENT_DATE - INTERVAL '30 days'
                GROUP BY DATE(b.date_of_start)
                ORDER BY booking_date DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf(Locale.US, "%-12s %-10s %-15s %-12s%n",
                    "Дата", "Броней", "Выручка (₽)", "Сред.чек");
            System.out.println("─".repeat(52));
            while (rs.next()) {
                System.out.printf(Locale.US, "%-12s %-10d %-15.2f %-12.2f%n",
                        rs.getDate("booking_date"),
                        rs.getInt("new_bookings"),
                        rs.getBigDecimal("daily_revenue"),
                        rs.getBigDecimal("avg_check"));
            }
        }
        System.out.println();
    }

    // =================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // =================================================================

    /**
     * Обрезает строку до указанной длины с добавлением многоточия
     */
    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }

    /**
     * Форматирует BigDecimal для вывода с фиксированным числом знаков
     * Использует точку как разделитель независимо от локали системы
     */
    private static String formatMoney(BigDecimal value) {
        if (value == null) return "0.00";
        return String.format(Locale.US, "%.2f", value);
    }
}