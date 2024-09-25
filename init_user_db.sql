-- Create a database user
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY '$MARIADB_APP_PASSWORD';
GRANT ALL PRIVILEGES ON user_management.* TO 'app_user'@'%';
--GRANT ALL ON *.* TO 'app_user'@'%';
FLUSH PRIVILEGES;

-- Create the 'users' table
CREATE TABLE IF NOT EXISTS users 
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Insert some initial data
INSERT INTO users (username, api_key, email) 
VALUES ('doris', '123e4567-e89b-12d3-a456-426614174001', 'doris.djivanovic@gmail.com'),
       ('admin', '123e4567-e89b-12d3-a456-426614174002', 'doris.djivanovic@gmail.com'),
       ('gost', '123e4567-e89b-12d3-a456-426614174003', 'doris.djivanovic@gmail.com'),
       ('neka', '123e4567-e89b-12d3-a456-426614174004', 'doris.djivanovic@gmail.com');