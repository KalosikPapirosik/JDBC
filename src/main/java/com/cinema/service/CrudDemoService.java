package com.cinema.service;

import com.cinema.dao.*;
import com.cinema.model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class CrudDemoService {

    private final GuestDao guestDao = new GuestDao();
    private final HousingDao housingDao = new HousingDao();
    private final BookingDao bookingDao = new BookingDao();
    private final CampDao campDao = new CampDao();
    private final InventoryDao inventoryDao = new InventoryDao();


    // CREATE

    public void demoCreate() throws SQLException {
        System.out.println("=== CREATE — Создание записей ===");

        Guest guest = new Guest("123456789", "Test", "Testov", "Testovich");
        int guestId = guestDao.insert(guest);
        System.out.printf("Создан посетитель: id=%d, %s%n", guestId,guest.getFirstname());
        BigDecimal cost = new BigDecimal(1000);
        Housing housing = new Housing(1, 1, "Комфорт ++", 1, cost, 1);
        int housingId = housingDao.insert(housing);
        System.out.printf("Создан Дом: id=%d, '%s', %s", housingId, housing.getComfort_lvl(), housing.getCost());
        System.out.println();
        Booking booking = new Booking(guestId, housingId, OffsetDateTime.now(), OffsetDateTime.of(2026, 7, 12, 12, 12, 12, 12, OffsetDateTime.now().getOffset()));
        int bookingId = bookingDao.insert(booking);
        System.out.printf("Создано Бронирование: id=%d, '%s', %s", bookingId, booking.getHousing_id(), booking.getGuest_id());
        System.out.println();
        List<Booking> bookings = housingDao.findBookingsByHousing(housingId);
        System.out.printf("Привязаны Бронирования к дому: %s%n",
                bookings.stream().toList());
        System.out.println();

        bookingDao.delete(bookingId);
        housingDao.delete(housingId);
        guestDao.delete(guestId);
    }

    // READ

    public void demoRead() throws SQLException {
        System.out.println("=== READ — Чтение данных ===");

        System.out.println("Все посетители:");
        System.out.printf("%s %s %s %s%n", "ID", "Имя", "Фамилия", "Документ");
        for (Guest g : guestDao.findAll()) {
            System.out.printf("%d %s %s %s%n",
                    g.getId(), g.getFirstname(), g.getLastname(), g.getDocument());
        }

        System.out.println("\nВсе Дома:");
        System.out.printf("%s %s %s %s%n", "ID", "Уровень комфорта", "Стоимость", "Бронирования");
        for (Housing h : housingDao.findAll()) {
            String bookings;
            try {
                bookings = housingDao.findBookingsByHousing(h.getId()).stream().toList().toString();
            } catch (SQLException e) {
                bookings = "?";
            }
            System.out.printf("%d %s %s %s%n",
                    h.getId(), h.getComfort_lvl(), h.getCost(), bookings);
        }

        System.out.println("\nВсе Лагеря:");
        System.out.printf("%s %s", "№", "Расположение");
        for (Camp c : campDao.findAll()) {
            System.out.printf("%d %s",
                    c.getId(), c.getLocation());
        }

        System.out.println("\nПоиск посетителя по id=1:");
        guestDao.findById(1).ifPresentOrElse(
                v -> System.out.println(v),
                () -> System.out.println("Не найден")
        );

        System.out.println("\nПоиск бронирования: дом=1, посетитель=1:");
        bookingDao.findByHousingAndGuestId(1, 1).ifPresentOrElse(
                s -> System.out.println(s),
                () -> System.out.println("Не найдено")
        );

        System.out.println();
    }

    // UPDATE

    public void demoUpdate() throws SQLException {
        System.out.println("=== UPDATE — Обновление данных ===");

        guestDao.findById(1).ifPresent(v -> {
            String oldDocument = v.getDocument();
            v.setDocument("12345678900");
            try {
                boolean ok = guestDao.update(v);
                System.out.printf("Обновлён паспорт посетителя id=1: '%s' → '%s' (успех=%b)%n",
                        oldDocument, v.getDocument(), ok);
            } catch (SQLException e) {
                System.out.println("Ошибка обновления: " + e.getMessage());
            }
        });

        housingDao.findById(1).ifPresent(f -> {
            int oldCapacity = f.getCapacity();
            f.setCapacity(20);
            try {
                boolean ok = housingDao.update(f);
                System.out.printf("Обновлён дом id=1: '%s' → '%s' (успех=%b)%n",
                        oldCapacity, f.getCapacity(), ok);
                f.setCapacity(oldCapacity);
                housingDao.update(f);
            } catch (SQLException e) {
                System.out.println("Ошибка обновления: " + e.getMessage());
            }
        });

        System.out.println();
    }

    // DELETE

    public void demoDelete() throws SQLException {
        System.out.println("=== DELETE — Удаление данных ===");

        Guest temp = new Guest("12134343", "Delete", "Me", "Please");
        int tempId = guestDao.insert(temp);
        System.out.printf("Создан временный посетитель id=%d%n", tempId);

        boolean deleted = guestDao.delete(tempId);
        System.out.printf("Удалён посетитель id=%d (успех=%b)%n", tempId, deleted);

        boolean notFound = guestDao.delete(99999);
        System.out.printf("Удаление несуществующего id=99999 (успех=%b)%n", notFound);

        System.out.println();
    }

    // BATCH INSERT

    public void demoBatchInsert() throws SQLException {
        System.out.println("=== BATCH INSERT — Массовая вставка ===");

        Camp newCamp = new Camp("Тестовая Локация");
        int campId = campDao.insert(newCamp);
        System.out.printf("Создан зал: id=%d, '%s'%n", campId, newCamp.getLocation());

        List<Inventory> inventory = new java.util.ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            inventory.add(new Inventory(campId, "Инвентарь " + i, i));
        }

        long start = System.nanoTime();
        int count = inventoryDao.batchInsert(inventory);
        long elapsed = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("Вставлено %d позиций инвентаря за %d мс (batch)%n", count, elapsed);

        campDao.delete(campId);

        System.out.println();
    }

    // TRANSACTION

    public void demoTransaction() throws SQLException {
        System.out.println("=== TRANSACTION — Покупка билета ===");
        System.out.println("Попытка забронировать жилье: посетитель=3, дом=4, дополнительные услуги отсутствуют");

        try {
            int bookingId = bookingDao.bookingByHousingAndGuestId(3, 4);
            System.out.printf("Бронь успешна! id=%d%n", bookingId);

            System.out.println("Повторная бронь...");
            try {
                bookingDao.bookingByHousingAndGuestId(3, 4);
            } catch (RuntimeException e) {
                System.out.printf("Ожидаемая ошибка: %s%n", e.getMessage());
            }

            bookingDao.delete(bookingId);
        } catch (SQLException e) {
            System.out.printf("Дом уже занят: %s%n", e.getMessage());
        }

        System.out.println();
    }

    // Утилита

    public static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
