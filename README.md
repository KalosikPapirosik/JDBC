# JDBC Cinema Demo — Демонстрация JDBC + PostgreSQL

Консольное приложение на **Java 21** + **JDBC** + **PostgreSQL**, демонстрирующее работу с реляционной БД на примере системы кинотеатра.

## Требования

- [Java 21 JDK](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [PostgreSQL 17](https://www.postgresql.org/download/) (локально или Docker)

### Быстрый запуск PostgreSQL через Docker

```bash
docker run -d --name postgres-cinema \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:17
```

Создайте базу данных:
```bash
docker exec -it postgres-cinema psql -U postgres -c "CREATE DATABASE lab-psql-camp;"
```

## Как запустить

1. **Настройте** подключение в `src/main/resources/application.properties`:
   ```properties
   db.url=jdbc:postgresql://localhost:5432/cinema
   db.username=postgres
   db.password=postgres
   ```

2. **Запустите**:
   ```bash
   mvn clean compile exec:java
   ```

Приложение автоматически создаст таблицы и заполнит тестовыми данными.

## Зависимости

| Пакет | Версия | Назначение |
|-------|--------|------------|
| `postgresql` | 42.7.5 | PostgreSQL JDBC Driver |
| `HikariCP` | 6.2.1 | Connection Pool |
| `slf4j-api` | 2.0.16 | Logging API |
| `logback-classic` | 1.5.15 | Logging Implementation |
