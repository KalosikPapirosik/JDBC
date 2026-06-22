package com.cinema;

import com.cinema.db.ConnectionManager;
import com.cinema.db.SchemaInitializer;
import com.cinema.service.BusinessQueryService;
import com.cinema.service.CrudDemoService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final CrudDemoService crudDemo = new CrudDemoService();
    private static final BusinessQueryService bizQuery = new BusinessQueryService();

    public static void main(String[] args) {
        System.out.println("=== JDBC Camp Demo (Java 21 · PostgreSQL 17 · HikariCP) ===\n");

        try {
            SchemaInitializer.initialize();
            System.out.println("БД готова.\n");
        } catch (SQLException e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("""
                    [1] CRUD  [2] Запросы  [3] Всё  [0] Выход
                    > """);

            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> runCrudMenu(scanner);
                    case "2" -> runBusinessMenu(scanner);
                    case "3" -> runAllDemo();
                    case "0" -> running = false;
                    default  -> System.out.println("Неверный выбор.");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка SQL: " + e.getMessage());
            }
        }

        System.out.println("До свидания!");
        ConnectionManager.close();
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    private static void runCrudMenu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.print("""
                    [1] Create  [2] Read  [3] Update  [4] Delete
                    [5] Batch   [6] Транзакция  [7] Всё  [0] Назад
                    > """);

            switch (scanner.nextLine().trim()) {
                case "1" -> crudDemo.demoCreate();
                case "2" -> crudDemo.demoRead();
                case "3" -> crudDemo.demoUpdate();
                case "4" -> crudDemo.demoDelete();
                case "5" -> crudDemo.demoBatchInsert();
                case "6" -> crudDemo.demoTransaction();
                case "7" -> runAllCrud();
                case "0" -> { return; }
                default  -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void runAllCrud() throws SQLException {
        crudDemo.demoCreate();
        crudDemo.demoRead();
        crudDemo.demoUpdate();
        crudDemo.demoDelete();
        crudDemo.demoBatchInsert();
        crudDemo.demoTransaction();
    }

    // ── Бизнес-запросы ────────────────────────────────────────────────────────

    private static void runBusinessMenu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.print("""
                    [1] Выручка   [2] Тренды броннирования       [3] Популярные доп. усл.
                    [4] Топ-10 гостей [5] Вместимость Лагерей [6] Все
                    [0] Назад
                    > """);

            switch (scanner.nextLine().trim()) {
                case "1"  -> bizQuery.revenueByHousingType();
                case "2"  -> bizQuery.bookingTrendsLastMonth();
                case "3"  -> bizQuery.additionalServicesPopularity();
                case "4"  -> bizQuery.topGuestsByActivity();
                case "5"  -> bizQuery.campOccupancyReport();
//                case "6"  -> bizQuery.genrePopularity();
//                case "7"  -> {
//                    System.out.print("ID сеанса [1]: ");
//                    String s = scanner.nextLine().trim();
//                    bizQuery.availableSeats(s.isEmpty() ? 1 : Integer.parseInt(s));
//                }
                case "6" -> runAllBusinessQueries();
                case "0"  -> { return; }
                default   -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void runAllBusinessQueries() throws SQLException {
        bizQuery.topGuestsByActivity();
        bizQuery.campOccupancyReport();
        bizQuery.revenueByHousingType();
        bizQuery.additionalServicesPopularity();
        bizQuery.bookingTrendsLastMonth();
    }

    // ── Запустить всё ─────────────────────────────────────────────────────────

    private static void runAllDemo() throws SQLException {
        System.out.println("\n--- CRUD ---");
        runAllCrud();
        System.out.println("\n--- Бизнес-запросы ---");
        runAllBusinessQueries();
        System.out.println("\nГотово.");
    }
}

