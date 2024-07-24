# Сервис бронирования отелей

## Обзор

Сервис бронирования отелей — это веб-приложение, которое позволяет пользователям бронировать отели и номера, просматривать рейтинги и фильтровать результаты поиска по различным критериям. Администраторы могут управлять контентом через административную панель CMS и получать детализированную статистику о работе системы. Проект использует Spring Boot, Spring Data JPA, Kafka и MongoDB.

## Возможности

1. **Управление отелями**:
    - Создание, редактирование, просмотр и удаление отелей.
    - Поиск и фильтрация отелей по имени, городу, рейтингу и т.д.
    - Пагинация и сортировка списков отелей.

2. **Управление номерами**:
    - CRUD-операции для номеров.
    - Фильтрация номеров по цене, вместимости, доступности и т.д.

3. **Управление пользователями**:
    - Регистрация и вход с ролями (Пользователь/Администратор).
    - Безопасная аутентификация с использованием Spring Security.

4. **Управление бронированиями**:
    - Бронирование номеров на определенные даты, с проверкой на перекрытие.
    - Просмотр истории бронирований с пагинацией.

5. **Статистика и отчеты**:
    - Сбор статистики о регистрации пользователей и бронированиях.
    - Экспорт данных в формате CSV для анализа.

6. **Безопасность**:
    - Основная аутентификация.
    - Контроль доступа на основе ролей.

## Технологии

- **Backend**: Spring Boot, Spring Web MVC, Spring Data JPA, Spring Security
- **База данных**: PostgreSQL, MongoDB
- **Сообщения**: Kafka
- **Маппинг**: MapStruct
- **Система сборки**: Gradle
- **Docker**: Docker, Docker Compose

## Установка и настройка

### Требования

- Java 21
- Docker и Docker Compose
- Maven

### Клонирование репозитория

```bash
git clone https://github.com/Bunchiek/booking-service.git
```

### Запуск приложения
Запустите приложение и необходимые сервисы с помощью Docker Compose:
```bash
docker-compose up
```
Эта команда настроит следующие сервисы:

- **PostgreSQL:** База данных для основной информации приложения.
- **MongoDB:** База данных для хранения статистики событий.
- **Kafka:** Сообщений для обработки событий.
- **ZooKeeper:** Для координации Kafka.
Доступ к приложению
Административная панель: Доступна только для пользователей с ролью ROLE_ADMIN.

## Конфигурация окружения
### Конфигурация базы данных:
Измените файл application.yml для настроек PostgreSQL и MongoDB.
### Конфигурация Kafka:
Настройки Kafka можно найти в файле application.yml.
## Использование
Управление отелями и номерами
1. **Административная панель:**
- Администраторы могут создавать и управлять отелями и номерами.
- Администраторы могут просматривать и экспортировать статистику бронирований.
- Используйте API или административную панель для CRUD-операций.
2. **Действия пользователей:**
- Пользователи могут зарегистрироваться, войти в систему и просматривать доступные отели и номера.
- Бронирование номеров на указанные даты.


