-- Create a database user
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY '$MARIADB_APP_PASSWORD';
GRANT ALL PRIVILEGES ON device_management.* TO 'app_user'@'%';
--GRANT ALL ON *.* TO 'app_user'@'%';
FLUSH PRIVILEGES;

-- Create the 'devices' table
CREATE TABLE IF NOT EXISTS devices 
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    location VARCHAR(255)
);

-- Create the 'user_device' association table
-- Ensure that if a device is deleted, its associations are automatically removed from the table
CREATE TABLE IF NOT EXISTS user_device 
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    device_id BIGINT NOT NULL,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE
);

-- Insert some initial data
INSERT INTO devices (name, status, location) 
VALUES ('Thermostat', 'active', 'Living Room'),
       ('Light Switch', 'inactive', 'Bedroom'),
       ('Smart Bulb', 'active', 'Bedroom');

INSERT INTO user_device (user_id, device_id) 
VALUES (1, 1),
       (2, 1),
       (3, 1);
