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
    apiKey VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Insert some initial data
INSERT INTO users (username, apiKey, email) 
VALUES ('doris', '12345', 'doris.djivanovic@gmail.com'),
       ('admin', 'admin', 'doris.djivanovic@gmail.com'),
       ('gost', 'gost', 'doris.djivanovic@gmail.com'),
       ('neka', 'osoba', 'doris.djivanovic@gmail.com');