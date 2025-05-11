-- init_database.sql
CREATE TABLE IF NOT EXISTS roles (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
    );

CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    role_id INTEGER REFERENCES roles(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS employee_positions (
                                                  id SERIAL PRIMARY KEY,
                                                  name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
    );

CREATE TABLE IF NOT EXISTS employees (
                                         id SERIAL PRIMARY KEY,
                                         first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    position_id INTEGER REFERENCES employee_positions(id),
    hire_date DATE,
    experience INTEGER, -- опыт в месяцах
    passport VARCHAR(100) UNIQUE,
    contact_info TEXT,
    active BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS flights (
                                       id SERIAL PRIMARY KEY,
                                       flight_number VARCHAR(20) NOT NULL UNIQUE,
    departure_airport VARCHAR(100) NOT NULL,
    arrival_airport VARCHAR(100) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    aircraft_type VARCHAR(100) NOT NULL,
    status VARCHAR(50) DEFAULT 'SCHEDULED', -- SCHEDULED, DELAYED, CANCELLED, COMPLETED
    created_by INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS crews (
                                     id SERIAL PRIMARY KEY,
                                     flight_id INTEGER REFERENCES flights(id),
    name VARCHAR(100),
    created_by INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS crew_members (
                                            id SERIAL PRIMARY KEY,
                                            crew_id INTEGER REFERENCES crews(id),
    employee_id INTEGER REFERENCES employees(id),
    UNIQUE(crew_id, employee_id)
    );

-- Наполнение начальными данными
INSERT INTO roles (name, description) VALUES
                                          ('ADMIN', 'Администратор системы'),
                                          ('DISPATCHER', 'Диспетчер рейсов')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO employee_positions (name, description) VALUES
                                                       ('PILOT', 'Пилот'),
                                                       ('CO_PILOT', 'Второй пилот'),
                                                       ('NAVIGATOR', 'Штурман'),
                                                       ('RADIO_OPERATOR', 'Радист'),
                                                       ('STEWARD', 'Стюард'),
                                                       ('STEWARDESS', 'Стюардесса')
    ON CONFLICT (name) DO NOTHING;

-- Создание первого пользователя-администратора
INSERT INTO users (username, password, first_name, last_name, email, role_id)
VALUES ('admin', 'admin123', 'Администратор', 'Системы', 'admin@airline.com',
        (SELECT id FROM roles WHERE name = 'ADMIN'))
    ON CONFLICT (username) DO NOTHING;

-- Создание пользователя-диспетчера
INSERT INTO users (username, password, first_name, last_name, email, role_id)
VALUES ('dispatcher', 'dispatcher123', 'Иван', 'Диспетчеров', 'dispatcher@airline.com',
        (SELECT id FROM roles WHERE name = 'DISPATCHER'))
    ON CONFLICT (username) DO NOTHING;